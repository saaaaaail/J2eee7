package sc.ustc.ioc;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sc.ustc.dao.Configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiEntity {

    private DiEntity(){}
    private final static DiEntity diEntity = new DiEntity();
    public static DiEntity getInstance(){
        return diEntity;
    }

    private static Map<String,Bean> map;

    public Map parseDi(){
        if(map==null) {
            map = new HashMap<>();
            Document document = getDocument();
            NodeList beans = document.getElementsByTagName("bean");
            for(int i=0;i<beans.getLength();i++){
                Node node = beans.item(i);
                NamedNodeMap namedNodeMap = node.getAttributes();
                String beanId = namedNodeMap.getNamedItem("id").getNodeValue();
                String beanClass = namedNodeMap.getNamedItem("class").getNodeValue();
                List<Field> fields = new ArrayList<>();
                NodeList clist = node.getChildNodes();
                for(int j=0;j<clist.getLength();j++) {
                    if (clist.item(j).getNodeName().equals("field")) {
                        NamedNodeMap cNodeMap = clist.item(j).getAttributes();
                        String fieldName = cNodeMap.getNamedItem("name").getNodeValue();
                        String beanRef = cNodeMap.getNamedItem("bean-ref").getNodeValue();
                        Field field = new Field(fieldName,beanRef);
                        fields.add(field);
                    }
                }
                Bean bean = new Bean(beanId,beanClass,fields);
                map.put(beanId,bean);
            }
        }
        return map;
    }

    public Document getDocument(){
        try {
            String fileString = this.getClass().getClassLoader().getResource("di.xml").getPath();
            System.out.println("getdidoc: "+fileString);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(fileString));
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
