package org.example;

import org.springframework.stereotype.Component;

@Component
public class MyBean implements MyBeanInterface {
    public int i;
    public MyBean() {
        System.out.println("MyBean init");
    }
    public void print() {
        System.out.println("i = " + i);
    }
}
