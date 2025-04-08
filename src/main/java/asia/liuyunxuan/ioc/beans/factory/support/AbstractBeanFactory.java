package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.factory.FactoryBean;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.config.BeanPostProcessor;
import asia.liuyunxuan.ioc.beans.factory.config.ConfigurableBeanFactory;
import asia.liuyunxuan.ioc.utils.ClassUtils;
import asia.liuyunxuan.ioc.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * IoC容器的核心抽象类，实现了Bean的获取、创建和管理的基本逻辑。
 * 
 * <p>该类提供了Bean生命周期管理的骨架实现，包括：
 * <ul>
 *     <li>Bean的获取和创建</li>
 *     <li>FactoryBean的处理</li>
 *     <li>Bean后处理器的注册和应用</li>
 *     <li>嵌入值解析器的管理</li>
 * </ul>
 * 
 * <p>子类需要实现以下核心方法：
 * <ul>
 *     <li>{@link #getBeanDefinition(String)} - 获取Bean定义</li>
 *     <li>{@link #createBean(String, BeanDefinition, Object[])} - 创建Bean实例</li>
 * </ul>
 *
 * <p>该类通过模板方法模式定义了Bean的创建过程，同时提供了可扩展的后处理器机制，
 * 允许在Bean实例化前后进行自定义处理。
 *
 * @see FactoryBeanRegistrySupport
 * @see ConfigurableBeanFactory
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /** 
     * 用于解析Bean类名的类加载器
     */
    private final ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /** 
     * Bean后处理器列表，用于在Bean创建过程中的前置和后置处理
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /** 
     * 嵌入值解析器列表，用于解析配置文件中的占位符
     */
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    /**
     * 获取指定名称的Bean实例。
     *
     * @param name Bean的名称
     * @return Bean实例
     * @throws BeansException 如果无法创建Bean
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    /**
     * 获取指定名称的Bean实例，支持传入构造参数。
     *
     * @param name Bean的名称
     * @param args 构造参数
     * @return Bean实例
     * @throws BeansException 如果无法创建Bean
     */
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    /**
     * 获取指定名称和类型的Bean实例。
     *
     * @param name Bean的名称
     * @param requiredType 期望的Bean类型
     * @return 指定类型的Bean实例
     * @throws BeansException 如果无法创建Bean或类型不匹配
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    /**
     * 获取Bean的实际实现方法。处理FactoryBean，并支持单例缓存。
     *
     * @param <T> Bean 的类型
     * @param name Bean的名称
     * @param args 构造参数
     * @return Bean实例
     */
    @SuppressWarnings("unchecked")
    protected <T> T doGetBean(final String name, final Object[] args) {
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            // 如果是 FactoryBean，则需要调用 FactoryBean#getObject
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean, name);
    }

    /**
     * 处理Bean实例，如果是FactoryBean，则获取其创建的对象。
     *
     * @param beanInstance Bean实例
     * @param beanName Bean的名称
     * @return 最终的Bean对象
     */
    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = getCachedObjectForFactoryBean(beanName);

        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }

        return object;
    }

    /**
     * 添加嵌入值解析器，用于解析配置文件中的占位符。
     *
     * @param valueResolver 值解析器
     */
    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    /**
     * 解析包含占位符的值。
     *
     * @param value 待解析的值
     * @return 解析后的值
     */
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    /**
     * 获取Bean的定义信息。
     *
     * @param beanName Bean的名称
     * @return Bean的定义信息
     * @throws BeansException 如果找不到Bean定义
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 创建Bean实例。
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义信息
     * @param args 构造参数
     * @return 创建的Bean实例
     * @throws BeansException 如果Bean创建失败
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    /**
     * 添加Bean后处理器。
     * 如果该处理器已存在，则先移除再添加，确保处理器按照添加顺序执行。
     *
     * @param beanPostProcessor Bean后处理器
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 获取所有注册的Bean后处理器。
     * 这些处理器将应用于该工厂创建的所有Bean。
     *
     * @return Bean后处理器列表
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    /**
     * 获取Bean类加载器。
     *
     * @return 用于加载Bean类的类加载器
     */
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
