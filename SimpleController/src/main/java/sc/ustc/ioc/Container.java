package sc.ustc.ioc;

import sc.ustc.ioc.config.Bean;
import sc.ustc.ioc.config.DiEntity;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Container {

    //ioc容器
    private static Map<String,Object> iocMap;

    //保存解析xml的bean类信息
    private static Map<String,Bean> beanMap;
    private Container(){

    }

    private static Container container = new Container();

    public static Container getInstance(){
        return container;
    }

    public void addObject(String id,Object o){
        iocMap.put(id,o);
    }

    public Object getObject(String id){
        return iocMap.get(id);
    }

    //加载xml解析对象
    public void loadDiXml(){
        DiEntity diEntity = DiEntity.getInstance();
        beanMap = diEntity.parseDi();
    }

    //存储到IOC容器
    public void store(){
        if(iocMap==null){
            iocMap = new HashMap<>();
            Set keys = beanMap.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()){
                String key = (String) it.next();
                Bean bean = beanMap.get(key);
                Class cls=null;//类变量
                Object newObj=null;//实例
                try {
                    cls = Class.forName(bean.getClazz());
                    newObj = cls.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                iocMap.put(beanMap.get(key).getId(),newObj);
            }
        }
    }

    public void process(){
        if (beanMap==null){loadDiXml();}
        store();
    }


}
