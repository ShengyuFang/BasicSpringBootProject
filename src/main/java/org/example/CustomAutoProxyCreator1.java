package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;

@Component
public class CustomAutoProxyCreator1 extends AbstractAutoProxyCreator {
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        Class<?> clazz = bean.getClass();
        if (!Modifier.isPublic(clazz.getModifiers()) || bean.getClass().getInterfaces().length == 0) {
            // 类不可代理，返回原始 bean
            return bean;
        }
        if (!(bean instanceof MyBeanInterface)) {
            return bean;
        }
        System.out.println("CustomAutoProxyCreator1 wrapIfNecessary " + beanName);
        // 在这里实现自定义的代理创建逻辑
        // 创建 ProxyFactory，通过ProxyFactory增强方法
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        // 添加 MethodInterceptor，实现方法增强逻辑
        MethodInterceptor methodInterceptor = invocation -> {
            System.out.println("Before method execution 1");
            Object result = invocation.proceed(); // 执行原始方法
            System.out.println("After method execution 1");
            return result;
        };
        proxyFactory.addAdvice(methodInterceptor);
        buildAdvisors(beanName, new Object[]{methodInterceptor});
        bean = proxyFactory.getProxy();
        return super.wrapIfNecessary(bean, beanName, cacheKey);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[0];
    }
}
