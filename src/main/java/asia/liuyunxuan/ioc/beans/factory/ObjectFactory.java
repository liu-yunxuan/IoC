package asia.liuyunxuan.ioc.beans.factory;


import asia.liuyunxuan.ioc.beans.BeansException;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
