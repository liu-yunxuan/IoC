package asia.liuyunxuan.ioc.beans.factory.config;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
