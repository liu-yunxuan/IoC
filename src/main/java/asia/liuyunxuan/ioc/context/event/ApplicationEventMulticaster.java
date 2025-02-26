package asia.liuyunxuan.ioc.context.event;


import asia.liuyunxuan.ioc.context.ApplicationEvent;
import asia.liuyunxuan.ioc.context.ApplicationListener;

public interface ApplicationEventMulticaster {


    void addApplicationListener(ApplicationListener<?> listener);


    void removeApplicationListener(ApplicationListener<?> listener);


    void multicastEvent(ApplicationEvent event);

}
