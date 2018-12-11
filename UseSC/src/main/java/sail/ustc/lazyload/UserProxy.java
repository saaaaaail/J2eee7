package sail.ustc.lazyload;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sail.ustc.model.UserBean;
import sc.ustc.dao.Conversation;


import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProxy implements MethodInterceptor {
    //业务类对象
    private Object target;

    public Object getInstance(Object target){
        this.target=target;//业务对象赋值
        Enhancer enhancer = new Enhancer();//创建加强器，用来创建代理类
        enhancer.setSuperclass(this.target.getClass());//为加强器指定要代理的业务类
        //设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦截
        enhancer.setCallback(this);
        //创建动态代理类对象并返回
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object obj=null;
        UserBean userBean = (UserBean) o;
        String methodString = method.getName();
        //String methodString = "gets";
        System.out.println("methodString: "+methodString);
        String regex = "get[a-zA-Z]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(methodString);
        System.out.println(matcher.matches());
        if(matcher.matches()){
            System.out.println("调用了get方法！！");
            switch (methodString){
                case "getUserId":
                    break;
                case "getUserName":{
                    UserBean ub = (UserBean) target;
                    if(ub.getUserName()==null) {
                        System.out.println("查询username");
                        String username = sail.ustc.dao.Conversation.getFieldById(target, "name");
                        ub.setUserName(username);
                        target = ub;
                        userBean.setUserName(username);
                        System.out.println("查询username结束返回");
                    }
                    break;}
                case "getUserPass":{
                    UserBean ub = (UserBean) target;
                    if(ub.getUserPass()==null) {
                        System.out.println("查询userpass");
                        String password = sail.ustc.dao.Conversation.getFieldById(target, "password");
                        ub.setUserPass(password);
                        target = ub;
                        userBean.setUserPass(password);
                        System.out.println("查询userpass结束返回");
                    }
                    break;}

            }
            obj = methodProxy.invokeSuper(userBean, objects); //调用业务类（父类中）的方法
        }else {
            obj = methodProxy.invokeSuper(o, objects); //调用业务类（父类中）的方法
        }

        return obj;
    }
}
