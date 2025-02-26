package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.DisposableBean;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName() != null ? beanDefinition.getDestroyMethodName() : "";
    }

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

