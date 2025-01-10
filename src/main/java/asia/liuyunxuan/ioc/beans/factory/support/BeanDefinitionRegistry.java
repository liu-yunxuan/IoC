package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

}
