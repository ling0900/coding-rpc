package io.lh.rpc.constants;

/**
 * The type Rpc constants.
 * 这里面的各种序列化方式，结合SPI机制来使用的。
 *
 * @author lh
 */
public class RpcConstants {

    /**
     * 消息头: 固定32个字节
     */
    public static final int HEADER_TOTAL_LEN = 32;

    /**
     * 魔数
     */
    public static final short MAGIC = 0x10;

    /**
     * 版本号
     */
    public static final byte VERSION = 0x1;

    /**
     * REFLECT_TYPE_JDK
     * 反射的类型JDK
     */
    public static final String REFLECT_TYPE_JDK = "jdk";

    /**
     * REFLECT_TYPE_CGLIB
     */
    public static final String REFLECT_TYPE_CGLIB = "cglib";

    /**
     * JDK动态代理
     */
    public static final String PROXY_JDK = "jdk";
    /**
     * javassist动态代理
     */
    public static final String PROXY_JAVASSIST = "javassist";
    /**
     * cglib动态代理
     */
    public static final String PROXY_CGLIB = "cglib";

    /**
     * 初始化的方法
     */
    public static final String INIT_METHOD_NAME = "init";

    /**
     * zookeeper
     */
    public static final String REGISTRY_CENTER_ZOOKEEPER = "zookeeper";
    /**
     * nacos
     */
    public static final String REGISTRY_CENTER_NACOS = "nacos";
    /**
     * apoll
     */
    public static final String REGISTRY_CENTER_APOLL = "apoll";
    /**
     * etcd
     */
    public static final String REGISTRY_CENTER_ETCD = "etcd";
    /**
     * eureka
     */
    public static final String REGISTRY_CENTER_EUREKA = "eureka";

    /**
     * protostuff 序列化
     */
    public static final String SERIALIZATION_PROTOSTUFF = "protostuff";
    /**
     * FST 序列化
     */
    public static final String SERIALIZATION_FST = "fst";
    /**
     * hessian2 序列化
     */
    public static final String SERIALIZATION_HESSIAN2 = "hessian2";
    /**
     * jdk 序列化
     */
    public static final String SERIALIZATION_JDK = "jdk";
    /**
     * json 序列化
     */
    public static final String SERIALIZATION_JSON = "json";
    /**
     * kryo 序列化
     */
    public static final String SERIALIZATION_KRYO = "kryo";
    /**
     * 基于ZK的一致性Hash负载均衡
     */
    public static final String SERVICE_LOAD_BALANCER_ZKCONSISTENTHASH = "zkconsistenthash";

    /**
     * The constant SERVICE_LOAD_BALANCER_RANDOM.
     */
    public static final String SERVICE_LOAD_BALANCER_RANDOM = "random";

    /**
     * Main.测试用的
     *
     * @param args the args
     */
    public static final String HEARTBEAT_PONG = "pong";

    /**
     * The constant HEARTBEAT_PING.
     */
    public static final Object HEARTBEAT_PING = "ping";

    /**
     * The constant CODEC_DECODER.
     */
    public static final String CODEC_DECODER = "decoder";

    /**
     * encoder
     */
    public static final String CODEC_ENCODER = "encoder";

    /**
     * handler
     */
    public static final String CODEC_HANDLER = "handler";

    /**
     * server-idle-handler
     */
    public static final String CODEC_SERVER_IDLE_HANDLER = "server-idle-handler";

    /**
     * client-idle-handler
     */
    public static final String CODEC_CLIENT_IDLE_HANDLER = "client-idle-handler";

    /**
     * The constant DEFAULT_RETRY_INTERVAL.
     */
    public static final int DEFAULT_RETRY_INTERVAL = 1000;

    /**
     * The constant DEFAULT_RETRY_TIMES.
     */
    public static final int DEFAULT_RETRY_TIMES = 3;

    /**
     * 最小权重
     */
    public static final int SERVICE_WEIGHT_MIN = 1;
    /**
     * 最大权重
     */
    public static final int SERVICE_WEIGHT_MAX = 100;

    /**
     * 框架的默认版本号
     */
    public static final String RPC_COMMON_DEFAULT_VERSION = "1.0.0";

    /**
     * 框架默认的分组
     */
    public static final String RPC_COMMON_DEFAULT_GROUP = "lh";

    /**
     * RPC框架默认的心跳间隔时间
     */
    public static final int RPC_COMMON_DEFAULT_HEARTBEATINTERVAL = 30000;

    /**
     * 服务提供者,默认的扫描并移除不活跃连接的间隔时间
     */
    public static final int RPC_COMMON_DEFAULT_SCANNOTACTIVECHANNELINTERVAL = 60000;

    /**
     * 服务消费者默认的注册中心类型
     */
    public static final String RPC_REFERENCE_DEFAULT_REGISTRYTYPE = "zookeeper";

    /**
     * 服务消费者默认的注册中心地址
     */
    public static final String RPC_REFERENCE_DEFAULT_REGISTRYADDRESS = "127.0.0.1:2181";

    /**
     * 服务消费者默认负载均衡类型
     */
    public static final String RPC_REFERENCE_DEFAULT_LOADBALANCETYPE = "zkconsistenthash";

    /**
     * 服务消费者默认的序列化方式
     */
    public static final String RPC_REFERENCE_DEFAULT_SERIALIZATIONTYPE = "protostuff";

    /**
     * 服务消费者默认的超时时间
     */
    public static final int RPC_REFERENCE_DEFAULT_TIMEOUT = 5000;

    /**
     * 服务消费者默认的代理
     */
    public static final String RPC_REFERENCE_DEFAULT_PROXY = "jdk";

    /**
     * 服务消费者默认的重试间隔时间
     */
    public static final int RPC_REFERENCE_DEFAULT_RETRYINTERVAL = 1000;

    /**
     * 服务消费者默认的重试次数
     */
    public static final int RPC_REFERENCE_DEFAULT_RETRYTIMES = 3;

    /**
     * 默认直连服务地址
     */
    public static final String RPC_COMMON_DEFAULT_DIRECT_SERVER = "";

    /**
     * IP、端口分隔符
     */
    public static final String IP_PORT_SPLIT = ":";


    public static final String RPC_MULTI_DIRECT_SERVERS_SEPARATOR = ",";
}
