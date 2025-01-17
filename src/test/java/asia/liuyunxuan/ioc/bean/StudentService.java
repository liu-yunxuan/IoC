package asia.liuyunxuan.ioc.bean;

public class StudentService {
    private String name;

    public StudentService(String name) {
        this.name = name;
    }

    public StudentService() {
    }

    @Override
    public String toString() {
        return "StudentService{" +
                "name='" + name + '\'' +
                '}';
    }

    public void queryStudent() {
        System.out.println("查询学生信息");
    }
}
