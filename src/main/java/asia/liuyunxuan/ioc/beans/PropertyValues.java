package asia.liuyunxuan.ioc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean属性值集合的管理类
 * <p>
 * 用于管理一个Bean的所有属性值，提供属性的添加、获取等操作
 */
public class PropertyValues {

    /**
     * 存储PropertyValue的列表
     */
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 添加一个属性值到集合中
     *
     * @param pv 要添加的PropertyValue对象
     */
    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    /**
     * 获取所有属性值
     *
     * @return 包含所有PropertyValue对象的数组
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 根据属性名称获取对应的PropertyValue对象
     *
     * @param propertyName 属性名称
     * @return 如果找到则返回对应的PropertyValue对象，否则返回null
     */
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

}
