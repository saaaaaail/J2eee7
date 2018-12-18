package sc.ustc.interceptor;

import sc.ustc.ioc.Di;
import sc.ustc.util.XmlUtil;

import java.lang.reflect.Method;

public class ActionPackage implements Action{

    @Override
    public String doAction(String actionName,String userid,String userpass) {
        try {
            Di di = Di.getInstance();
            String fileString = this.getClass().getClassLoader().getResource("controller.xml").getPath();
            XmlUtil xmlUtil = XmlUtil.getInstance();
            String actionMess = xmlUtil.parseXml(fileString,"action",actionName);
            String[] mess = actionMess.split(",");
            Object obj=null;//保存action方法执行结果
            //mess[0] actionclass
            //mess[1] method
            if(mess[0].equals("action:failure")){
                return mess[0];
            }

            //注入所有bean
            di.Inject();
            //从容器中获得bean实例
            Object object = di.getObject(actionName);

            //Class actionClass = Class.forName(mess[0]);
            //以下完成bean实例中对应controller.xml的跳转
            Class actionClass = object.getClass();
            Method method = actionClass.getMethod(mess[1],String.class,String.class);
            obj = method.invoke(object,userid,userpass);
            System.out.println((String)obj);

            String result = xmlUtil.parseChild(fileString,actionName,(String)obj);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
