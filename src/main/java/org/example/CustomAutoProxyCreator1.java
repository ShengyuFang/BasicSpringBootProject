package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

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

        System.out.println("wrapIfNecessary1 " + beanName + " 代理前 hashcode" + bean.hashCode());
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
        bean = super.wrapIfNecessary(bean, beanName, cacheKey);
        System.out.println("wrapIfNecessary1 " + beanName + " 代理后 hashcode" + bean.hashCode());
        try {
            AdvisedSupport advised = AdvisedSupportUtil.getAdvisedSupport(bean);
            System.out.println("advised size " + advised.getAdvisors().length);
            MethodInterceptor methodInterceptor2 = invocation -> {
                System.out.println("Before method execution 12");
                Object result = invocation.proceed(); // 执行原始方法
                System.out.println("After method execution 12");
                return result;
            };
            advised.addAdvisor(0, buildAdvisors(beanName, new Object[]{methodInterceptor2})[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[0];
    }
}
