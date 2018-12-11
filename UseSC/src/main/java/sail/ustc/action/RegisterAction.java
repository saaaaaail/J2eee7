package sail.ustc.action;

public class RegisterAction {
    public String handleRegister(String name,String password){
        System.out.println("执行handleRegister...");
        return "success";
    }
}
