package org.example;

import org.springframework.stereotype.Component;

@Component("MyBean2")
public class MyBean2 implements MyBeanInterface {
    public int i;
    public MyBean2() {
        System.out.println("MyBean2 init");
    }
    public void print() {
        System.out.println("i = " + i);
    }
}
