package io.userproject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static utils.OtherUtils.getXMLTagValue;

/**
 * 用于读写user_project.xml文件的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserProjectReadWrite {
    /**
     * 读取user_project.xml中的数据，并将其储存为UserProject类对象
     *
     * @return 返回user_project.xml中储存的所有user_project
     */
    public static List<UserProject> readUserProjectXML() {
        File xmlFile = new File("src/main/resources/xmls/user_project.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<UserProject> userProjectList = new ArrayList<UserProject>();
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ROW");

            for (int i = 0; i < nodeList.getLength(); i++) {
                userProjectList.add(getUserProjectFromXML(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userProjectList;
    }

    /**
     * 将xml文件中的一个节点数据转换为UserProject类对象
     *
     * @param node user_project.xml文件中一条记录
     * @return 返回UserProject类对象
     */
    private static UserProject getUserProjectFromXML(Node node) {
        UserProject userProject = new UserProject();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            userProject.setIdUser(getXMLTagValue(element, "IdUser"));
            userProject.setIdProject(getXMLTagValue(element, "IdProject"));
        }
        return userProject;
    }

    /**
     * 将用户数据写入user_project.xml文件
     *
     * @param userProjectList 写入user_project.xml的用户-项目关系列表
     */
    public void writeUserProjectXML(List<UserProject> userProjectList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        UserProject temp;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element record = document.createElement("DATA");

            for (UserProject value : userProjectList) {
                temp = value;

                Element userProject = document.createElement("ROW");

                // 为client添加子节点
                Element idUser = document.createElement("IdUser");
                idUser.setTextContent(temp.getIdUser());
                userProject.appendChild(idUser);

                Element idProject = document.createElement("IdProject");
                idProject.setTextContent(temp.getIdProject());
                userProject.appendChild(idProject);

                record.appendChild(userProject);
            }
            document.appendChild(record);
            // generate xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/user_project.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }


}
