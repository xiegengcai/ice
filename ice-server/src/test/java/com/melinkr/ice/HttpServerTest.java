package com.melinkr.ice;

import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.context.IceContext;
import com.melinkr.ice.http.HttpIceServer;
import com.melinkr.ice.http.handler.HttpServerHandler;
import com.melinkr.ice.http.handler.HttpServerInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class HttpServerTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("conf/http-server.xml");
        IceServerConfig serverConfig = context.getBean(IceServerConfig.class);
        Dispatcher dispatcher = context.getBean(Dispatcher.class);
        IceServer server = new HttpIceServer(new HttpServerInitializer(new HttpServerHandler(serverConfig , dispatcher
        )));
        server.start();
//        IceServer server = (IceServer) IceContext.getBean("httpServer");
//        server.stop();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.stop();
            }
        });

    }
}
