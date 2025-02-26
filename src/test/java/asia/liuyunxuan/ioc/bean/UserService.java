package asia.liuyunxuan.ioc.bean;

import asia.liuyunxuan.ioc.beans.factory.DisposableBean;
import asia.liuyunxuan.ioc.beans.factory.InitializingBean;

public class UserService implements InitializingBean, DisposableBean {

    private String id;
    private String company;
    private String location;
    private UserDao userDao;

    @Override
    public void destroy() {
        System.out.println("执行：UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("执行：UserService.afterPropertiesSet");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String queryUserInfo() {
        return userDao.queryUserName(id) + "," + company + "," + location;
    }
}
