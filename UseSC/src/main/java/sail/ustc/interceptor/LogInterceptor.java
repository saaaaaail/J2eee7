package sail.ustc.interceptor;

import sail.ustc.interfacer.Interceptor;
import sc.ustc.util.XmlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LogInterceptor implements Interceptor {

    private static String startTime;
    private static String endTime;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String name;
    private static String result;

    public void preAction(String actionName){
        Date date = new Date();
        startTime = sdf.format(date);
        name = actionName;
        System.out.println("actionName: "+name+" "+"startTime: "+startTime);
    }

    public void afterAction(String resultString){
        Date date = new Date();
        endTime = sdf.format(date);
        String[] tmp = resultString.split(",");
        result = tmp[2];
        System.out.println("actionName: "+name+" "+"result: "+result+" "
                +"startTime: "+startTime+" "+"endTime: "+endTime);
        XmlUtil xmlUtil = XmlUtil.getInstance();
        String file = this.getClass().getClassLoader().getResource("log.xml").getPath();
        System.out.println(file);
        xmlUtil.writeToXml(file,name,result,startTime,endTime);
    }



}
