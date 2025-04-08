package asia.liuyunxuan.ioc.context.annotation;


import asia.liuyunxuan.ioc.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.support.BeanDefinitionRegistry;
import asia.liuyunxuan.ioc.stereotype.Component;

import java.util.Set;


/**
 * 类路径Bean定义扫描器，用于扫描并注册被注解标记的组件。
 * <p>
 * 该类继承自ClassPathScanningCandidateComponentProvider，在其基础扫描功能之上增加了：
 * <ul>
 *     <li>自动注册扫描到的Bean定义到容器</li>
 *     <li>处理Bean的作用域注解（@Scope）</li>
 *     <li>处理组件名称的生成和注册</li>
 *     <li>自动注册AutowiredAnnotationBeanPostProcessor用于处理依赖注入</li>
 * </ul>
 * @author liuyunxuan
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 执行扫描操作，扫描指定的基础包并注册发现的组件。
     *
     * @param basePackages 要扫描的基础包路径数组
     */
    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                // 解析 Bean 的作用域 singleton、prototype
                String beanScope = resolveBeanScope(beanDefinition);
                if (beanScope != null && !beanScope.isEmpty()) {
                    beanDefinition.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }
        registry.registerBeanDefinition("asia.liuyunxuan.ioc.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    /**
     * 解析Bean的作用域。
     *
     * @param beanDefinition 要解析的Bean定义
     * @return Bean的作用域，如果未指定则返回空字符串
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope) return scope.value();
        return " ";
    }

    /**
     * 确定Bean的名称。
     * <p>
     * 如果@Component注解指定了value，则使用该值作为Bean名称；
     * 否则使用类名的首字母小写形式作为Bean名称。
     * </p>
     * @param beanDefinition 要确定名称的Bean定义
     * @return 确定的Bean名称
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (value == null || value.isEmpty()) {
            String simpleName = beanClass.getSimpleName();
            if (!simpleName.isEmpty()) {
                value = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            } else {
                value = simpleName;
            }
        }
        return value;
    }

}
