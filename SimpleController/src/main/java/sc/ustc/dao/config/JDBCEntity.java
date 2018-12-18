package sc.ustc.dao.config;

public class JDBCEntity {
    private String driver;
    private String url;
    private String username;
    private String userpass;

    public JDBCEntity(){}

    public JDBCEntity(String driver, String url, String username, String userpass) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.userpass = userpass;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
}
