package controller.utlis;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 一些多次使用的工具
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class Utils {
    /**
     * 使用md5对数据进行加密
     *
     * @param data 待加密的字符串
     * @return 返回加密完成的数据
     */
    public static String encryptByMD5(String data) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        return new BigInteger(1, digest).toString(16);
    }

    /**
     * 获取一个Tag的值
     * @param element user.xml文件中一个用户的所有数据
     * @param tag 一个element中，tag的名称
     * @return 返回User类对象
     */
    public static String getXMLTagValue(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
