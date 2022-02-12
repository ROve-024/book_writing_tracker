package model.user;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static controller.utlis.Utils.getXMLTagValue;


/**
 * 用于读写user.xml文件的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class UserReadWrite {
    /**
     * 读取user.xml中的数据，并将其储存为User类对象
     *
     * @return 返回user.xml中储存的所有user
     */
    public static List<User> readUserXML() {
        File xmlFile = new File("src/main/resources/xmls/user.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<User> userList = new ArrayList<User>();
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ROW");

            for (int i = 0; i < nodeList.getLength(); i++) {
                userList.add(getUserFromXML(nodeList.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 将xml文件中的一个节点数据转换为User类对象
     *
     * @param node user.xml文件中一个用户的所有数据
     * @return 返回User类对象
     */
    private static User getUserFromXML(Node node) {
        User user = new User();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            user.setIdUser(getXMLTagValue(element, "IdUser"));
            user.setUsername(getXMLTagValue(element, "Username"));
            user.setPassword(getXMLTagValue(element, "Password"));
            user.setQuestion(getXMLTagValue(element, "SecurityQuestion"));
            user.setAnswer(getXMLTagValue(element, "SecurityAnswer"));
            user.setEmail(getXMLTagValue(element, "Email"));
            user.setTelephone(getXMLTagValue(element, "Telephone"));
        }
        return user;
    }

    /**
     * 将用户数据写入user.xml文件
     *
     * @param userList 写入user.xml的user列表
     */
    public void writeXML(List<User> userList) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        User temp;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element record = document.createElement("DATA");

            for (User value : userList) {
                temp = value;

                Element user = document.createElement("ROW");

                // 为client添加子节点
                Element idUser = document.createElement("IdUser");
                idUser.setTextContent(temp.getIdUser());
                user.appendChild(idUser);

                Element username = document.createElement("Username");
                username.setTextContent(temp.getUsername());
                user.appendChild(username);

                Element password = document.createElement("Password");
                password.setTextContent(temp.getPassword());
                user.appendChild(password);

                Element question = document.createElement("SecurityQuestion");
                question.setTextContent(temp.getQuestion());
                user.appendChild(question);

                Element answer = document.createElement("SecurityAnswer");
                answer.setTextContent(temp.getAnswer());
                user.appendChild(answer);

                Element email = document.createElement("Email");
                email.setTextContent(temp.getEmail());
                user.appendChild(email);

                Element telephone = document.createElement("Telephone");
                telephone.setTextContent(temp.getTelephone());
                user.appendChild(telephone);

                record.appendChild(user);
            }
            document.appendChild(record);
            // generate xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/user.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

}
