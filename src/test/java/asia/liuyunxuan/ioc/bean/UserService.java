package asia.liuyunxuan.ioc.bean;


public class UserService {

    private String id;
    private String company;
    private String location;
    private IUserDao userDao;

    public String queryUserInfo() {
        return userDao.queryUserName(id) + "," + company + "," + location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }
}
