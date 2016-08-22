package com.melinkr.ice;

import com.melinkr.ice.server.IceServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class HttpServerTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("conf/http-server.xml");
        IceServer server =  context.getBean(IceServer.class);
//        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.stop();
            }
        });

    }
}
