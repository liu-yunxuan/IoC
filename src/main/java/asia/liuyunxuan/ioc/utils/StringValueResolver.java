package asia.liuyunxuan.ioc.utils;

/**
 * 字符串值解析器接口。
 * 
 * <p>该接口用于在IoC容器中解析字符串值，通常用于处理配置文件中的占位符、
 * 表达式或其他需要动态解析的字符串值。实现类可以提供自定义的字符串解析逻辑。
 *
 * @author liuyunxuan
 * @since 1.0
 */
public interface StringValueResolver {

    /**
     * 解析给定的字符串值。
     *
     * @param strVal 要解析的字符串值
     * @return 解析后的字符串值
     */
    String resolveStringValue(String strVal);

}
