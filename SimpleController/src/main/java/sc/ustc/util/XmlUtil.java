package sc.ustc.util;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XmlUtil {

    private XmlUtil(){}

    private static XmlUtil xmlUtil = new XmlUtil();

    public static XmlUtil getInstance(){
        if(xmlUtil==null){xmlUtil = new XmlUtil();}
        return xmlUtil;
    }

    public String analyzeAction(String file, String actionName) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(file));
            NodeList actions = doc.getElementsByTagName("action");
            NodeList interceptors = doc.getElementsByTagName("interceptor");
            int actionsLength = actions.getLength();
            int interLength = interceptors.getLength();

            for (int i = 0; i < actionsLength; i++) {
                Node actionNode = actions.item(i);
                //获取Node节点所有属性值
                NamedNodeMap actionNodeMap = actionNode.getAttributes();
                String nameString = actionNodeMap.getNamedItem("name").getNodeValue();
                String methodString = actionNodeMap.getNamedItem("method").getNodeValue();
                String classString = actionNodeMap.getNamedItem("class").getNodeValue();
                String resultString=" ";
                if (nameString.equals(actionName)) {
                    //get单个节点中的子节点list
                    NodeList actionChildNodes = actionNode.getChildNodes();

                    for(int j=0;j<actionChildNodes.getLength();j++){
                        if (actionChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if(actionChildNodes.item(j).getNodeName().equals("interceptor-ref")){
                                NamedNodeMap interRefMap = actionChildNodes.item(j).getAttributes();
                                String interRefName = interRefMap.getNamedItem("name").getNodeValue();
                                for(int k=0;k<interLength;k++){
                                    Node interNode = interceptors.item(i);
                                    NamedNodeMap interMap = interNode.getAttributes();
                                    String interNameString = interMap.getNamedItem("name").getNodeValue();
                                    String interPreString = interMap.getNamedItem("predo").getNodeValue();
                                    String interAfterString = interMap.getNamedItem("afterdo").getNodeValue();
                                    String interClassString = interMap.getNamedItem("class").getNodeValue();

                                    //拦截器查找成功
                                    if(interNameString.equals(interRefName)){

                                        //拦截器
                                        Class interClass = Class.forName(interClassString);
                                        Method interPre = interClass.getMethod(interPreString,String.class);
                                        Method interAfter = interClass.getMethod(interAfterString,String.class);

                                        //Action
                                        Class clazz = Class.forName(classString);
                                        Method method = clazz.getMethod(methodString);

                                        //predo action afterdo
                                        interPre.invoke(interClass.newInstance(),nameString);
                                        Object obj = method.invoke(clazz.newInstance());
                                        resultString = (String)obj;
                                        interAfter.invoke(interClass.newInstance(),resultString);

                                    }
                                }
                            }
                        }
                    }

                    for (int j = 0; j < actionChildNodes.getLength(); j++) {
                        if (actionChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (actionChildNodes.item(j).getNodeName().toString().equals("result")) {//result
                                NamedNodeMap resultMap = actionChildNodes.item(j).getAttributes();
                                String resultName = resultMap.getNamedItem("name").getNodeValue();
                                String resultType = resultMap.getNamedItem("type").getNodeValue();
                                String resultValue = resultMap.getNamedItem("value").getNodeValue();
                                if (resultName.equals(resultString)) {
                                    return resultType + "," + resultValue;
                                }
                            }
                        }
                    }
                    //result不匹配
                    return "result:failure";
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //没有对应的action
        return "action:failure";
    }

    public String parseXml(String fileString,String field,String name,String attr) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileString));
            NodeList fields = doc.getElementsByTagName(field);
            for(int i=0;i<fields.getLength();i++){
                if(fields.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(name)){
                    return fields.item(i).getAttributes().getNamedItem(attr).getNodeValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String parseXml(String fileString,String field,String name) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileString));
            NodeList fields = doc.getElementsByTagName(field);

            for(int i=0;i<fields.getLength();i++){
                NamedNodeMap map = fields.item(i).getAttributes();
                if(map.getNamedItem("name").getNodeValue().equals(name)){
                    return map.getNamedItem("class").getNodeValue()
                            +","+ map.getNamedItem("method").getNodeValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "action:failure";
    }

    public String parseInterceptor(String fileString,String name){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileString));
            NodeList interceptors = doc.getElementsByTagName("interceptor");
            NodeList actions = doc.getElementsByTagName("action");
            for(int i=0;i<actions.getLength();i++){
                NamedNodeMap map = actions.item(i).getAttributes();
                if(map.getNamedItem("name").getNodeValue().equals(name)){
                    NodeList childs = actions.item(i).getChildNodes();
                    for (int j=0;j<childs.getLength();j++){
                      if(childs.item(j).getNodeName().equals("interceptor-ref")){
                          for(int k=0;k<interceptors.getLength();k++){
                              if(childs.item(j).getAttributes().getNamedItem("name").getNodeValue()
                                      .equals(interceptors.item(k).getAttributes().getNamedItem("name").getNodeValue())){
                                  return interceptors.item(k).getAttributes().getNamedItem("name").getNodeValue();
                              }
                          }

                      }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String parseChild(String fileString,String name,String result){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileString));
            NodeList actions = doc.getElementsByTagName("action");
            for(int i=0;i<actions.getLength();i++){
                NamedNodeMap map = actions.item(i).getAttributes();
                if(map.getNamedItem("name").getNodeValue().equals(name)){
                    NodeList childs = actions.item(i).getChildNodes();
                    for (int j=0;j<childs.getLength();j++){
                        if (childs.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (childs.item(j).getNodeName().equals("result")) {//result
                                //System.out.println("parseChild: "+childs.item(j).getAttributes().getNamedItem("name").getNodeValue());
                                if (result.equals(childs.item(j).getAttributes().getNamedItem("name").getNodeValue())) {
                                    String typeString = childs.item(j).getAttributes().getNamedItem("type").getNodeValue();
                                    String valueString = childs.item(j).getAttributes().getNamedItem("value").getNodeValue();
                                    return typeString + "," + valueString + "," + childs.item(j).getAttributes().getNamedItem("name").getNodeValue();
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "result:failure";
    }


        public void writeToXml(String fileString,String name,String result,String startTime,String endTime){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        File file = new File(fileString);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList logs = doc.getElementsByTagName("log");
            Node logElement = logs.item(0);
            if(logElement==null){
                logElement = doc.createElement("log");
                doc.appendChild(logElement);
            }
            Element actionElement = doc.createElement("action");
            Element nameElement = doc.createElement("name");
            Element startElement = doc.createElement("start_time");
            Element endElement = doc.createElement("end_time");
            Element resultElement = doc.createElement("result");

            nameElement.setTextContent(name);
            startElement.setTextContent(startTime);
            endElement.setTextContent(endTime);
            resultElement.setTextContent(result);

            actionElement.appendChild(nameElement);
            actionElement.appendChild(startElement);
            actionElement.appendChild(endElement);
            actionElement.appendChild(resultElement);

            logElement.appendChild(actionElement);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建Transformer对象
            Transformer tf = tff.newTransformer();
            // 设置输出数据时换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 使用Transformer的transform()方法将DOM树转换成XML
            tf.transform(new DOMSource(doc), new StreamResult(file));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
