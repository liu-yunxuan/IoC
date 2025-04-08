package asia.liuyunxuan.ioc.component.container.config;


/**
 * Bean引用的封装类，用于在Bean定义中表示对其他Bean的依赖关系。
 * 当一个Bean的属性需要引用另一个Bean时，使用此类来保存被引用Bean的名称。
 */
public class ComponentReference {

    private final String beanName;

    /**
     * 创建一个新的Bean引用实例
     * @param beanName 被引用的Bean名称
     */
    public ComponentReference(String beanName) {
        this.beanName = beanName;
    }

    /**
     * 获取被引用的Bean名称
     * @return Bean名称
     */
    public String getBeanName() {
        return beanName;
    }

}
