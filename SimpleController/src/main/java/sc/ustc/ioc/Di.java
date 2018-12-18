package sc.ustc.ioc;


import sc.ustc.ioc.config.Bean;
import sc.ustc.ioc.config.DiEntity;
import sc.ustc.ioc.config.Field;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class Di {

    private Di(){container.process();}
    private final static Di di = new Di();
    public static Di getInstance(){return di;}

    //获得IOC容器实例
    private Container container = Container.getInstance();
    //private Map<String,Object> iocMap = container.

    //获得xml解析对象
    DiEntity diEntity = DiEntity.getInstance();
    private Map<String, Bean> beanMap = diEntity.parseDi();

    private Object actionObj = null;

    public boolean Inject(){
        boolean flag =false;
        try {
            System.out.println("开始注入！！");
            Set keys = beanMap.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()){
                String id = (String) it.next();
                Bean curBean = beanMap.get(id);
                Class curClazz = Class.forName(curBean.getClazz());//当前bean的类变量
                Object curObj = curClazz.newInstance();//当前bean的实例
                List<Field> fields = curBean.getFields();
                if(fields!=null){
                     fields = curBean.getFields();
                     for(Field f:fields){
                         Bean refBean = judgeRef(f);//获得依赖bean的xml解析类
                         //下面获得当前bean类的setter方法，注入依赖bean类
                         BeanInfo beanInfo = Introspector.getBeanInfo(curClazz,Object.class);
                         PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                         for(PropertyDescriptor pd:pds){
                             System.out.println("pd.getName(): " + pd.getName());
                             //匹配当前依赖bean，找到对应的属性加载器
                             if(pd.getName().toString().equals(refBean.getId())){
                                 flag = true;
                                 Method method = pd.getWriteMethod();
                                 method.invoke(curObj,Class.forName(refBean.getClazz()).newInstance());
                             }
                         }
                     }
                }
                container.addObject(curBean.getId(),curObj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public Object getObject(Class cls) {
        try {
            System.out.println("cls.getName(): "+cls.getName());
            System.out.println("cls.getSimpleName(): "+cls.getSimpleName());
            Bean bean = judgeBean(cls);
            String key = bean.getId();
            return container.getObject(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(String id) {
        try {
            return container.getObject(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bean judgeRef(Field f){
        String ref = f.getBeanRef();//
        Bean bean = beanMap.get(ref);
        return bean;
    }

    public Bean judgeBean(Class cls){
        String classString = cls.getName();
        Set keys = beanMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()){
            String key = (String) it.next();
            if(beanMap.get(key).getClazz().equals(classString)){
                return beanMap.get(key);
            }
        }
        return null;
    }

}
