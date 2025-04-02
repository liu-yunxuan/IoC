package asia.liuyunxuan.ioc.aop;

import java.util.Random;

public class Student2Service implements IStudentService {

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

}
