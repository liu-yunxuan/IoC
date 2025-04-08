package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.DisposableBean;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * Bean销毁适配器。
 * <p>
 * 该适配器实现了DisposableBean接口，用于统一管理Bean的销毁方法。
 * 它能够处理以下两种方式定义的Bean销毁方法：
 * <ul>
 * <li>实现DisposableBean接口</li>
 * <li>通过配置destroy-method属性</li>
 * </ul>
 */

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName() != null ? beanDefinition.getDestroyMethodName() : "";
    }

    /**
     * 执行Bean的销毁方法。
     * <p>
     * 该方法会按照以下顺序尝试执行Bean的销毁：
     * 1. 如果Bean实现了DisposableBean接口，则调用其destroy方法
     * 2. 如果配置了destroy-method，则通过反射调用指定的方法
     *
     * @throws Exception 当销毁过程中发生异常时抛出
     */
    @Override
    public void destroy() throws Exception {
        // 1. 实现接口 DisposableBean
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        // 2. 注解配置 destroy-method
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            // 避免重复销毁
            if (!(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
                try {
                    Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
                    destroyMethod.invoke(bean);
                } catch (NoSuchMethodException e) {
                    throw new BeansException("Could not find destroy method '" + destroyMethodName + "' on bean with name '" + beanName + "'", e);
                } catch (Exception e) {
                    throw new BeansException("Invocation of destroy method '" + destroyMethodName + "' on bean with name '" + beanName + "' failed", e);
                }
            }
        }
    }

}

