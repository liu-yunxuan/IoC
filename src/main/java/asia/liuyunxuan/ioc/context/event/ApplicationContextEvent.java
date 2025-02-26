package asia.liuyunxuan.ioc.context.event;

import asia.liuyunxuan.ioc.context.ApplicationContext;
import asia.liuyunxuan.ioc.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
