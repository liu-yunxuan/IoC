package asia.liuyunxuan.ioc.context.annotation;


import asia.liuyunxuan.ioc.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import asia.liuyunxuan.ioc.beans.factory.config.BeanDefinition;
import asia.liuyunxuan.ioc.beans.factory.support.BeanDefinitionRegistry;
import asia.liuyunxuan.ioc.stereotype.Component;

import java.util.Set;


public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

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

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope) return scope.value();
        return " ";
    }

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
