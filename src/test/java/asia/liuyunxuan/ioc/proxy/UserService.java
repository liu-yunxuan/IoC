package asia.liuyunxuan.ioc.proxy;

import java.util.Random;


public class UserService implements IUserService {

    private String token;

    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "张三，" + token;
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "注册用户：" + userName + " success！";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
