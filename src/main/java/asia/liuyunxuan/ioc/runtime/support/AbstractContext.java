package asia.liuyunxuan.ioc.runtime.support;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.container.ConfigurableRegistry;
import asia.liuyunxuan.ioc.component.container.config.ComponentProviderPostProcessor;
import asia.liuyunxuan.ioc.component.container.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.runtime.Message;
import asia.liuyunxuan.ioc.runtime.MessageSubscriber;
import asia.liuyunxuan.ioc.runtime.ConfigurableContext;
import asia.liuyunxuan.ioc.runtime.message.MessageMulticaster;
import asia.liuyunxuan.ioc.runtime.message.ContextClosedEvent;
import asia.liuyunxuan.ioc.runtime.message.ContextRefreshedEvent;
import asia.liuyunxuan.ioc.runtime.message.SimpleMessageMulticaster;
import asia.liuyunxuan.ioc.kernel.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * ApplicationContext接口的抽象实现类，为具体的应用上下文实现提供基础模板。
 * <p>
 * 该类提供了Spring应用上下文的核心功能实现，包括：
 * <ul>
 *     <li>BeanFactory的初始化和配置</li>
 *     <li>Bean后处理器的注册和管理</li>
 *     <li>应用事件的发布和监听机制</li>
 *     <li>资源加载和管理</li>
 *     <li>生命周期管理（包括启动和关闭）</li>
 * </ul>
 * <p>
 * 子类主要需要实现{@link #refreshBeanFactory()}和{@link #getBeanFactory()}方法
 * 来提供具体的Bean工厂实现。
 * @author liuyunxuan
 */
public abstract class AbstractContext extends DefaultResourceLoader implements ConfigurableContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "messageMulticaster";

    private MessageMulticaster messageMulticaster;

    /**
     * 刷新整个应用上下文，这是Spring容器启动的核心方法。
     * <p>
     * 该方法按顺序执行以下操作：
     * <ol>
     *     <li>创建和准备Bean工厂</li>
     *     <li>注册和调用Bean工厂后处理器</li>
     *     <li>注册Bean后处理器</li>
     *     <li>初始化事件广播器</li>
     *     <li>注册事件监听器</li>
     *     <li>初始化所有单例Bean</li>
     *     <li>完成刷新过程并发布事件</li>
     * </ol>
     * @throws ComponentException 如果刷新过程中发生错误
     */
    @Override
    public void refresh() throws ComponentException {
        // 1. 创建 ComponentProvider，并加载 ComponentDefinition
        refreshBeanFactory();

        // 2. 获取 ComponentProvider
        ConfigurableRegistry beanFactory = getBeanFactory();

        // 3. 添加 RuntimeContextAwareProcessor，让继承自 RuntimeContextAware 的 Bean 对象都能感知所属的 RuntimeContext
        beanFactory.addBeanPostProcessor(new RuntimeContextAwareProcessor(this));

        // 4. 在 Bean 实例化之前，执行 ComponentProviderPostProcessor (Invoke container processors registered as component in the runtime.)
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);

        // 6. 初始化事件发布者
        initApplicationEventMulticaster();

        // 7. 注册事件监听器
        registerListeners();

        // 8. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 9. 发布容器刷新完成事件
        finishRefresh();
    }

    /**
     * 刷新Bean工厂，由子类实现具体的刷新逻辑。
     * <p>
     * 该方法负责创建或刷新内部的Bean工厂，加载Bean定义等操作。
     * 具体实现取决于应用上下文的类型（如XML配置、注解配置等）。
     * @throws ComponentException 如果刷新过程中发生错误
     */
    protected abstract void refreshBeanFactory() throws ComponentException;

    /**
     * 获取此上下文使用的内部Bean工厂。
     * <p>
     * 该方法由子类实现，返回用于Bean管理的具体工厂实例。
     * @return 可配置的Bean工厂实例
     */
    protected abstract ConfigurableRegistry getBeanFactory();

    /**
     * 调用所有注册的BeanFactoryPostProcessor。
     * <p>
     * 在Bean实例化之前，允许修改Bean定义的属性值和其他元数据。
     * @param beanFactory 要使用的Bean工厂
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableRegistry beanFactory) {
        Map<String, ComponentProviderPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(ComponentProviderPostProcessor.class);
        for (ComponentProviderPostProcessor componentProviderPostProcessor : beanFactoryPostProcessorMap.values()) {
            componentProviderPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 注册所有的BeanPostProcessor。
     * <p>
     * 这些处理器将应用于随后创建的Bean实例。
     * @param beanFactory 要使用的Bean工厂
     */
    private void registerBeanPostProcessors(ConfigurableRegistry beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 初始化ApplicationEventMulticaster。
     * <p>
     * 如果上下文中没有定义，则使用SimpleApplicationEventMulticaster。
     */
    private void initApplicationEventMulticaster() {
        ConfigurableRegistry beanFactory = getBeanFactory();
        messageMulticaster = new SimpleMessageMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, messageMulticaster);
    }

    /**
     * 注册所有的ApplicationListener。
     * <p>
     * 将所有实现ApplicationListener接口的Bean注册为事件监听器。
     */
    @SuppressWarnings("rawtypes")
    private void registerListeners() {
        Collection<MessageSubscriber> messageSubscribers = getBeansOfType(MessageSubscriber.class).values();
        for (MessageSubscriber listener : messageSubscribers) {
            messageMulticaster.addApplicationListener(listener);
        }
    }

    /**
     * 完成刷新过程。
     * <p>
     * 发布ContextRefreshedEvent事件，标志着上下文刷新完成。
     */
    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(Message event) {
        messageMulticaster.multicastEvent(event);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws ComponentException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws ComponentException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws ComponentException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws ComponentException {
        return getBeanFactory().getBean(name, requiredType);
    }
    @Override
    public <T> T getBean(Class<T> requiredType) throws ComponentException {
        return getBeanFactory().getBean(requiredType);
    }
    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * 关闭此应用上下文。
     * <p>
     * 发布上下文关闭事件，并销毁所有单例Bean。
     */
    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // 执行销毁单例bean的销毁方法
        getBeanFactory().destroySingletons();
    }

}
