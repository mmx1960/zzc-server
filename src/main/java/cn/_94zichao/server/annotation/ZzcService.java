package cn._94zichao.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })//注解用在接口�?
@Retention(RetentionPolicy.RUNTIME)//VM将在运行期也保留注释，因此可以�?�过反射机制读取注解的信�?
@Component
public @interface ZzcService {

	String value();
}
