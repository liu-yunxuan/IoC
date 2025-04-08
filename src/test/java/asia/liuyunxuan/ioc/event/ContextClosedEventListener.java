package asia.liuyunxuan.ioc.event;

import asia.liuyunxuan.ioc.runtime.MessageSubscriber;
import asia.liuyunxuan.ioc.runtime.message.ContextClosedEvent;

public class ContextClosedEventListener implements MessageSubscriber<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }

}
