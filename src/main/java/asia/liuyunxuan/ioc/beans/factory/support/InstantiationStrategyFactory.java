package asia.liuyunxuan.ioc.beans.factory.support;

import asia.liuyunxuan.ioc.spi.ExtensionLoader;

import java.io.IOException;

public class InstantiationStrategyFactory {
    
   static {
       try {
           ExtensionLoader.getInstance().loadExtension(InstantiationStrategy.class);
       } catch (IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
   }
    
    public static InstantiationStrategy getStrategy(String strategyType) {
        if (strategyType != null && !strategyType.isEmpty()) {
            return ExtensionLoader.getInstance().get(strategyType);
        }
        return ExtensionLoader.getInstance().get("jdk");
    }
}