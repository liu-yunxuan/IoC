package asia.liuyunxuan.ioc.component.container.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.DisposableComponent;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;

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

public class DisposableComponentAdapter implements DisposableComponent {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableComponentAdapter(Object bean, String beanName, ComponentDefinition componentDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = componentDefinition.getDestroyMethodName() != null ? componentDefinition.getDestroyMethodName() : "";
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
        // 1. 实现接口 DisposableComponent
        if (bean instanceof DisposableComponent) {
            ((DisposableComponent) bean).destroy();
        }

        // 2. 注解配置 destroy-method
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            // 避免重复销毁
            if (!(bean instanceof DisposableComponent && "destroy".equals(this.destroyMethodName))) {
                try {
                    Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
                    destroyMethod.invoke(bean);
                } catch (NoSuchMethodException e) {
                    throw new ComponentException("Could not find destroy method '" + destroyMethodName + "' on bean with name '" + beanName + "'", e);
                } catch (Exception e) {
                    throw new ComponentException("Invocation of destroy method '" + destroyMethodName + "' on bean with name '" + beanName + "' failed", e);
                }
            }
        }
    }

}

