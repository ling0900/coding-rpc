package io.lh.rpc.commom.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描
 *
 * @author lh
 * @date 2023 /02/11
 */
public class ClassScanner {

    /**
     * 注意这里的是static
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);

    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";

    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";

    /**
     * class文件的后缀
     */
    private static final String PROTOCOL_SUFFIX = ".class";

    /**
     * 扫描类路径下所有的类信息，然后放到一个list中缓存起来。
     * <></>例如：io.lh.rpc.commom.scanner
     *
     * @param packageName the package name
     * @return {@link List}<{@link String}>
     * @throws IOException the io exception
     */
    public static List<String> getClassNameList(String packageName) throws IOException {

        LOGGER.info("根据包路径，扫描class");

        // 定义返回list
        List<String> classNameList = new ArrayList<>();

        //包名转文件目录
        String packageDirName = packageName.replace(".", "/");

        // 获取目录下的内容：1、不存在下级目录 2、存在下级目录 3、jar包 4、是文件（非jar包）
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        // 遍历，一层层目录遍历
        while (dirs.hasMoreElements()) {

            // 当前目录
            URL url = dirs.nextElement();

            // 物理路径
            String filePath = URLDecoder.decode(url.getPath(), "UTF-8");

            // 协议的名称 todo
            String protocol = url.getProtocol();

            LOGGER.info("当前目录是{}，物理路径是{}，协议名称是{}", url, filePath, protocol);

            // 这可能是目录，可能是文件。
            if (PROTOCOL_FILE.equals(protocol)) {
                //
                addClassesInPackageByFile(filePath, packageName, classNameList, true);
                // jar包
            } else if (PROTOCOL_JAR.equals(protocol)) {
                //
                packageName = findAndAddClassesInPackageByJar(packageName, packageDirName, url, classNameList);
            }
        }
        return classNameList;
    }

    /**
     * 扫描当前工程中指定包下的所有类信息
     *
     * @param filePath      the file path
     * @param packageName   the package name
     * @param classNameList the class name list
     * @param hasChildPath  the has child path
     */
    static void addClassesInPackageByFile(String filePath, String packageName, List<String> classNameList, boolean hasChildPath) {



        // 根据路径new一个file，有可能是一个目录或者一个文件。
        File file = new File(filePath);

        // 不存在或者不是目录
        if (!file.exists() || !file.isDirectory()) {
            return;
        }

        // 过滤条件：如果是class文件 或者 目录
        File[] files = file.listFiles(
                (pathname) -> (pathname.isDirectory()) || pathname.getName().endsWith(PROTOCOL_SUFFIX)
        );

        // 遍历
        for (File f : files) {

            // 目录
            if (f.isDirectory()) {
                // 递归
                addClassesInPackageByFile(f.getPath(), packageName + "." + f.getName(), classNameList, hasChildPath);
            } else {
                // class类
                String className = f.getName().substring(0, f.getName().length() - 6);
                // add
                classNameList.add(packageName + "." +className);
            }

        }

    }

    /**
     * Find and add classes in package by jar string.
     * todo 这个方法待测试验证
     * @param packageName    the package name
     * @param packageDirName the package dir name
     * @param url            the url
     * @param classNameList  the class name list
     * @return the string
     * @throws IOException the io exception
     */
    static String findAndAddClassesInPackageByJar(String packageName, String packageDirName, URL url, List<String> classNameList) throws IOException {

        LOGGER.info("通过jar加载class");
        // jar，其实是一个压缩包
        JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();

        Enumeration<JarEntry> jars = jarFile.entries();

        while (jars.hasMoreElements()) {

            // 可能是 目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry jarEntry = jars.nextElement();
            String name = jarEntry.getName();

            // 是目录
            if (name.startsWith("/")) {
                name = name.substring(1);
            }

            if (name.startsWith(packageDirName)) {
                int i = name.lastIndexOf("/");
                if (i != -1) {
                    if (name.endsWith(PROTOCOL_SUFFIX)) {
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        classNameList.add(packageName + "." + className);
                    }
                }
            }
        }
        return packageName;
    }
}
