package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
//        SpringApplication app = new SpringApplication(Main.class);
//        ConfigurableApplicationContext context = app.run(args); 会再次启动Spring


        //Mybean是单例
        //MyBean bean = context.getBean(MyBean.class); //会报not available
        MyBeanInterface bean = context.getBean(MyBeanInterface.class);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        bean.print();
    }
}

/**
 * 默认只扫描注解类所在的包，与@SpringBootApplication(scanBasePackages="org.example")作用相同
 * @SpringBootApplication
 * public class Main {
 *     public static void main(String[] args) {
 *         AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
 *         ctx.register(MyBean.class);
 *         ctx.refresh();
 *
 *         //Mybean是单例
 *         MyBean bean = ctx.getBean(MyBean.class);
 *         bean.print();
 *         bean.i += 1;
 *         bean = ctx.getBean(MyBean.class);
 *         bean.print();
 *         //测试AOP
 *
 *     }
 * }
 */