package asia.liuyunxuan.ioc.component.container.config;

import asia.liuyunxuan.ioc.component.ComponentException;

/**
 * Bean后处理器接口，允许自定义修改新实例化的Bean对象。
 * <p>
 * 该接口作为Spring IoC容器的重要扩展点，主要用途包括：
 * <ul>
 *     <li>在Bean初始化前后对Bean实例进行自定义修改</li>
 *     <li>处理Bean中的标记接口或注解</li>
 *     <li>包装Bean对象，例如创建AOP代理</li>
 *     <li>执行自定义的初始化或销毁逻辑</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>Bean实例化完成</li>
 *     <li>属性注入完成</li>
 *     <li>执行postProcessBeforeInitialization方法</li>
 *     <li>执行Bean的初始化方法</li>
 *     <li>执行postProcessAfterInitialization方法</li>
 * </ol>
 * <p>
 * 注意：BeanPostProcessor对象会在Spring容器启动时自动检测并注册，
 * 它们会按照定义的顺序应用到所有符合条件的Bean实例上。
 *
 * @see ComponentException
 * @see InstantiationAwareBeanPostProcessor
 */
public interface BeanPostProcessor {

    /**
     * 在Bean执行初始化方法之前执行的后处理操作
     * <p>
     * 这个方法的典型用途包括：
     * <ul>
     *     <li>处理Bean中的注解</li>
     *     <li>设置资源引用</li>
     *     <li>设置Bean的特定属性值</li>
     * </ul>
     *
     * @param bean 新实例化的Bean对象
     * @param beanName Bean的名称
     * @return 处理后的Bean对象（如果返回null，将终止后续的处理器调用）
     * @throws ComponentException 处理过程中发生异常时抛出
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws ComponentException;

    /**
     * 在Bean执行初始化方法之后执行的后处理操作
     * <p>
     * 这个方法的典型用途包括：
     * <ul>
     *     <li>包装Bean，创建代理对象</li>
     *     <li>处理Bean中的标记接口</li>
     *     <li>执行自定义的初始化逻辑</li>
     * </ul>
     *
     * @param bean 完成初始化的Bean对象
     * @param beanName Bean的名称
     * @return 处理后的Bean对象（如果返回null，将终止后续的处理器调用）
     * @throws ComponentException 处理过程中发生异常时抛出
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws ComponentException;

}
