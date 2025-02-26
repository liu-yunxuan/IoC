package asia.liuyunxuan.ioc.context;

import asia.liuyunxuan.ioc.beans.factory.HierarchicalBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.ListableBeanFactory;
import asia.liuyunxuan.ioc.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher{
}
