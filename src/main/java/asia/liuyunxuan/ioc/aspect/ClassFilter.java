package asia.liuyunxuan.ioc.aspect;

/**
 * 类型过滤器接口，用于判断给定的类型是否匹配切点表达式。
 * <p>
 * 在AOP中，切点不仅需要对方法进行匹配，还需要对类型进行匹配。ClassFilter接口
 * 负责类型匹配判断，与{@link MethodMatcher}配合完成完整的切点匹配。其主要特点：
 * <ul>
 *     <li>提供类级别的匹配判断</li>
 *     <li>可以基于类名、注解、接口等条件进行匹配</li>
 *     <li>通常与方法匹配器组合使用</li>
 * </ul>
 *
 * @author liuyunxuan
 * @see JoinPointSelector
 * @see MethodMatcher
 * @since 1.0
 */
public interface ClassFilter {

    /**
     * 判断给定的类型是否匹配切点表达式
     *
     * @param clazz 待匹配的类型
     * @return 如果类型匹配返回true，否则返回false
     */
    boolean matches(Class<?> clazz);

}
