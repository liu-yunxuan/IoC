package asia.liuyunxuan.ioc.common;


import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValue;
import asia.liuyunxuan.ioc.beans.PropertyValues;
import asia.liuyunxuan.ioc.beans.factory.ConfigurableListableBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动"));
    }

}
