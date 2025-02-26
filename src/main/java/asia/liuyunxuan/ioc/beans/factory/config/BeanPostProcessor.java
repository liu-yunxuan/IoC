package asia.liuyunxuan.ioc.beans.factory.config;

import asia.liuyunxuan.ioc.beans.BeansException;

public interface BeanPostProcessor {


    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;


    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
