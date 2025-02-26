package asia.liuyunxuan.ioc.beans.factory;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.config.AutowireCapableBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}