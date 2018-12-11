package sail.ustc.action;

import sail.ustc.model.UserBean;
import sc.ustc.ioc.Di;

public class LoginAction {
    private UserBean userBean;
    public String handleLogin(String name,String password) {
        Di di = Di.getInstance();
        LoginAction loginAction = (LoginAction) di.getBean("login");
        userBean = loginAction.getUserBean();
        System.out.println("执行handleLogin...");
        if (userBean.signIn(name,password)) {
            return "success";
        } else {
            return "failure";
        }
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
