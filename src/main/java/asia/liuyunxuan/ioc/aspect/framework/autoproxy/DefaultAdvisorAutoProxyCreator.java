package asia.liuyunxuan.ioc.aspect.framework.autoproxy;

import asia.liuyunxuan.ioc.aspect.*;
import asia.liuyunxuan.ioc.aspect.aspectj.AspectJExpressionPointcutAspectAdvisor;
import asia.liuyunxuan.ioc.aspect.framework.ProxyFactory;
import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.component.PropertyValues;
import asia.liuyunxuan.ioc.component.container.ComponentFactoryAware;
import asia.liuyunxuan.ioc.component.container.ComponentProvider;
import asia.liuyunxuan.ioc.component.container.config.InstantiationAwareBeanPostProcessor;
import asia.liuyunxuan.ioc.component.container.support.DefaultRegistry;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * AOP自动代理创建器的默认实现，负责为匹配的Bean自动创建代理对象。
 * <p>
 * 该类在Spring AOP中扮演着核心角色，主要功能包括：
 * <ul>
 *     <li>自动识别系统中的切面配置</li>
 *     <li>为符合切点表达式的Bean创建代理</li>
 *     <li>处理代理对象的早期引用，解决循环依赖问题</li>
 * </ul>
 * <p>
 * 工作流程：
 * <ol>
 *     <li>在Bean实例化后，检查是否需要创建代理</li>
 *     <li>收集系统中所有的AspectJ切面配置</li>
 *     <li>判断Bean是否匹配切点表达式</li>
 *     <li>如果匹配，则创建代理对象</li>
 * </ol>
 *
 * @see InstantiationAwareBeanPostProcessor
 * @see ComponentFactoryAware
 * @see AspectJExpressionPointcutAspectAdvisor
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, ComponentFactoryAware {

    /** 用于访问和管理Bean的工厂 */
    private DefaultRegistry beanFactory;

    /** 代理对象的名称标识 */
    private String proxyName=null;

    /**
     * 设置代理对象的名称
     * @param proxyName 代理对象的名称
     */
    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    /** 用于存储已经被提前代理的Bean名称，防止重复代理 */
    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());
    /**
     * 设置BeanFactory，用于后续获取Advisor
     *
     * @param componentProvider 容器的BeanFactory
     * @throws ComponentException 当类型转换失败时抛出
     */
    @Override
    public void setBeanFactory(ComponentProvider componentProvider) throws ComponentException {
        this.beanFactory = (DefaultRegistry) componentProvider;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws ComponentException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws ComponentException {
        return true;
    }

    /**
     * 判断给定的类是否是AOP基础设施类
     * <p>
     * 基础设施类包括：Advice、JoinPointSelector、Advisor等，这些类不需要被代理
     *
     * @param beanClass 要检查的类
     * @return 如果是基础设施类返回true，否则返回false
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || JoinPointSelector.class.isAssignableFrom(beanClass) || AspectAdvisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws ComponentException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws ComponentException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }

        return bean;
    }

    /**
     * 如果必要的话，为Bean创建代理对象
     * <p>
     * 创建代理的条件：
     * <ul>
     *     <li>不是AOP基础设施类</li>
     *     <li>存在匹配的切面</li>
     * </ul>
     *
     * @param bean 原始的Bean实例
     * @param beanName Bean的名称
     * @return 如果需要代理则返回代理对象，否则返回原始Bean
     */
    protected Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAspectAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAspectAdvisor.class).values();

        for (AspectJExpressionPointcutAspectAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(true);

            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy(proxyName);
        }

        return bean;
    }

    /**
     * 获取Bean的早期引用，用于处理循环依赖
     * <p>
     * 当发生循环依赖时，需要提前暴露代理对象。此方法会：
     * <ul>
     *     <li>记录已被代理的Bean</li>
     *     <li>创建并返回代理对象</li>
     * </ul>
     *
     * @param bean 原始的Bean实例
     * @param beanName Bean的名称
     * @return 代理对象或原始Bean
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws ComponentException {
        return pvs;
    }

}
