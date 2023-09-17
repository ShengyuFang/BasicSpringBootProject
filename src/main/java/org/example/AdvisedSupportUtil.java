package org.example;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 获取这个对象代理类的所有advised
 * @param proxy
 * @return
 * @throws Exception
 */
public class AdvisedSupportUtil {
    public static AdvisedSupport getAdvisedSupport(Object proxy) throws Exception {
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
