package asia.liuyunxuan.ioc.component.container;

import asia.liuyunxuan.ioc.component.ComponentException;
import asia.liuyunxuan.ioc.runtime.RuntimeContext;

/**
 * 实现此接口的bean在初始化时会被注入BeanFactory的引用。
 * <p>
 * 这提供了一种方式让bean能够访问其容器，从而可以：
 * <ul>
 *   <li>通过编程方式获取其他bean</li>
 *   <li>检查容器提供的服务</li>
 *   <li>在需要时获取更多的容器特定功能</li>
 * </ul>
 * <p>
 * 注意：通常不建议使用此接口，因为它会将代码与Spring容器耦合，
 * 并且违反了控制反转（IoC）的原则。替代方案包括依赖注入和使用
 * {@link RuntimeContext}来访问其他bean。
 *
 * @author liuyunxuan
 * @see ComponentProvider
 * @see ComponentException
 */
public interface ComponentFactoryAware extends Aware {

   void setBeanFactory(ComponentProvider componentProvider) throws ComponentException;

}
