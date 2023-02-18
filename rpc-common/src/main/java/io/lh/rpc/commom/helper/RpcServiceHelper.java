package io.lh.rpc.commom.helper;

/**
 * The type Rpc service helper.
 *
 * @author lh
 */
public class RpcServiceHelper {

    /**
     * Build service key string.
     *
     * @param serviceName    the service name
     * @param ServiceVersion the service version
     * @param group          the group
     * @return 服务名称#服务把本好#服务分组
     */
    public static String buildServiceKey(String serviceName, String serviceVersion, String group) {
        return String.join("#", serviceName, serviceVersion, group);
    }
}
