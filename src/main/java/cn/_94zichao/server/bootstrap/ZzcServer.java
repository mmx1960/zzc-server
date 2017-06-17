package cn._94zichao.server.bootstrap;

import cn._94zichao.server.annotation.ZzcService;
import cn._94zichao.server.decoder.EndBasedDecoder;
import cn._94zichao.server.encoder.ToModelEncoder;
import cn._94zichao.server.handler.BarrierServerHandler;
import cn._94zichao.server.tcpServer.BaseServer;
import cn._94zichao.server.util.Content;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/17 0017.
 */
public class ZzcServer implements ApplicationContextAware {
    public static Map<Object,Method[]> methodsMap;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> map =applicationContext.getBeansWithAnnotation(ZzcService.class);
        for (Object serviceBean : map.values()) {
            try {
                //获取自定义注解上的value
                String value = serviceBean.getClass().getAnnotation(ZzcService.class).value();
                System.out.println("注解上的value: " + value);
                //拿到类下面的所有方法
                Method[] methods = serviceBean.getClass().getDeclaredMethods();
                methodsMap = new HashMap();
                methodsMap.put(serviceBean,methods);
                //启动服务器
                BaseServer.create().port(9999).decoder(new EndBasedDecoder(Content.END,true)).encoder(new ToModelEncoder()).in(new BarrierServerHandler()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    }
}
