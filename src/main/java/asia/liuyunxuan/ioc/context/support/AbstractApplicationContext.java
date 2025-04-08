package asia.liuyunxuan.ioc.context.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.ConfigurableListableBeanFactory;
import asia.liuyunxuan.ioc.beans.factory.config.BeanFactoryPostProcessor;
import asia.liuyunxuan.ioc.beans.factory.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.context.ApplicationEvent;
import asia.liuyunxuan.ioc.context.ApplicationListener;
import asia.liuyunxuan.ioc.context.ConfigurableApplicationContext;
import asia.liuyunxuan.ioc.context.event.ApplicationEventMulticaster;
import asia.liuyunxuan.ioc.context.event.ContextClosedEvent;
import asia.liuyunxuan.ioc.context.event.ContextRefreshedEvent;
import asia.liuyunxuan.ioc.context.event.SimpleApplicationEventMulticaster;
import asia.liuyunxuan.ioc.core.io.DefaultResourceLoader;

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
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

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
     * @throws BeansException 如果刷新过程中发生错误
     */
    @Override
    public void refresh() throws BeansException {
        // 1. 创建 BeanFactory，并加载 BeanDefinition
        refreshBeanFactory();

        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke factory processors registered as beans in the context.)
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
     * @throws BeansException 如果刷新过程中发生错误
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    /**
     * 获取此上下文使用的内部Bean工厂。
     * <p>
     * 该方法由子类实现，返回用于Bean管理的具体工厂实例。
     * @return 可配置的Bean工厂实例
     */
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 调用所有注册的BeanFactoryPostProcessor。
     * <p>
     * 在Bean实例化之前，允许修改Bean定义的属性值和其他元数据。
     * @param beanFactory 要使用的Bean工厂
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 注册所有的BeanPostProcessor。
     * <p>
     * 这些处理器将应用于随后创建的Bean实例。
     * @param beanFactory 要使用的Bean工厂
     */
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
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
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册所有的ApplicationListener。
     * <p>
     * 将所有实现ApplicationListener接口的Bean注册为事件监听器。
     */
    @SuppressWarnings("rawtypes")
    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
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
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
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
