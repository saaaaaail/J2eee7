package sc.ustc.interceptor;

import sc.ustc.util.XmlUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionProxy implements InvocationHandler {

    private Object target;
    private String fileString;
    private String actionName;
    private String resultString;


    public ActionProxy(Object target, String fileString, String actionName){
        this.target = target;
        this.fileString = fileString;
        this.actionName = actionName;
    }

    public Object bind(){
       return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        XmlUtil xmlUtil = XmlUtil.getInstance();

        //检查当前action是否有拦截器
        String interName = xmlUtil.parseInterceptor(fileString,actionName);
        if(interName.equals("log")){
            //调用前置方法
            preAction();

            //target方法
            result = method.invoke(target,args);
            resultString = (String)result;

            //调用后置方法
            afterAction();
        }else{
            result = method.invoke(target,args);
        }

        return result;
    }

    public void preAction(){
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            String methodString = xmlUtil.parseXml(fileString,"interceptor","log","predo");
            String classString = xmlUtil.parseXml(fileString,"interceptor","log","class");
            //System.out.println("preAction: "+classString);
            Class interPreClass = Class.forName(classString);
            Method interPre = interPreClass.getMethod(methodString,String.class);
            interPre.invoke(interPreClass.newInstance(),actionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterAction(){
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            String methodString = xmlUtil.parseXml(fileString,"interceptor","log","afterdo");
            String classString = xmlUtil.parseXml(fileString,"interceptor","log","class");
            //System.out.println("afterAction: "+classString);
            Class interAfterClass = Class.forName(classString);
            Method interPre = interAfterClass.getMethod(methodString,String.class);
            interPre.invoke(interAfterClass.newInstance(),resultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
