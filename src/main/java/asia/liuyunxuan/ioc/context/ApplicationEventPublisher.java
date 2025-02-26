package asia.liuyunxuan.ioc.context;


public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
