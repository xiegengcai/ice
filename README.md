# HTTP REST API Service
## 加入依赖
```xml
<dependency>
    <groupId>com.melinkr</groupId>
    <artifactId>ice-core</artifactId>
    <version>${ice.version}</version>
</dependency>
```
## 增加配置项
1. 默认conf/iceServer.properties，如需修改，请自身实现IceServerConfig接口或继承DefaultServerConfig
2. 配置举例
```
server.port=8080
server.maxContentLength=8192
## IP 黑名单，支持IPV4&IPV6。;分隔
server.black.ip=

## 负责处理客户端的TCP连接请求，建议1
boss.thread.bossThreadSize=1
## IO读写线程，建议1<N<CPU核心数*2
boss.thread.wokerThreadSize=3

## 是否开启签名验证
ice.service.openSign=true
## 是否开启session验证
ice.service.openSession=false
## 是否开启Timestamp验证
ice.service.openTimestamp=false
请求时间与服务收到时间允许最大误差。单位：秒，默认60s
ice.service.timestampRange=60
# 缺省超时时间 60s
ice.service.maxTimeout=60
# 缺省超时时间 30s
ice.service.defaultTimeout=30
```

## 引入Spring配置
```xml
<!-- 包扫描 -->
<context:annotation-config />
<context:component-scan base-package="com.melinkr.ice" use-default-filters="false">
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Service" />
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Component" />
</context:component-scan>
<!-- iceServer注册 -->
<bean id="iceServer" class="com.melinkr.ice.server.HttpIceServer">
    <constructor-arg>
        <bean class="com.melinkr.ice.server.handler.HttpServerInitializer">
            <constructor-arg>
                <!-- 标准实现，支持get/post url 参数、post request attribute及post body data（p1=v1&p2=v2及json两种格式）-->
                <bean class="com.melinkr.ice.server.handler.HttpServerHandler"/>
                <!-- 简单实现，只支持post json body data-->
                <!--
                <bean class="com.melinkr.ice.server.handler.JsonServerHandler"/>
                -->
            </constructor-arg>
        </bean>
    </constructor-arg>
</bean>
```