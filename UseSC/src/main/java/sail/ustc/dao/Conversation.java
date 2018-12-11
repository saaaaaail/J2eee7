package sail.ustc.dao;

import sail.ustc.model.UserBean;
import sc.ustc.dao.Configuration;
import sc.ustc.dao.JDBCEntity;
import sc.ustc.dao.ORMUserEntity;
import sc.ustc.dao.ProperityEntity;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

public class Conversation {
    public static Object loadObject(Object o){
        try{
            Configuration configuration = Configuration.getInstance();
            //获得类名
            Class cls = o.getClass();
            System.out.println("getObject: "+cls.getName());
            //获得映射关系
            ORMUserEntity ormUserEntity = configuration.configUserORM(cls.getName());
            //表名
            String table = ormUserEntity.getTable();
            System.out.println("table: "+ table);

            StringBuilder FIELD=new StringBuilder("");

            List<ProperityEntity> list = ormUserEntity.getPclist();

            //选择没有懒加载的数据
            for(ProperityEntity pe:list){
                if(pe.getLazy().equals("false")){
                    String column = pe.getColumn();
                    FIELD.append(" "+column+",");
                }
            }
            char lastChar = FIELD.charAt(FIELD.length()-1);
            if(lastChar==',') {
                FIELD.deleteCharAt(FIELD.length() - 1);
            }
            System.out.println("FIELD: "+FIELD);

            ResultSet resultSet=null;
            //获得id属性值
            for(ProperityEntity pe:list){
                if(pe.getColumn().equals("id")){
                    Field f = cls.getDeclaredField(pe.getName());
                    f.setAccessible(true);
                    //获得id属性值
                    String value = f.get(o).toString();

                    //sql
                    String SQL = " select "+FIELD+" from "+table+" where "+pe.getColumn()+" = "+value;
                    System.out.println(SQL);

                    Connection connection = getConnection();
                    PreparedStatement pstmt = connection.prepareStatement(SQL);
                    resultSet = pstmt.executeQuery();
                }
            }

            //构建返回对象
            Object newObj = cls.newInstance();
            while (resultSet.next()){
                for(ProperityEntity pe:list){
                    if(pe.getLazy().equals("false")){
                        Field f = cls.getDeclaredField(pe.getName());
                        f.setAccessible(true);
                        String value = resultSet.getString(pe.getColumn());
                        System.out.println("sql value: "+value);
                        f.set(newObj,value);
                    }
                }
            }

            System.out.println("loadObject返回对象");
            return newObj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  static String getFieldById(Object o,String field){
        try {
            Configuration  configuration = Configuration.getInstance();
            //获得类名
            Class cls = o.getClass();
            System.out.println("getObject: "+cls.getName());

            //获得映射关系
            ORMUserEntity ormEntity = configuration.configUserORM(cls.getName());
            //表名
            String table = ormEntity.getTable();
            String classString = ormEntity.getName();
            String className = classString.substring(classString.lastIndexOf(".")+1,classString.length());
            System.out.println(className);

            //获得原始类
            Class clazz = Class.forName(classString);
            ResultSet resultSet=null;
            String returnValue=null;
            List<ProperityEntity> list = ormEntity.getPclist();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getColumn().equals("id")){
                    Field f = cls.getDeclaredField(list.get(i).getName());
                    f.setAccessible(true);
                    //获得id属性值
                    String idValue = f.get(o).toString();
                    System.out.println("idValue: "+idValue);
                    //获得属性值
                    //String value = f.get(o).toString();

                    String SQL = "select "+field+" from " + table + " where " + list.get(i).getColumn() + " = " + idValue;
                    //String SQL = "select * from " + table + " where name = 'sail'";
                    System.out.println(SQL);
                    Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    resultSet = statement.executeQuery(SQL);
                }
            }

            while (resultSet.next()){
                returnValue = resultSet.getString(field);
            }
            System.out.println("returnValue: "+returnValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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
