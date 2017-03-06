package com.qianshanding;

import com.qianshanding.jetty.server.EmbeddedJettyServer;

/**
 * Created by zhengyu on 2017/3/6.
 */
public class Launcher {
    public static void main(String[] args) throws Exception {
        EmbeddedJettyServer jettyServer = new EmbeddedJettyServer();
        jettyServer.setPort(8092);
        jettyServer.setContextPath("/");
        jettyServer.start();
    }
}
