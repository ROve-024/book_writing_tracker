package io.task;

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
 * 用于读写task.xml文件的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class TaskReadWrite {
    /**
     * 读取task.xml中的数据，并将其储存为Task类对象
     *
     * @return 返回task.xml中储存的所有task
     */
    public static List<Task> readTaskXML() {
        File xmlFile = new File("src/main/resources/xmls/task.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<Task> taskList = new ArrayList<Task>();
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ROW");

            for (int i = 0; i < nodeList.getLength(); i++) {
                taskList.add(getTaskFromXML(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * 将xml文件中的一个节点数据转换为Task类对象
     *
     * @param node task.xml文件中一个用户的所有数据
     * @return 返回Task类对象
     */
    private static Task getTaskFromXML(Node node) {
        Task task = new Task();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            task.setIdTask(getXMLTagValue(element, "IdTask"));
            task.setIdParentTask(getXMLTagValue(element, "IdParentTask"));
            task.setIdProject(getXMLTagValue(element, "IdProject"));
            task.setIdUser(getXMLTagValue(element, "IdUser"));
            task.setStatus(getXMLTagValue(element, "Status"));
            task.setTaskTitle(getXMLTagValue(element, "TaskTitle"));
            task.setCreateTime(Long.parseLong(getXMLTagValue(element, "CreateTime")));
            task.setDeadlineTime(Long.parseLong(getXMLTagValue(element, "DeadlineTime")));
            task.setDescription(getXMLTagValue(element, "Description"));

        }
        return task;
    }

    /**
     * 将任务数据写入task.xml文件
     *
     * @param taskList 写入task.xml的task列表
     */
    public void writeTaskXML(List<Task> taskList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Task temp;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element record = document.createElement("DATA");

            for (Task value : taskList) {
                temp = value;

                Element task = document.createElement("ROW");

                // 为client添加子节点
                Element idTask = document.createElement("IdTask");
                idTask.setTextContent(temp.getIdTask());
                task.appendChild(idTask);

                Element idParentTask = document.createElement("IdParentTask");
                idParentTask.setTextContent(temp.getIdParentTask());
                task.appendChild(idParentTask);

                Element idProject = document.createElement("IdProject");
                idProject.setTextContent(temp.getIdProject());
                task.appendChild(idProject);

                Element idUser = document.createElement("IdUser");
                idUser.setTextContent(temp.getIdUser());
                task.appendChild(idUser);

                Element status = document.createElement("Status");
                status.setTextContent(temp.getStatus());
                task.appendChild(status);

                Element taskTitle = document.createElement("TaskTitle");
                taskTitle.setTextContent(temp.getTaskTitle());
                task.appendChild(taskTitle);

                Element createTime = document.createElement("CreateTime");
                createTime.setTextContent("" + temp.getCreateTime());
                task.appendChild(createTime);

                Element deadlineTime = document.createElement("DeadlineTime");
                deadlineTime.setTextContent("" + temp.getDeadlineTime());
                task.appendChild(deadlineTime);

                Element description = document.createElement("Description");
                description.setTextContent(temp.getDescription());
                task.appendChild(description);

                record.appendChild(task);
            }

            document.appendChild(record);
            // generate xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/task.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}
