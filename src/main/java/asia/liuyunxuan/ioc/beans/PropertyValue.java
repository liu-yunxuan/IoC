package asia.liuyunxuan.ioc.beans;

/**
 * Bean属性值的封装类
 * <p>
 * 用于存储Bean属性的名称和对应的值，作为Bean定义中属性注入的基本单元
 */
public class PropertyValue {

    /**
     * 属性名称
     */
    private final String name;

    /**
     * 属性值
     */
    private final Object value;

    /**
     * 创建一个新的PropertyValue实例
     *
     * @param name  属性名称
     * @param value 属性值
     */
    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取属性名称
     *
     * @return 属性名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取属性值
     *
     * @return 属性值
     */
    public Object getValue() {
        return value;
    }

}
