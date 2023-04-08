package loader;

import com.alibaba.fastjson.JSON;
import io.lh.rpc.spi.annotation.SPI;
import io.lh.rpc.spi.annotation.SPIClass;
import io.lh.rpc.spi.factry.IExtensionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Extension loader.
 * 这是一个核心类
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("all")
public class ExtensionLoader<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    /**
     * 下面的这几个目录，其实可以再继续拓展的。
     */
    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private static final String LH_DIRECTORY = "META-INF/lh/";
    private static final String LH_DIRECTORY_EXTERNAL = "META-INF/lh/external/";
    private static final String LH_DIRECTORY_INTERNAL = "META-INF/lh/internal/";

    private static final String[] SPI_DIRECTORIES = new String[] {
            SERVICES_DIRECTORY,
            LH_DIRECTORY,
            LH_DIRECTORY_EXTERNAL,
            LH_DIRECTORY_INTERNAL
    };

    private static final Map<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    private final Class<T> finalClazz;

    private final ClassLoader classLoader;

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> spiClassInstances = new ConcurrentHashMap<>();

    private String cachedDefaultName;

    /**
     * Instantiates a new Extension loader.
     * 构造器
     * @param clazz the clazz.
     */
    private ExtensionLoader(final Class<T> clazz, final ClassLoader cl) {
        this.finalClazz = clazz;
        this.classLoader = cl;
        // 比较传入的类是否是IExtensionFactory
        if (!Objects.equals(clazz, IExtensionFactory.class)) {
            ExtensionLoader<IExtensionFactory> extensionLoader = ExtensionLoader.getExtensionLoader(IExtensionFactory.class);
            extensionLoader.getExtensionClasses();
        }
    }

    /**
     * Gets extension loader.
     * 获取拓展类加载器
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param cl    the cl
     * @return the extension loader.
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz, final ClassLoader cl) {

        Objects.requireNonNull(clazz, "extension clazz is null");

        // 如果不是接口
        if (! clazz.isInterface()) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") is not interface!");
        }
        // 如果没有标注SPI注解
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") without @" + SPI.class + " Annotation");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
        if (Objects.nonNull(extensionLoader)) {
            return extensionLoader;
        }
        // 有待于思考
        LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz, cl));
        return (ExtensionLoader<T>) LOADERS.get(clazz);
    }


    /**
     * 获取拓展类实例。
     *
     * @param <T>   泛型类型
     * @param clazz 接口的Class实例
     * @param name  SPI名称
     * @return 泛型实例 t
     */
    public static <T> T getExtension(final Class<T> clazz, String name){
        ExtensionLoader<T> extensionLoader = getExtensionLoader(clazz);
        if (StringUtils.isEmpty(name)) {
            return extensionLoader.getDefaultSpiClassInstance();
        }
        return extensionLoader.getSpiClassInstance(name);
    }

    /**
     * Gets extension loader.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the extension loader
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
        return getExtensionLoader(clazz, classLoader);
    }

    /**
     * Gets default spi class instance.
     *
     * @return the default spi class instance.
     */
    public T getDefaultSpiClassInstance() {
        getExtensionClasses();
        if (StringUtils.isBlank(cachedDefaultName)) {
            return null;
        }
        return getSpiClassInstance(cachedDefaultName);
    }

    /**
     * Gets spi class.
     *
     * @param name the name
     * @return the spi class instance.
     */
    public T getSpiClassInstance(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("get spi class name is null");
        }
        Holder<Object> objectHolder = cachedInstances.get(name);
        if (Objects.isNull(objectHolder)) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            objectHolder = cachedInstances.get(name);
        }
        Object value = objectHolder.getValue();
        if (Objects.isNull(value)) {
            synchronized (cachedInstances) {
                value = objectHolder.getValue();
                if (Objects.isNull(value)) {
                    value = createExtension(name);
                    objectHolder.setValue(value);
                }
            }
        }
        return (T) value;
    }

    /**
     * get all spi class spi.
     *
     * @return list. spi instances
     */
    public List<T> getSpiClassInstances() {
        Map<String, Class<?>> extensionClasses = this.getExtensionClasses();
        if (extensionClasses.isEmpty()) {
            return Collections.emptyList();
        }
        if (Objects.equals(extensionClasses.size(), cachedInstances.size())) {
            return (List<T>) this.cachedInstances.values().stream().map(e -> {
                return e.getValue();
            }).collect(Collectors.toList());
        }
        List<T> instances = new ArrayList<>();
        extensionClasses.forEach((name, v) -> {
            T instance = this.getSpiClassInstance(name);
            instances.add(instance);
        });
        return instances;
    }

    @SuppressWarnings("unchecked")
    private T createExtension(final String name) {
        Map<String, Class<?>> extensionClasses = getExtensionClasses();
        Class<?> innerClazz = extensionClasses.get(name);
        if (Objects.isNull(innerClazz)) {
            throw new IllegalArgumentException("name is error");
        }
        Object o = spiClassInstances.get(innerClazz);
        if (Objects.isNull(o)) {
            try {
                spiClassInstances.putIfAbsent(innerClazz, innerClazz.newInstance());
                o = spiClassInstances.get(innerClazz);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Extension instance(name: " + name + ", class: "
                        + innerClazz + ")  could not be instantiated: " + e.getMessage(), e);

            }
        }
        return (T) o;
    }

    /**
     * Gets extension classes.
     *
     * @return the extension classes
     */
    public Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.getValue();
        // 双重检测
        if (Objects.isNull(classes)) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getValue();
                if (Objects.isNull(classes)) {
                    classes = loadExtensionClass();
                    if (Objects.isNull(classes) || classes.size() < 1) {
//                        throw new RuntimeException("实在获取不到实例");
                    }
                    cachedClasses.setValue(classes);
                }
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClass() {
        SPI annotation = finalClazz.getAnnotation(SPI.class);
        if (Objects.nonNull(annotation)) {
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                cachedDefaultName = value;
            }
        }
        Map<String, Class<?>> clazzMap = new HashMap<>(16);
        loadDirectory(clazzMap);
        if (clazzMap == null || clazzMap.size() < 1) {
//            throw new RuntimeException("loadExtensionClass 失败了！");
        }
        return clazzMap;
    }

    private void loadDirectory(final Map<String, Class<?>> classMap) {
        logger.info("要遍历的路径有一下\n：{}", JSON.toJSONString(SPI_DIRECTORIES));

        for (String directory : SPI_DIRECTORIES){
            logger.info("当前遍历的路径是{}", directory);
            String fileName = directory + finalClazz.getName();
            logger.info("文件名字是{}", fileName);

            try {
                Enumeration<URL> urls;
                if (Objects.nonNull(this.classLoader)) {
                    urls = classLoader.getResources(fileName);
                } else {
                    urls = ClassLoader.getSystemResources(fileName);
                }
                if (Objects.nonNull(urls)) {
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        loadResources(classMap, url);
                    }
                }
            } catch (IOException t) {
                logger.error("load extension class error {}", fileName, t);
            }
        }

        logger.info("最后，map的内容如下{}", JSON.toJSONString(classMap));
    }

    private void loadResources(final Map<String, Class<?>> classes, final URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                String name = (String) k;
                String classPath = (String) v;
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
                        loadClass(classes, name, classPath);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("load extension resources error", e);
                    }
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("load extension resources error", e);
        }
    }

    private void loadClass(final Map<String, Class<?>> classes,
                           final String name, final String classPath) throws ClassNotFoundException {
        Class<?> subClass = Objects.nonNull(this.classLoader) ? Class.forName(classPath, true, this.classLoader) : Class.forName(classPath);
        if (!finalClazz.isAssignableFrom(subClass)) {
            throw new IllegalStateException("load extension resources error," + subClass + " subtype is not of " + finalClazz);
        }
        if (!subClass.isAnnotationPresent(SPIClass.class)) {
            throw new IllegalStateException("load extension resources error," + subClass + " without @" + SPIClass.class + " annotation");
        }
        Class<?> oldClass = classes.get(name);
        if (Objects.isNull(oldClass)) {
            classes.put(name, subClass);
        } else if (!Objects.equals(oldClass, subClass)) {
            throw new IllegalStateException("load extension resources error,Duplicate class " + finalClazz.getName() + " name " + name + " on " + oldClass.getName() + " or " + subClass.getName());
        }
    }

    /**
     * The type Holder.
     *
     * @param <T> the type parameter.
     */
    public static class Holder<T> {

        private volatile T value;

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(final T value) {
            this.value = value;
        }
    }


}
