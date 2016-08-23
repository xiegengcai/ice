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

## 包扫描
```xml
   <!-- 包扫描 -->
   <context:annotation-config />
   <context:component-scan base-package="com.melinkr.ice" use-default-filters="false">
       <context:include-filter type="annotation" expression="org.springframework.stereotype.Service" />
       <context:include-filter type="annotation" expression="org.springframework.stereotype.Component" />
   </context:component-scan>
```
## 注册拦截器
```xml
<bean id="iterceptorChain" class="com.melinkr.ice.interceptor.IterceptorChain">
    <constructor-arg>
        <!-- 按先后执行顺序注册拦截器 -->
        <list>
            <!-- 连接器配置举例 -->
            <bean class="com.melinkr.ice.interceptor.BaseInterceptor">
                <property name="ignoreParams">
                    <!-- 第一个忽略的参数 -->
                    <value>appKey</value>
                    <!-- 第二个-->
                </property>
                <property name="ignoreValues">
                    <util:list value-type="java.util.List">
                        <!-- 第一个忽略的参数哪些值忽略-->
                        <list value-type="java.lang.String">
                            <value>0</value>
                            <value>1</value>
                            <value>2</value>
                            <value>3</value>
                        </list>
                        <!-- 第二个忽略的参数哪些值忽略-->
                    </util:list>
                </property>
            </bean>
        </list>
    </constructor-arg>
</bean>
```
或没有拦截器
```xml
<bean id="iterceptorChain" class="com.melinkr.ice.interceptor.IterceptorChain"/>
```

## 注册ICE服务
```xml
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