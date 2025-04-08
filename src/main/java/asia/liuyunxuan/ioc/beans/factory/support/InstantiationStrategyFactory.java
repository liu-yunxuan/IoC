package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.spi.ExtensionLoader;

import java.io.IOException;

/**
 * Bean实例化策略工厂类。
 * <p>
 * 该工厂类负责创建和管理Bean实例化策略的实例。它使用SPI机制来加载和管理
 * 不同的实例化策略实现。通过这种方式，可以在运行时动态地选择和切换不同的
 * 实例化策略，比如JDK反射方式或CGLib方式。
 */
public class InstantiationStrategyFactory {
    
   static {
       try {
           ExtensionLoader.getInstance().loadExtension(InstantiationStrategy.class);
       } catch (IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
   }
    
    /**
     * 获取指定类型的实例化策略。
     *
     * @param strategyType 策略类型，如"jdk"表示使用JDK反射方式，为空时默认使用JDK方式
     * @return 对应类型的实例化策略实例
     */
    public static InstantiationStrategy getStrategy(String strategyType) {
        if (strategyType != null && !strategyType.isEmpty()) {
            return ExtensionLoader.getInstance().get(strategyType);
        }
        return ExtensionLoader.getInstance().get("jdk");
    }
}