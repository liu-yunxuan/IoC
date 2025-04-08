package asia.liuyunxuan.ioc.component.container;

/**
 * 定义了bean工厂的分层结构。
 * <p>
 * 这个接口表明bean工厂可以是层次化的，即可以有父子关系。
 * 这种层次结构允许在子容器中找不到bean定义时，
 * 可以在父容器中继续查找，形成一个完整的容器体系。
 * <p>
 * 这种机制常用于：
 * <ul>
 *     <li>Web应用中的父子容器关系</li>
 *     <li>不同模块间的配置隔离</li>
 *     <li>允许bean定义的继承和覆盖</li>
 * </ul>
 *
 * @see ComponentProvider
 */
public interface HierarchicalProvider extends ComponentProvider {
}
