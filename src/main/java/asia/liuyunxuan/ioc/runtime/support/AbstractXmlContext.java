package asia.liuyunxuan.ioc.runtime.support;


import asia.liuyunxuan.ioc.component.container.support.DefaultRegistry;
import asia.liuyunxuan.ioc.component.container.xml.XmlComponentDefinitionReader;

/**
 * 基于XML配置的应用上下文抽象实现类。
 * <p>
 * 该类提供了从XML文件加载Bean定义的基本实现：
 * <ul>
 *     <li>使用XmlBeanDefinitionReader读取XML配置文件</li>
 *     <li>支持多个XML配置文件的加载</li>
 *     <li>提供配置文件位置的抽象方法</li>
 * </ul>
 * <p>
 * 子类主要需要实现{@link #getConfigLocations()}方法来提供XML配置文件的位置。
 * @author liuyunxuan
 */
public abstract class AbstractXmlContext extends AbstractRefreshableContext {

    /**
     * 实现父类的抽象方法，从XML配置文件中加载Bean定义。
     * <p>
     * 该方法创建XmlBeanDefinitionReader实例，并使用它来加载XML配置文件中的Bean定义。
     * @param beanFactory 要加载Bean定义的目标工厂
     */
    @Override
    protected void loadBeanDefinitions(DefaultRegistry beanFactory) {
        XmlComponentDefinitionReader beanDefinitionReader = new XmlComponentDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    /**
     * 获取XML配置文件的位置。
     * <p>
     * 这是一个模板方法，由具体的子类实现，用于提供XML配置文件的具体位置。
     * 可以返回多个配置文件位置。
     * @return XML配置文件的位置数组，如果没有配置文件可以返回null
     */
    protected abstract String[] getConfigLocations();

}
