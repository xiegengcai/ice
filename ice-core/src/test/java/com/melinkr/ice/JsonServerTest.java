package com.melinkr.ice;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class JsonServerTest {
    static {
        // 真实使用时可以 -Dice.spring.config=conf/json-server.xml -Dice.shutdown.hook=true 指定
        System.setProperty("ice.spring.config","conf/json-server.xml");
        System.setProperty("ice.shutdown.hook","true");
    }
    public static void main(String[] args) {
        Main.main(args);
    }
}
