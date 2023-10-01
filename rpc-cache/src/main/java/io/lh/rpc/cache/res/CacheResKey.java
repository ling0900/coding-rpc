package io.lh.rpc.cache.res;

import java.util.Arrays;
import java.util.Objects;

/**
 * The type Cache res key.
 * 缓存结果数据的 key
 */
public class CacheResKey {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 89878789989L;

    /**
     * 保存缓存时的时间戳
     */
    private long cacheTimeStamp;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型数组
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数数组
     */
    private Object[] parameters;

    /**
     * 版本号
     */
    private String version;

    /**
     * 服务分组
     */
    private String group;

    /**
     * Instantiates a new Cache res key.
     *
     * @param className      the class name
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param parameters     the parameters
     * @param version        the version
     * @param group          the group
     */
    public CacheResKey(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters, String version, String group) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
        this.version = version;
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        // 先判断是否是同一个对象
        if (this == o) return true;
        // 如果对象是null或则类不一致
        if (o == null || getClass() != o.getClass()) return false;
        // 强转后，比较内容
        CacheResKey cacheKey = (CacheResKey) o;
        return  Objects.equals(className, cacheKey.className)
                && Objects.equals(methodName, cacheKey.methodName)
                && Arrays.equals(parameterTypes, cacheKey.parameterTypes)
                && Arrays.equals(parameters, cacheKey.parameters)
                && Objects.equals(version, cacheKey.version)
                && Objects.equals(group, cacheKey.group);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName, version, group);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    /**
     * Gets cache time stamp.
     *
     * @return the cache time stamp
     */
    public long getCacheTimeStamp() {
        return cacheTimeStamp;
    }

    /**
     * Sets cache time stamp.
     *
     * @param cacheTimeStamp the cache time stamp
     */
    public void setCacheTimeStamp(long cacheTimeStamp) {
        this.cacheTimeStamp = cacheTimeStamp;
    }
}
