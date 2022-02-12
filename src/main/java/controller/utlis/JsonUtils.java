package controller.utlis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * 用于处理Json文件
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class JsonUtils {
    /**
     * 从文件中获取json对象
     *
     * @param path fxml文件的根节点
     * @return 返回文件储存的Json对象
     */
    public static JSONObject getJsonObjectFromFile(String path) {
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                result.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(result.toString().replace(" ", "").strip());
    }

    /**
     * 将json对象保存到文件中
     *
     * @param jsonObject 要保存的json对象
     * @param path       保存到目标文件的路径
     */
    public static void saveJsonToFile(JSONObject jsonObject, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
