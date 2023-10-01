package io.lh.rpc.loadbalancer.context;

import io.lh.rpc.protocol.meta.ServiceMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author: Ling
 * @date: 2023年10月01日 16:21
 * @since 1.0.0
 */
public class ConnectionsContext {
    private static volatile Map<String, Integer> connectionsMap = new ConcurrentHashMap<>();

    public static void add(ServiceMeta serviceMeta){
        String key = generateKey(serviceMeta);
        Integer count = connectionsMap.get(key);
        if (count == null){
            count = 0;
        }
        count++;
        connectionsMap.put(key, count);
    }

    public static Integer getValue(ServiceMeta serviceMeta){
        String key = generateKey(serviceMeta);
        return connectionsMap.get(key);
    }

    private static String generateKey(ServiceMeta serviceMeta){
        return serviceMeta.getServiceAddr().concat(":").concat(String.valueOf(serviceMeta.getServicePort()));
    }

}
