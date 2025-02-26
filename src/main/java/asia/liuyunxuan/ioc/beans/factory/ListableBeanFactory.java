package asia.liuyunxuan.ioc.beans.factory;

import asia.liuyunxuan.ioc.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {

    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    String[] getBeanDefinitionNames();

}
