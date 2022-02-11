package controller.utlis;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String encryptByMD5(String password) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        return new BigInteger(1, digest).toString(16);
    }
}
