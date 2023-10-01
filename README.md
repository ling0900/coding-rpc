# 🐬coding-rpc🐬

## 🐬项目结构

rpc-annotation：注解模块

rpc-codec:编码解码模块

rpc-common:公共工具类模块

rpc-constants:常量模块

rpc-protocol:协议模块

rpc-provider:服务提供者模块

rpc-serialization:序列化模块

todo



## 🐬开发笔记

除了从注册中心选择具体的服务列表，框架应该也要能够具备直接连接服务的能力（直连能力）。

### docker整合部分

在整合docker的时候，修改了服务提供者的类，增加了字段：serverRegistryAddress。

serverAddress：
该参数为服务提供者启动时监听的IP和端⼝，如果用了容器，这个是容器内部的IP（比如是 0.0.0.0:20880）；
serverRegistryAddress为服务提供者启动时注册到注册中⼼的IP和端⼝，这个是宿主机的。




## 🐬个人的公开文档（pc端打开显示更佳）
🌐🌐 https://www.yuque.com/javalh

今天，终于用到了正版的idea！

## 💧常用的学习网站
1、zookeeper的学习网址：
https://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/ZooKeeper%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90%E4%B8%8E%E5%AE%9E%E6%88%98-%E5%AE%8C/00%20%E5%BC%80%E7%AF%87%E8%AF%8D%EF%BC%9A%E9%80%89%E6%8B%A9%20ZooKeeper%EF%BC%8C%E4%B8%80%E6%AD%A5%E5%88%B0%E4%BD%8D%E6%8E%8C%E6%8F%A1%E5%88%86%E5%B8%83%E5%BC%8F%E5%BC%80%E5%8F%91.md

## 🐬思考
1、服务者、消费者，他们两个的序列化方式可以是不同的吗，比如一个是JDK一个CGLIB？

2、netty的心跳机制

3、zk实现注册中心的优缺点（需要集群吗）

4、SPI机制怎么用到工作中呢？和starter有什么关系吗，从灵活性来说一下

5、spring-boot-starter的集成方案，其实就是对spring集成方案的一个封装。

## 🐬遇到的问题：
1、Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass
这个问题也是出现在换了jdk版本后出现的，解决方案参考：
https://blog.csdn.net/m0_52155263/article/details/120867149

https://leetcode.cn/u/lh0900/


