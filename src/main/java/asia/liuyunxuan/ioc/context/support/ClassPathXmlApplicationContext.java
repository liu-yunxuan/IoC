package asia.liuyunxuan.ioc.context.support;


import asia.liuyunxuan.ioc.beans.BeansException;

/**
 * 基于类路径下XML配置文件的应用上下文实现类。
 * <p>
 * 该类提供了从类路径加载XML配置文件的具体实现：
 * <ul>
 *     <li>支持从类路径加载单个或多个XML配置文件</li>
 *     <li>提供了便捷的构造方法来初始化上下文</li>
 *     <li>自动执行容器的刷新操作</li>
 * </ul>
 * <p>
 * 这是一个独立的应用上下文实现，可以单独使用或作为Web应用上下文的基类。
 * @author liuyunxuan
 * @since 1.0
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    /**
     * 创建一个新的ClassPathXmlApplicationContext实例。
     * <p>
     * 使用无参构造方法创建实例后，需要手动设置配置文件位置并调用refresh方法。
     */
    public ClassPathXmlApplicationContext() {
    }


    /**
     * 创建一个新的ClassPathXmlApplicationContext实例。
     * <p>
     * 加载指定的XML配置文件，并自动刷新上下文。
     * @param configLocations XML配置文件的类路径
     * @throws BeansException 如果加载或解析配置文件时发生错误
     */
    public ClassPathXmlApplicationContext(String configLocations) throws BeansException {
        this(new String[]{configLocations});
    }

    /**
     * 创建一个新的ClassPathXmlApplicationContext实例。
     * <p>
     * 加载多个XML配置文件，并自动刷新上下文。
     * @param configLocations XML配置文件的类路径数组
     * @throws BeansException 如果加载或解析配置文件时发生错误
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }

    /**
     * 获取配置文件的位置。
     * <p>
     * 实现父类的抽象方法，返回在构造时设置的配置文件位置。
     * @return 配置文件位置的字符串数组
     */
    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

}
