package asia.liuyunxuan.ioc.bean;

import asia.liuyunxuan.ioc.aop.IStudentService;
import asia.liuyunxuan.ioc.beans.factory.annotation.Autowired;
import asia.liuyunxuan.ioc.beans.factory.annotation.Value;
import asia.liuyunxuan.ioc.stereotype.Component;

import java.util.Random;

@Component("studentService")
public class Student2Service implements IStudentService {

    @Value("${token}")
    private String token;
    @Autowired
    private UserDao userDao;

    public String selectUser() {
//        try {
//            Thread.sleep(new Random(1).nextInt(100));
//        } catch (InterruptedException e) {
//            throw new RuntimeException();
//        }
//        return "张三 18 软件2101";
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userDao.queryUserName("10001") + "，" + token;
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return "注册用户：" + userName + " success！";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String toString() {
        return "Student2Service{" +
                "token='" + token + '\'' +
                ", userDao=" + userDao +
                '}';
    }

    public Student2Service(String token, UserDao userDao) {
        this.token = token;
        this.userDao = userDao;
    }

    public Student2Service() {
    }
}
