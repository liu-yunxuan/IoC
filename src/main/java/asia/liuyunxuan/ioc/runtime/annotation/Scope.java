package asia.liuyunxuan.ioc.runtime.annotation;

import java.lang.annotation.*;

/**
 * 用于声明Bean的作用域的注解。
 * <p>
 * 该注解可以应用于类或方法级别，用于指定Bean的作用域。默认作用域为singleton（单例）。
 * 支持的作用域包括：
 * <ul>
 *     <li>singleton - 单例模式，整个应用中只创建一个Bean实例</li>
 *     <li>prototype - 原型模式，每次请求都创建一个新的Bean实例</li>
 * </ul>
 * @author liuyunxuan
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    /**
     * 指定Bean的作用域
     * @return 作用域值，默认为"singleton"
     */
    String value() default "singleton";

}
