package sail.ustc.model;

import sail.ustc.dao.UserDAO;

public class UserBean {
    private String userId;
    private String userName;
    private String userPass;
    private boolean lazy;
    public UserBean(){}
    public UserBean(String userId){
        this.userId = userId;
    }
    public UserBean(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }
    public UserBean(String userId, String userName, String userPass) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
    }

    public boolean signIn(String id,String password){
        UserDAO userDAO=UserDAO.getInstance();
        //UserBean userBean = (UserBean) userDAO.query(id);
        UserBean userBean = (UserBean) userDAO.load(id);
        System.out.println("userBean: "+userBean);
        if (userBean!=null){
            if(userBean.getUserPass().equals(password)) {
                System.out.println("signIn中第二次getpassword: "+userBean.getUserPass().equals(password));
                return true;
            }
        }
        return false;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }


}
