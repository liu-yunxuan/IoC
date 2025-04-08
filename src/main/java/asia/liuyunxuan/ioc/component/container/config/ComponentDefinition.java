package asia.liuyunxuan.ioc.component.container.config;

import asia.liuyunxuan.ioc.component.PropertyValues;

/**
 * Bean定义类，用于描述Bean实例的配置元信息。
 * <p>
 * 该类持有Bean的配置信息，包括：
 * <ul>
 *     <li>Bean的Class类型</li>
 *     <li>Bean的作用域（单例/原型）</li>
 *     <li>Bean的属性值</li>
 *     <li>Bean的初始化方法</li>
 *     <li>Bean的销毁方法</li>
 * </ul>
 * <p>
 * BeanDefinition在Spring IoC容器中扮演着重要角色：
 * <ol>
 *     <li>它是容器实例化Bean的原材料，容器根据BeanDefinition中的信息创建Bean实例</li>
 *     <li>它提供了Bean的作用域控制，支持单例（Singleton）和原型（Prototype）两种作用域</li>
 *     <li>它允许配置Bean的生命周期回调方法，如初始化和销毁方法</li>
 *     <li>它支持属性注入，通过PropertyValues保存Bean的属性配置信息</li>
 * </ol>
 * 
 * @see ConfigurableProvider
 * @see PropertyValues
 */
public class ComponentDefinition {

    String SCOPE_SINGLETON = ConfigurableProvider.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableProvider.SCOPE_PROTOTYPE;

    private Class<?> beanClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    /**
     * 创建一个Bean定义实例
     * 
     * @param beanClass Bean的Class对象
     */
    public ComponentDefinition(Class<?> beanClass) {
        this(beanClass, null);
    }

    /**
     * 创建一个Bean定义实例，并指定属性值
     * 
     * @param beanClass Bean的Class对象
     * @param propertyValues Bean的属性值集合，如果为null则创建空的PropertyValues
     */
    public ComponentDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    /**
     * 设置Bean的作用域
     * 
     * @param scope 作用域标识符（"singleton"或"prototype"）
     */
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    /**
     * 判断Bean是否是单例作用域
     * 
     * @return 如果是单例返回true，否则返回false
     */
    public boolean isSingleton() {
        return singleton;
    }

    /**
     * 判断Bean是否是原型作用域
     * 
     * @return 如果是原型返回true，否则返回false
     */
    public boolean isPrototype() {
        return prototype;
    }

    /**
     * 获取Bean的Class对象
     * 
     * @return Bean的Class对象
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * 设置Bean的Class对象
     * 
     * @param beanClass Bean的Class对象
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 获取Bean的属性值集合
     * 
     * @return PropertyValues对象，包含Bean的所有属性值
     */
    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    /**
     * 设置Bean的属性值集合
     * 
     * @param propertyValues 包含Bean属性值的PropertyValues对象
     */
    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    /**
     * 获取Bean的初始化方法名
     * 
     * @return 初始化方法的名称
     */
    public String getInitMethodName() {
        return initMethodName;
    }

    /**
     * 设置Bean的初始化方法名
     * 
     * @param initMethodName 初始化方法的名称
     */
    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    /**
     * 获取Bean的销毁方法名
     * 
     * @return 销毁方法的名称
     */
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    /**
     * 设置Bean的销毁方法名
     * 
     * @param destroyMethodName 销毁方法的名称
     */
    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    /**
     * 获取Bean的作用域
     * 
     * @return 作用域标识符（"singleton"或"prototype"）
     */
    public String getScope() {
        return scope;
    }
}
