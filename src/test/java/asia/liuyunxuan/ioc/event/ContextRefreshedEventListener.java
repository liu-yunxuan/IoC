package asia.liuyunxuan.ioc.event;

import asia.liuyunxuan.ioc.runtime.MessageSubscriber;
import asia.liuyunxuan.ioc.runtime.message.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements MessageSubscriber<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件：" + this.getClass().getName());
    }

}
