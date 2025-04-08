package asia.liuyunxuan.ioc.runtime.support;


import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;
import asia.liuyunxuan.ioc.component.container.support.DefaultRegistry;

/**
 * 可刷新的ApplicationContext抽象实现类。
 * <p>
 * 该类提供了创建和刷新Spring容器内部Bean工厂的基本实现：
 * <ul>
 *     <li>支持多次刷新，每次刷新都会创建新的Bean工厂</li>
 *     <li>使用DefaultListableBeanFactory作为默认的Bean工厂实现</li>
 *     <li>提供了加载Bean定义的抽象模板方法</li>
 * </ul>
 * <p>
 * 子类主要需要实现{@link #loadBeanDefinitions(DefaultRegistry)}方法
 * 来提供具体的Bean定义加载机制（如XML、注解等）。
 * @author liuyunxuan
 */
public abstract class AbstractRefreshableContext extends AbstractContext {

    private DefaultRegistry beanFactory;

    /**
     * 实现父类的抽象方法，执行Bean工厂的刷新。
     * <p>
     * 该方法会创建新的Bean工厂实例，并加载Bean定义。
     * @throws ComponentException 如果刷新过程中发生错误
     */
    @Override
    protected void refreshBeanFactory() throws ComponentException {
        DefaultRegistry beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    /**
     * 创建新的DefaultListableBeanFactory实例。
     * <p>
     * 每次刷新容器时都会调用此方法创建新的工厂实例。
     * @return 新创建的Bean工厂实例
     */
    private DefaultRegistry createBeanFactory() {
        return new DefaultRegistry();
    }

    /**
     * 加载Bean定义到指定的Bean工厂中。
     * <p>
     * 这是一个模板方法，由具体的子类实现，用于根据不同的配置源（如XML、注解等）
     * 加载Bean定义。
     * @param beanFactory 要加载Bean定义的目标工厂
     * @throws ComponentException 如果加载过程中发生错误
     */
    protected abstract void loadBeanDefinitions(DefaultRegistry beanFactory);

    /**
     * 获取此上下文使用的内部Bean工厂。
     * <p>
     * 实现父类的抽象方法，返回当前正在使用的Bean工厂实例。
     * @return 当前的Bean工厂实例
     */
    @Override
    public ConfigurableRegistry getBeanFactory() {
        return beanFactory;
    }

}
