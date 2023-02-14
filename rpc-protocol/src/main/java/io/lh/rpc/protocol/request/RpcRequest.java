package io.lh.rpc.protocol.request;

import io.lh.rpc.protocol.base.RpcMessage;

public class RpcRequest extends RpcMessage {
    private static final long serialVersionUID = -3713278488615579572L;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes; // 为何这么写，思考以下？

    private Object[] parameters;

    private String version;

    private String group;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
