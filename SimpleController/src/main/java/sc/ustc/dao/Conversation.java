package sc.ustc.dao;

import sc.ustc.dao.config.JDBCEntity;
import sc.ustc.dao.config.ORMUserEntity;
import sc.ustc.dao.config.ProperityEntity;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

public class Conversation {

    public static Object getObject(Object o) throws SQLException {
        Connection connection=null;//数据库连接
        try {
            Configuration  configuration = Configuration.getInstance();
            //获得类名
            Class cls = o.getClass();
            System.out.println("getObject: "+cls.getName());
            //获得映射关系
            ORMUserEntity ormUserEntity = configuration.configUserORM(cls.getName());
            //表名
            String table = ormUserEntity.getTable();
            String className = ormUserEntity.getName();
            System.out.println("table: "+ table);

            //使代理对象与原始对象类属性一致

            ResultSet resultSet=null;
            //属性对应关系
            List<ProperityEntity> list = ormUserEntity.getPclist();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getColumn().equals("id")){
                    //获得对象属性
                    Field f = cls.getDeclaredField(list.get(i).getName());
                    f.setAccessible(true);
                    System.out.println("f: "+list.get(i).getName());
                    //获得属性值
                    String value = f.get(o).toString();

                    String SQL = "select * from " + table + " where " + list.get(i).getColumn() + " = " + value;
                    //String SQL = "select * from " + table + " where name = 'sail'";
                    System.out.println(SQL);
                    connection = getConnection();
                    Statement statement = connection.createStatement();
                    resultSet = statement.executeQuery(SQL);
                }
            }

            //构造返回对象
            Object newObject = cls.newInstance();

            while (resultSet.next()) {
                //获得对象属性
                for (int i = 0; i < list.size(); i++) {
                    Field f = cls.getDeclaredField(list.get(i).getName());
                    f.setAccessible(true);
                    //设置返回对象的属性值

                    String v = resultSet.getString(list.get(i).getColumn());
                    System.out.println("returnValue: " + v);
                    f.set(newObject, v);
                }
            }

            return newObject;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }
        return null;
    }

    public static boolean deleteObject(Object o) throws SQLException {
        Connection connection=null;
        try {
            Configuration configuration = Configuration.getInstance();
            Class cls = o.getClass();
            System.out.println("getObject: " + cls.getName());
            ORMUserEntity ormEntity = configuration.configUserORM(cls.getName());
            String table = ormEntity.getTable();
            PreparedStatement pstmt=null;
            int r=0;
            List<ProperityEntity> list = ormEntity.getPclist();
            for(int i=0;i<list.size();i++) {
                if (list.get(i).getColumn().equals("id")) {
                    Field f = cls.getDeclaredField(list.get(i).getName());
                    f.setAccessible(true);
                    String value = f.get(o).toString();
                    String SQL = "delete from " + table + " where " + list.get(i).getColumn() + " = " + value;
                    connection = getConnection();
                    pstmt = connection.prepareStatement(SQL);
                    r = pstmt.executeUpdate();
                }
            }
            if(r!=0){return true;}
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }
        return false;
    }



    private static Connection getConnection(){
        try {
            Configuration  configuration = Configuration.getInstance();
            JDBCEntity jdbcEntity = configuration.configJDBC();
            Class.forName(jdbcEntity.getDriver());
            Connection connection = DriverManager.getConnection(jdbcEntity.getUrl(),jdbcEntity.getUsername(),jdbcEntity.getUserpass());
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
