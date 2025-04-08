package asia.liuyunxuan.ioc.annotation;

import java.lang.annotation.*;

/**
 * 组件注解，用于标识一个类为Spring IoC容器管理的组件。
 * 
 * <p>被此注解标记的类将被自动检测并注册到IoC容器中，成为一个Bean。
 * 通常用于标记服务层、数据访问层或其他应用组件，使其能够被容器自动扫描和管理。
 *
 * @author liuyunxuan
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Injectable {

    /**
     * 指定组件的名称。
     * 
     * <p>如果未指定，则默认使用类名的首字母小写形式作为Bean的名称。
     * 该名称将用作注册到容器中的Bean的标识符。
     *
     * @return 组件的名称
     */
    String value() default "";

}
