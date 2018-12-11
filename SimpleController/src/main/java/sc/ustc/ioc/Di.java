package sc.ustc.ioc;


import sc.ustc.dao.ProperityEntity;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class Di {

    private Di(){
    }
    private static Di di = new Di();
    public static Di getInstance(){return di;}
    DiEntity diEntity = DiEntity.getInstance();
    Map<String, Bean> map = diEntity.parseDi();
    private Object actionObj = null;
    public boolean DependencyInject(String actionName){
        boolean flag =false;

        try {
            Class actionCls=null;//
            Set ks = map.keySet();
            Iterator it = ks.iterator();
            List<Field> fields = null;
            List<Bean> DependBeans = new ArrayList<>();//依赖的beans
            while (it.hasNext()) {
                String key = (String) it.next();
                if (key.equals(actionName)) {
                    Bean bean = map.get(key);
                    actionCls = Class.forName(bean.getClazz());//依赖类
                    fields = bean.getFields();
                }
            }

            //获得被依赖的bean的list
            if (fields != null) {
                flag=true;
                for (int i = 0; i < fields.size(); i++) {
                    String beanId = fields.get(i).getBeanRef();
                    it = ks.iterator();
                    while (it.hasNext()) {
                        if (beanId.equals(it.next())) {
                            DependBeans.add(map.get(beanId));
                        }
                    }
                }
            }

            actionObj = actionCls.newInstance();
            BeanInfo info = Introspector.getBeanInfo(actionCls,Object.class);
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for(PropertyDescriptor pd:pds) {
                System.out.println("pd.getName(): " + pd.getName());
                for (Bean b : DependBeans) {
                    if (pd.getName().equals(b.getId())&&pd.getName().equals("userBean")){
                        //初始化被依赖bean类
                        //执行setter方法
                        Method method = pd.getWriteMethod();
                        method.invoke(actionObj,Class.forName(b.getClazz()).newInstance());
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public Object getBean(String actionName) {
        if(actionObj==null){DependencyInject(actionName);}
        return actionObj;
    }


}
