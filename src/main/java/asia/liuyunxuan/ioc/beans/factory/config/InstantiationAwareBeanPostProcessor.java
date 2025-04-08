package asia.liuyunxuan.ioc.beans.factory.config;


import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValues;

/**
 * Bean实例化过程的扩展接口，提供了在Bean实例化前后以及属性设置阶段的干预机会。
 * 继承自BeanPostProcessor，在标准的Bean生命周期基础上提供了更细粒度的控制。
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在目标Bean实例化之前调用，给予了一个机会可以返回一个代理对象来替代目标Bean的实例化。
     * @param beanClass 目标Bean的类
     * @param beanName Bean的名称
     * @return 代理对象，如果返回null则继续正常的实例化流程
     * @throws BeansException 如果发生错误
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     * 在目标Bean实例化之后，属性设置之前调用。
     * @param bean 已实例化的Bean对象
     * @param beanName Bean的名称
     * @return 如果返回false，将跳过属性设置阶段
     * @throws BeansException 如果发生错误
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    /**
     * 在属性设置到目标Bean之前调用，可以对属性值进行修改。
     * @param pvs 属性值集合
     * @param bean 目标Bean
     * @param beanName Bean的名称
     * @return 修改后的属性值集合
     * @throws BeansException 如果发生错误
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    /**
     * 获取提前暴露的Bean引用，用于解决循环依赖。
     * @param bean 原始Bean实例
     * @param beanName Bean的名称
     * @return 可能被包装的Bean实例
     */
    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
