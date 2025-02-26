package asia.liuyunxuan.ioc.context.event;


import asia.liuyunxuan.ioc.beans.factory.BeanFactory;
import asia.liuyunxuan.ioc.context.ApplicationEvent;
import asia.liuyunxuan.ioc.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        for (final ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }

}
