package asia.liuyunxuan.ioc.context;

import asia.liuyunxuan.ioc.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();

}
