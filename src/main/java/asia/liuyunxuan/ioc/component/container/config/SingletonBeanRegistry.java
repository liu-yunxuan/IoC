package asia.liuyunxuan.ioc.component.container.config;

/**
 * 单例Bean注册表接口，定义了管理单例Bean的基本操作。
 * 提供了获取和注册单例Bean的能力，是Spring容器单例Bean管理的核心接口。
 */
public interface SingletonBeanRegistry {
    /**
     * 获取单例Bean
     * @param beanName 要获取的Bean的名称
     * @return 注册的单例Bean对象，如果不存在则返回null
     */
    Object getSingleton(String beanName);

    /**
     * 注册单例Bean
     * @param beanName 要注册的Bean的名称
     * @param singletonObject 要注册的单例Bean对象
     */
    void registerSingleton(String beanName, Object singletonObject);
}
