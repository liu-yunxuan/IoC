package asia.liuyunxuan.ioc.bean;

import asia.liuyunxuan.ioc.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao {
    private static final Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod(){
        System.out.println("执行：init-method");
        hashMap.put("10001", "张三");
        hashMap.put("10002", "李四");
        hashMap.put("10003", "王五");
    }

    static {
        hashMap.put("10001", "张三");
        hashMap.put("10002", "李四");
        hashMap.put("10003", "王五");
    }

    public void destroyDataMethod(){
        System.out.println("执行：destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String id) {
        return hashMap.get(id);
    }
}
