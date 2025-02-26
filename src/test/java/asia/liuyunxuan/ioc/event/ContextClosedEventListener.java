package asia.liuyunxuan.ioc.event;

import asia.liuyunxuan.ioc.context.ApplicationListener;
import asia.liuyunxuan.ioc.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }

}
