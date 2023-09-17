package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

@Component
public class CustomAutoProxyCreator2 extends AbstractAutoProxyCreator {
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        if(beanName.equals("myBean")) {
            /**
             * 会把methodInterceptor这个advice添加到advised中，与后面advised.addAdvisor()重复
             * ProxyFactory proxyFactory = new ProxyFactory();
             * proxyFactory.setTarget(bean);
             * proxyFactory.addAdvice(methodInterceptor);
             * bean = proxyFactory.getProxy();
             **/
            try {
                System.out.println("wrapIfNecessary2 " + beanName + " 代理前 hashcode" + bean.hashCode());
                /**
                 * 也就是被CustomAutoProxyCreator1代理过的对象的advised
                 * 删除CustomAutoProxyCreator1中bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                 * 后也就是被CustomAutoProxyCreator1代理过的对象的advised，返回大小为2
                 * 删除前是1
                 */
                AdvisedSupport advised = AdvisedSupportUtil.getAdvisedSupport(bean);
                System.out.println("advised size " + advised.getAdvisors().length);
                MethodInterceptor methodInterceptor = invocation -> {
                    System.out.println("Before method execution 2");
                    Object result = invocation.proceed(); // 执行原始方法
                    System.out.println("After method execution 2");
                    return result;
                };
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
            bean = super.wrapIfNecessary(bean, beanName, cacheKey);
            System.out.println("wrapIfNecessary2 " + beanName + " 代理后 hashcode" + bean.hashCode());
            AdvisedSupport advised = null;
            try {
                // 产生新的代理类，所以advised大小是0
                advised = AdvisedSupportUtil.getAdvisedSupport(bean);
                System.out.println("advised size " + advised.getAdvisors().length);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return bean;
        }
        return bean;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[0];
    }

}
