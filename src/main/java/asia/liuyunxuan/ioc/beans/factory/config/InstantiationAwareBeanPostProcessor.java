package asia.liuyunxuan.ioc.beans.factory.config;


import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {


    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;


    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;


    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;
}
