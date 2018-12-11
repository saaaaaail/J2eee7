package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
    protected String driver;
    protected String url;
    protected String userName;
    protected String userPassword;
    protected Connection connection;

    public Connection openDBConnectionMYSQL(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,userName,userPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Connection openDBConnectionSQLITE(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean closeDBConnection(){
        try {
            if(connection!=null){
                connection.close();
            }
            System.out.println("close Connection");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract Object query(String sql);

    public abstract boolean insert(String sql);

    public abstract boolean update(String sql);

    public abstract boolean dalete(String sql);

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
