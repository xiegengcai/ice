package com.xiegengcai.ice;

import com.xiegengcai.ice.server.IceServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/22.
 */
public class Main {

    public static final String SHUTDOWN_HOOK_KEY = "ice.shutdown.hook";
    public static final String SPRING_CONFIG = "ice.spring.config";
    public static final String DEFAULT_SPRING_CONFIG = "classpath*:META-INF/spring/*.xml";

    private static volatile boolean running = true;

    public static void main(String[] args) {
        String configPath = System.getProperty(SPRING_CONFIG);
        if (configPath == null || configPath.length() == 0) {
            configPath = DEFAULT_SPRING_CONFIG;
        }

        final ClassPathXmlApplicationContext  context = new ClassPathXmlApplicationContext(configPath);
        context.start();
        IceServer server =  context.getBean(IceServer.class);
        if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    server.stop();
                    context.stop();
                    context.close();
                    synchronized (Main.class) {
                        running = false;
                        Main.class.notify();
                    }
                }
            });
        }

        synchronized (Main.class) {
            while (running) {
                try {
                    Main.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }
}
