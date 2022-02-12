package io.project;

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
 * 用于读写project.xml文件的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class ProjectReadWrite {
    /**
     * 读取project.xml中的数据，并将其储存为Project类对象
     *
     * @return 返回project.xml中储存的所有project
     */
    public static List<Project> readProjectXML() {
        File xmlFile = new File("src/main/resources/xmls/project.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<Project> projectList = new ArrayList<Project>();
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ROW");

            for (int i = 0; i < nodeList.getLength(); i++) {
                projectList.add(getProjectFromXML(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * 将xml文件中的一个节点数据转换为Project类对象
     *
     * @param node project.xml文件中一个用户的所有数据
     * @return 返回Project类对象
     */
    private static Project getProjectFromXML(Node node) {
        Project project = new Project();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            project.setIdProject(getXMLTagValue(element, "IdProject"));
            project.setMasterIdUser(getXMLTagValue(element, "MasterIdUser"));
            project.setProjectName(getXMLTagValue(element, "ProjectName"));
            project.setDescription(getXMLTagValue(element, "Description"));
            project.setCreateTime(Long.parseLong(getXMLTagValue(element, "CreateTime")));
            project.setDeadlineTime(Long.parseLong(getXMLTagValue(element, "DeadlineTime")));
        }
        return project;
    }

    /**
     * 将项目数据写入project.xml文件
     *
     * @param projectList 写入project.xml的project列表
     */
    public void writeProjectXML(List<Project> projectList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Project temp;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element record = document.createElement("DATA");

            for (Project value : projectList) {
                temp = value;

                Element project = document.createElement("ROW");

                // 为client添加子节点
                Element idProject = document.createElement("IdProject");
                idProject.setTextContent(temp.getIdProject());
                project.appendChild(idProject);

                Element masterIdUser = document.createElement("MasterIdUser");
                masterIdUser.setTextContent(temp.getMasterIdUser());
                project.appendChild(masterIdUser);

                Element projectName = document.createElement("ProjectName");
                projectName.setTextContent(temp.getProjectName());
                project.appendChild(projectName);

                Element description = document.createElement("Description");
                description.setTextContent(temp.getDescription());
                project.appendChild(description);

                Element createTime = document.createElement("CreateTime");
                createTime.setTextContent("" + temp.getCreateTime());
                project.appendChild(createTime);

                Element deadlineTime = document.createElement("DeadlineTime");
                deadlineTime.setTextContent("" + temp.getDeadlineTime());
                project.appendChild(deadlineTime);

                record.appendChild(project);
            }

            document.appendChild(record);
            // generate xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/project.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

}
