package asia.liuyunxuan.ioc.component.container.config;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.ComponentProvider;

/**
 * 提供Bean自动装配功能的工厂接口，扩展了BeanFactory的基础功能。
 * <p>
 * 该接口主要提供以下功能：
 * <ul>
 *     <li>自动装配Bean之间的依赖关系</li>
 *     <li>在Bean初始化前后应用后处理器</li>
 *     <li>处理Bean的生命周期回调</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>创建Bean实例</li>
 *     <li>注入Bean的依赖</li>
 *     <li>应用Bean的初始化前处理器</li>
 *     <li>执行Bean的初始化方法</li>
 *     <li>应用Bean的初始化后处理器</li>
 * </ol>
 *
 * @see ComponentProvider
 * @see ComponentException
 * @see BeanPostProcessor
 */
public interface AutowireCapableComponentProvider extends ComponentProvider {

    /**
     * 在Bean初始化之前应用BeanPostProcessor的前置处理
     * <p>
     * 这个方法会按照注册顺序执行所有BeanPostProcessor的postProcessBeforeInitialization方法。
     * 如果任何后处理器返回null，将停止后续处理器的调用。
     *
     * @param existingBean 需要处理的Bean实例
     * @param beanName Bean的名称
     * @return 处理后的Bean实例
     * @throws ComponentException 处理过程中发生异常时抛出
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws ComponentException;

    /**
     * 在Bean初始化之后应用BeanPostProcessor的后置处理
     * <p>
     * 这个方法会按照注册顺序执行所有BeanPostProcessor的postProcessAfterInitialization方法。
     * 如果任何后处理器返回null，将停止后续处理器的调用。
     *
     * @param existingBean 需要处理的Bean实例
     * @param beanName Bean的名称
     * @return 处理后的Bean实例
     * @throws ComponentException 处理过程中发生异常时抛出
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws ComponentException;

}


