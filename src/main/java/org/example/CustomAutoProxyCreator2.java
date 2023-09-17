package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

@Component
public class CustomAutoProxyCreator2 extends AbstractAutoProxyCreator {
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        if(beanName.equals("myBean")) {
            System.out.println("CustomAutoProxyCreator2 wrapIfNecessary " + beanName);
            // 添加 MethodInterceptor，实现方法增强逻辑
            MethodInterceptor methodInterceptor = invocation -> {
                System.out.println("Before method execution 2");
                Object result = invocation.proceed(); // 执行原始方法
                System.out.println("After method execution 2");
                return result;
            };
            /**
             * 会把methodInterceptor这个advice添加到advised中，与后面advised.addAdvisor()重复
             * ProxyFactory proxyFactory = new ProxyFactory();
             * proxyFactory.setTarget(bean);
             * proxyFactory.addAdvice(methodInterceptor);
             * bean = proxyFactory.getProxy();
             **/
            try {
                AdvisedSupport advised = getAdvisedSupport(bean);
                MethodInterceptor methodInterceptor2 = invocation -> {
                    System.out.println("Before method execution 22");
                    Object result = invocation.proceed(); // 执行原始方法
                    System.out.println("After method execution 22");
                    return result;
                };
                advised.addAdvisor(0, buildAdvisors(beanName, new Object[]{methodInterceptor})[0]);
                advised.addAdvisor(1, buildAdvisors(beanName, new Object[]{methodInterceptor2})[0]);
//                advised.addAdvisor(0, buildAdvisors(beanName, new Object[]{methodInterceptor2})[0]);
//                advised.addAdvisor(1, buildAdvisors(beanName, new Object[]{methodInterceptor})[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return super.wrapIfNecessary(bean, beanName, cacheKey);
        }
        return bean;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[0];
    }

    /**
     * 获取这个类的所有拦截器
     * @param proxy
     * @return
     * @throws Exception
     */
    public AdvisedSupport getAdvisedSupport(Object proxy) throws Exception {
        Object dynamicAdvisedInterceptor;
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            dynamicAdvisedInterceptor = Proxy.getInvocationHandler(proxy);
        } else {
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            dynamicAdvisedInterceptor = h.get(proxy);
        }
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
    }

}
