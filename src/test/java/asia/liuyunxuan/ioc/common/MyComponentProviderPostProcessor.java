package asia.liuyunxuan.ioc.common;


import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.PropertyValue;
import asia.liuyunxuan.ioc.component.PropertyValues;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;
import asia.liuyunxuan.ioc.component.container.config.ComponentDefinition;
import asia.liuyunxuan.ioc.component.container.config.ComponentProviderPostProcessor;

public class MyComponentProviderPostProcessor implements ComponentProviderPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableRegistry beanFactory) throws ComponentException {

        ComponentDefinition componentDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = componentDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动"));
    }

}
