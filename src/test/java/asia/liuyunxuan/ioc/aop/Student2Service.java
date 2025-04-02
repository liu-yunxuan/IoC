package asia.liuyunxuan.ioc.aop;

import asia.liuyunxuan.ioc.stereotype.Component;

import java.util.Random;

@Component("studentService")
public class Student2Service implements IStudentService {

    private String token;

    public String selectUser() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return "张三 18 软件2101";
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

    @Override
    public String toString() {
        return "Student2Service{" +
                "token='" + token + '\'' +
                '}';
    }
}
