package asia.liuyunxuan.ioc.beans.factory;

import asia.liuyunxuan.ioc.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
}
