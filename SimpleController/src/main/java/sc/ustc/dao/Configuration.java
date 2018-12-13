package sc.ustc.dao;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private Configuration(){}
    private final static Configuration configuration = new Configuration();
    public static Configuration getInstance(){
        return configuration;
    }

    //类静态对象，存储映射关系
    private static ORMUserEntity ormUserEntity;

    //类静态对象，存储jdbc信息
    private static JDBCEntity jdbcEntity;

    public Document getDocument(){
        try {
            String fileString = this.getClass().getClassLoader().getResource("or_mapping.xml").getPath();
            System.out.println("getdoc: "+fileString);
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

    public JDBCEntity configJDBC(){
        if(jdbcEntity==null) {
            jdbcEntity = new JDBCEntity();
            Document document = getDocument();
            NodeList list = document.getElementsByTagName("jdbc");
            NodeList clist = list.item(0).getChildNodes();
            for (int i = 0; i < clist.getLength(); i++) {
                Node node = clist.item(i);
                NamedNodeMap nodeMap = node.getAttributes();
                String name = nodeMap.getNamedItem("name").getNodeValue();
                String value = nodeMap.getNamedItem("value").getNodeValue();
                System.out.println("jdbcConfig: " + name + " " + value);
                switch (name) {
                    case "driver":
                        jdbcEntity.setDriver(value);
                        break;
                    case "url":
                        jdbcEntity.setUrl(value);
                        break;
                    case "username":
                        jdbcEntity.setUsername(value);
                        break;
                    case "password":
                        jdbcEntity.setUserpass(value);
                        break;
                }
            }
        }
        return  jdbcEntity;
    }

    public ORMUserEntity configUserORM(String bean){
        if(ormUserEntity ==null) {
            ormUserEntity = new ORMUserEntity();
            List<ProperityEntity> properities = new ArrayList<>();
            Document document = getDocument();
            NodeList list = document.getElementsByTagName("class");
            System.out.println("configUserORM:listlength: " + list.getLength());
            String beanClass = bean;
            System.out.println("configUserORM:beanClass " + bean);
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                NamedNodeMap nodeMap = node.getAttributes();
                String name = nodeMap.getNamedItem("name").getNodeValue();
                String table = nodeMap.getNamedItem("table").getNodeValue();
                System.out.println("configUserORM:name,table: " + name + "," + nodeMap.getNamedItem("table").getNodeValue());
                if (name.equals(beanClass)) {
                    ormUserEntity.setName(name);
                    ormUserEntity.setTable(table);
                    NodeList clist = node.getChildNodes();
                    System.out.println("configUserORM:node: " + node.getNodeName());
                    System.out.println("configUserORM:clist: " + clist);
                    for (int j = 0; j < clist.getLength(); j++) {
                        NamedNodeMap cNodeMap = clist.item(j).getAttributes();
                        System.out.println("configUserORM:pname: " + cNodeMap.getNamedItem("name").getNodeValue());
                        String pname = cNodeMap.getNamedItem("name").getNodeValue();
                        String pcolumn = cNodeMap.getNamedItem("column").getNodeValue();
                        String ptype = cNodeMap.getNamedItem("type").getNodeValue();
                        String plazy = cNodeMap.getNamedItem("lazy").getNodeValue();
                        ProperityEntity pe = new ProperityEntity(pname, pcolumn, ptype, plazy);
                        properities.add(pe);
                    }
                    ormUserEntity.setPclist(properities);
                }
            }
        }
        return ormUserEntity;
    }
}
