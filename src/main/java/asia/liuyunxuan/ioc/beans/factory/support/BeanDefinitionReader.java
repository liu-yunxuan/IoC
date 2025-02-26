package asia.liuyunxuan.ioc.beans.factory.support;


import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.core.io.Resource;
import asia.liuyunxuan.ioc.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;

}
