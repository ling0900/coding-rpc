package io.lh.rpc.protocol.request;

import io.lh.rpc.protocol.base.RpcMessage;

/**
 * The type Rpc request.
 */
public class RpcRequest extends RpcMessage {
    private static final long serialVersionUID = -3713278488615579572L;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes; // 为何这么写，思考以下？

    private Object[] parameters;

    private String version;

    private String group;

    /**
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets method name.
     *
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets method name.
     *
     * @param methodName the method name
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Get parameter types class [ ].
     *
     * @return the class [ ]
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    /**
     * Sets parameter types.
     *
     * @param parameterTypes the parameter types
     */
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * Get parameters object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * Sets parameters.
     *
     * @param parameters the parameters
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(String group) {
        this.group = group;
    }
}
