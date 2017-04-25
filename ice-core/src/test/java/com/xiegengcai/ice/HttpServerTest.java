package com.xiegengcai.ice;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class HttpServerTest {

    static {
        // 真实使用时可以 -Dice.spring.config=conf/http-server.xml -Dice.shutdown.hook=true 指定
        System.setProperty("ice.spring.config","conf/http-server.xml");
        System.setProperty("ice.shutdown.hook","true");
    }
    public static void main(String[] args) {
        Main.main(args);
    }
}
