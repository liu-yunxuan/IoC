package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.beans.BeansException;
import asia.liuyunxuan.ioc.beans.PropertyValue;
import asia.liuyunxuan.ioc.beans.PropertyValues;
import asia.liuyunxuan.ioc.beans.factory.*;
import asia.liuyunxuan.ioc.beans.factory.config.*;
import asia.liuyunxuan.ioc.utils.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Bean自动装配功能的核心实现类。
 * <p>
 * 该类负责Bean的完整生命周期管理，包括：
 * <ul>
 *     <li>Bean的实例化：通过构造函数或工厂方法创建Bean实例</li>
 *     <li>属性注入：填充Bean的属性值，处理依赖注入</li>
 *     <li>Bean的初始化：执行初始化方法，应用Bean后处理器</li>
 *     <li>循环依赖处理：通过提前暴露Bean的引用解决循环依赖问题</li>
 *     <li>销毁方法注册：管理Bean的销毁过程</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>createBean：Bean创建的入口方法</li>
 *     <li>doCreateBean：实际的Bean创建过程
 *         <ul>
 *             <li>createBeanInstance：实例化Bean</li>
 *             <li>populateBean：填充属性</li>
 *             <li>initializeBean：初始化Bean</li>
 *         </ul>
 *     </li>
 *     <li>处理Aware接口：注入容器功能</li>
 *     <li>应用BeanPostProcessor：允许自定义修改Bean</li>
 * </ol>
 * <p>
 * 循环依赖解决方案：
 * <ul>
 *     <li>提前暴露创建中的Bean引用</li>
 *     <li>使用三级缓存机制</li>
 *     <li>支持构造器循环依赖（限单例）</li>
 * </ul>
 *
 * @see BeanPostProcessor
 * @see InstantiationAwareBeanPostProcessor
 * @see DisposableBean
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy;

    public AbstractAutowireCapableBeanFactory() {
        this.instantiationStrategy = InstantiationStrategyFactory.getStrategy("jdk");
    }

    /**
     * 创建Bean实例的入口方法。
     * <p>
     * 该方法首先尝试通过BeanPostProcessor创建代理对象，如果不能创建代理对象，
     * 则调用doCreateBean方法执行常规的Bean创建流程。
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义信息
     * @param args 构造函数参数
     * @return 创建的Bean实例
     * @throws BeansException Bean创建过程中的异常
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // 判断是否返回代理 Bean 对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (null != bean) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    /**
     * 执行Bean创建的核心流程。
     * <p>
     * 主要步骤包括：
     * <ol>
     *     <li>创建Bean实例</li>
     *     <li>处理循环依赖</li>
     *     <li>属性填充</li>
     *     <li>执行初始化方法</li>
     *     <li>注册销毁方法</li>
     * </ol>
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义信息
     * @param args 构造函数参数
     * @return 完全初始化的Bean实例
     * @throws BeansException Bean创建过程中的异常
     */
    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean;
        try {
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

            // 实例化后判断
            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }
            // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 判断 SCOPE_SINGLETON、SCOPE_PROTOTYPE
        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
            // 获取代理对象
            exposedObject = getSingleton(beanName);
            registerSingleton(beanName, exposedObject);
        }
        return exposedObject;
    }

    /**
     * 获取早期的Bean引用，用于处理循环依赖。
     * <p>
     * 允许BeanPostProcessor在Bean初始化完成之前修改Bean的引用，
     * 主要用于AOP代理等场景。
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义信息
     * @param bean 原始的Bean实例
     * @return 可能被修改的Bean引用
     */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if (null == exposedObject) return null;
            }
        }

        return exposedObject;
    }

    /**
     * 在Bean实例化后执行后处理器的处理。
     * <p>
     * 允许InstantiationAwareBeanPostProcessor在属性填充之前修改Bean或终止后续的处理过程。
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @return 如果应该继续进行属性填充则返回true，否则返回false
     */
    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    /**
     * 在属性填充之前应用后处理器。
     * <p>
     * 主要用于处理注解驱动的依赖注入，例如@Autowired、@Value等注解的解析。
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (null != pvs) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }
    /**
     * 实例化前的解析，给BeanPostProcessors一个机会返回代理对象来替代目标Bean。
     * <p>
     * 这个方法主要用于实现AOP等功能，允许在Bean实例化之前返回一个代理对象。
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义信息
     * @return 如果创建了代理对象则返回，否则返回null
     */
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (null != bean) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    /**
     * 应用InstantiationAwareBeanPostProcessor的前置处理。
     * <p>
     * 在Bean实例化之前调用，可以返回一个代理对象来替代原始的Bean实例。
     *
     * @param beanClass Bean的Class对象
     * @param beanName Bean的名称
     * @return 如果创建了代理对象则返回，否则返回null
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (null != result) return result;
            }
        }
        return null;
    }

    /**
     * 注册Bean的销毁方法。
     * <p>
     * 如果Bean实现了DisposableBean接口或配置了自定义的销毁方法，
     * 则将其包装为DisposableBeanAdapter并注册到容器中。
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 非 Singleton 类型的 Bean 不执行销毁方法
        if (!beanDefinition.isSingleton()) return;

        if (bean instanceof DisposableBean || (beanDefinition.getDestroyMethodName() != null && !beanDefinition.getDestroyMethodName().isEmpty())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    /**
     * 创建Bean实例。
     * <p>
     * 根据提供的构造参数选择合适的构造函数创建Bean实例。
     * 如果没有提供构造参数，则使用默认构造函数。
     *
     * @param beanDefinition Bean的定义信息
     * @param beanName Bean的名称
     * @param args 构造函数参数
     * @return 创建的Bean实例
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> ctor : declaredConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    /**
     * Bean属性填充。
     * <p>
     * 将BeanDefinition中定义的属性值注入到Bean实例中。
     * 如果属性值是BeanReference类型，则会先获取依赖的Bean实例。
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     * @throws BeansException 属性注入过程中的异常
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {

                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    // A 依赖 B，获取 B 的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                // 属性填充
                BeanUtils.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * 初始化Bean。
     * <p>
     * 执行Bean的初始化流程，包括：
     * <ol>
     *     <li>处理Aware接口回调</li>
     *     <li>执行BeanPostProcessor的前置处理</li>
     *     <li>调用初始化方法</li>
     *     <li>执行BeanPostProcessor的后置处理</li>
     * </ol>
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     * @return 初始化后的Bean实例
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }

        // 1. 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行 Bean 对象的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

        // 2. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    /**
     * 调用Bean的初始化方法。
     * <p>
     * 按以下顺序执行初始化：
     * <ol>
     *     <li>如果实现了InitializingBean接口，调用afterPropertiesSet方法</li>
     *     <li>如果配置了自定义初始化方法，则调用该方法</li>
     * </ol>
     *
     * @param beanName Bean的名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     * @throws Exception 初始化过程中的异常
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1. 实现接口 InitializingBean
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 2. 注解配置 init-method {判断是为了避免二次执行销毁}
        String initMethodName = beanDefinition.getInitMethodName();
        if (initMethodName != null && !initMethodName.isEmpty()) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            initMethod.invoke(bean);
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

}
