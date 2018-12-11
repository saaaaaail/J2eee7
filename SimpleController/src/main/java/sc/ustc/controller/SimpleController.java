package sc.ustc.controller;

import sc.ustc.interceptor.Action;
import sc.ustc.interceptor.ActionPackage;
import sc.ustc.interceptor.ActionProxy;
import sc.ustc.util.XmlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;

public class SimpleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        String userName = req.getParameter("username");
        String userPass = req.getParameter("userpass");

        PrintWriter out = resp.getWriter();
        String actionName = getActionName(req);
        System.out.println(actionName);

        //xml解析获得匹配结果
        String fileString = this.getClass().getClassLoader().getResource("controller.xml").getPath();
        //System.out.println("controller.xml: "+fileString);
        XmlUtil xmlUtil = XmlUtil.getInstance();
        //String result = xmlUtil.analyzeAction(fileString,actionName);
        //System.out.println(result);

        //动态代理
        String result = (String) ProxyParseXml(fileString,actionName,userName,userPass);
        System.out.println(result);

        //根据结果执行跳转
        dispatch(req,resp,result,out);

    }

    private String getActionName(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf(".sc"));
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp,String result,PrintWriter out){
        try {
            String[] tmp = result.split(",");
            //System.out.println("dispatch: "+tmp[1]);
            switch (tmp[0]){
                case "forward": out.println(tmp[1]);req.getRequestDispatcher(tmp[1]).forward(req,resp);break;
                case "redirect": out.println(tmp[1]);resp.sendRedirect(tmp[1]);break;
                case "action:failure": out.println("不可识别的action请求");break;
                case "result:failure": out.println("没有请求的资源");break;
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object ProxyParseXml(String fileString,String actionName,String userName,String userPass){
        Action action = new ActionPackage();
        ActionProxy actionProxy = new ActionProxy(action,fileString,actionName);
        Action ap = (Action) Proxy.newProxyInstance(action.getClass().getClassLoader(),action.getClass().getInterfaces(),actionProxy);
        Object result = ap.doAction(actionName,userName,userPass);
        return result;
    }
}
