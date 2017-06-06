package com.shua.main;

import com.shua.server.BarrierServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by SHUA on 2017/5/11.
 */
public class ServiceStart {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        ((BarrierServer)ctx.getBean("barrierServer")).run();
    }
}
