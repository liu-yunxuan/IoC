package asia.liuyunxuan.ioc.beans.factory.config;


import asia.liuyunxuan.ioc.beans.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

}
