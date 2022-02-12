package io.usertask;

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
 * 用于读写user_task.xml文件的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserTaskReadWrite {
    /**
     * 读取user_task.xml中的数据，并将其储存为UserTask类对象
     *
     * @return 返回user_task.xml中储存的所有user_task
     */
    public static List<UserTask> readUserTaskXML() {
        File xmlFile = new File("src/main/resources/xmls/user_task.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<UserTask> userTaskList = new ArrayList<UserTask>();
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ROW");

            for (int i = 0; i < nodeList.getLength(); i++) {
                userTaskList.add(getUserTaskFromXML(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTaskList;
    }

    /**
     * 将xml文件中的一个节点数据转换为UserTask类对象
     *
     * @param node user_task.xml文件中一条记录
     * @return 返回UserTask类对象
     */
    private static UserTask getUserTaskFromXML(Node node) {
        UserTask userTask = new UserTask();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            userTask.setIdUser(getXMLTagValue(element, "IdUser"));
            userTask.setIdTask(getXMLTagValue(element, "IdTask"));
        }
        return userTask;
    }

    /**
     * 将用户数据写入user_task.xml文件
     *
     * @param userTaskList 写入user_project.xml的用户-项目关系列表
     */
    public void writeUserTaskXML(List<UserTask> userTaskList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        UserTask temp;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element record = document.createElement("DATA");

            for (UserTask value : userTaskList) {
                temp = value;

                Element userTask = document.createElement("ROW");

                // 为client添加子节点
                Element idUser = document.createElement("IdUser");
                idUser.setTextContent(temp.getIdUser());
                userTask.appendChild(idUser);

                Element idProject = document.createElement("IdTask");
                idProject.setTextContent(temp.getIdTask());
                userTask.appendChild(idProject);

                record.appendChild(userTask);
            }
            document.appendChild(record);
            // generate xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/user_task.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}
