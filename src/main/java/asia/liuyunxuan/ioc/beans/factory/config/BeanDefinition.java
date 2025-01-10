package asia.liuyunxuan.ioc.beans.factory.config;

public class BeanDefinition {
    public Class bean;

    public BeanDefinition(Class bean) {
        this.bean = bean;
    }

    public Class getBean() {
        return bean;
    }

    public void setBean(Class bean) {
        this.bean = bean;
    }
}

