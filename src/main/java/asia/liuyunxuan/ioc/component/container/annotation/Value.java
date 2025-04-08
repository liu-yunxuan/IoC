package asia.liuyunxuan.ioc.component.container.annotation;

import java.lang.annotation.*;

/**
 * 属性值注入注解，用于注入外部配置的属性值。
 * <p>
 * 该注解允许将配置文件中的值、环境变量、系统属性等外部配置值注入到Bean的字段或方法参数中。
 * 支持使用"${...}"形式的占位符来引用属性值，也支持"#{...}"形式的表达式。
 * <p>
 * 该注解可以应用在以下位置：
 * <ul>
 *     <li>字段 - 直接将配置值注入到字段中</li>
 *     <li>方法 - 通过setter方法注入配置值</li>
 *     <li>方法参数 - 为方法参数注入配置值</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * public class DatabaseConfig {
 *     &#064;Value("${database.url}")
 *     private String databaseUrl;
 *     
 *     &#064;Value("${database.port:3306}")
 *     private int port;
 *     
 *     &#064;Value("#{systemProperties['user.region']}")
 *     private String region;
 * }
 * </pre>
 * <p>
 * 注意：如果指定的属性值不存在，且没有设置默认值，可能会导致注入失败。
 *
 * @see AutoInject
 */


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value();
}
