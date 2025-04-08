package asia.liuyunxuan.ioc.runtime.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.runtime.RuntimeContext;
import asia.liuyunxuan.ioc.runtime.RuntimeContextAware;

/**
 * ApplicationContextAware接口的处理器实现类。
 * <p>
 * 该处理器负责将ApplicationContext注入到实现了ApplicationContextAware接口的Bean中：
 * <ul>
 *     <li>在Bean初始化之前检查Bean是否实现了ApplicationContextAware接口</li>
 *     <li>如果实现了该接口，则将ApplicationContext注入到Bean中</li>
 *     <li>使Bean能够访问ApplicationContext及其提供的所有功能</li>
 * </ul>
 * @author liuyunxuan
 */
public class RuntimeContextAwareProcessor implements BeanPostProcessor {

    private final RuntimeContext runtimeContext;

    /**
     * 创建一个新的ApplicationContextAwareProcessor实例。
     *
     * @param runtimeContext 要注入到Aware Bean中的ApplicationContext实例
     */
    public RuntimeContextAwareProcessor(RuntimeContext runtimeContext) {
        this.runtimeContext = runtimeContext;
    }

    /**
     * 在Bean初始化之前执行的处理。
     * <p>
     * 如果Bean实现了ApplicationContextAware接口，
     * 则调用其setApplicationContext方法注入ApplicationContext实例。
     * @param bean 要处理的Bean实例
     * @param beanName Bean的名称
     * @return 处理后的Bean实例
     * @throws ComponentException 如果处理过程中发生错误
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws ComponentException {
        if (bean instanceof RuntimeContextAware){
            ((RuntimeContextAware) bean).setApplicationContext(runtimeContext);
        }
        return bean;
    }

    /**
     * 在Bean初始化之后执行的处理。
     * <p>
     * 此实现直接返回Bean实例，不做任何处理。
     * @param bean 要处理的Bean实例
     * @param beanName Bean的名称
     * @return 处理后的Bean实例
     * @throws ComponentException 如果处理过程中发生错误
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws ComponentException {
        return bean;
    }

}
