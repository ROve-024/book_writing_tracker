package model.user;

import model.user.User;
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


public class UserReadWrite {
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

            for(int  i = 0 ; i<nodeList.getLength();i++){
                userList.add(getUserFromXML(nodeList.item(i)));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
    
    private static User getUserFromXML(Node node)
    {
        User user = new User();
        if (node.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) node;
            user.setIdUser(getXMLTagValue(element, "IdUser"));
            user.setUsername(getXMLTagValue(element, "Username"));
            user.setPassword(getXMLTagValue(element, "Password"));
            user.setQuestion(getXMLTagValue(element, "SecurityQuestion"));
            user.setAnswer(getXMLTagValue(element, "SecurityAnswer"));
        }
        return user;
    }

    private static String getXMLTagValue(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

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

                record.appendChild(user);
            }

            document.appendChild(record);


            // genera xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File xmlFile = new File("src/main/resources/xmls/user.xml");
            tf.transform(new DOMSource(document), new StreamResult(xmlFile));

        }
        catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

}
