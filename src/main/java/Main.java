import com.alibaba.fastjson.JSONObject;

import javafx.scene.image.Image;
import utils.JsonUtils;
import utils.WindowsUtils;
import utils.OtherUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("page", "homePage");
        JsonUtils.setBuffer(buffer);
        if (buffer.get("userStatus").toString().equals(OtherUtils.encryptByMD5("true"))) {
            try {
                root = FXMLLoader.load(new File("src/main/java/ui/fxml/MainPage.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(new File("src/main/java/ui/fxml/Login.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert root != null;
        WindowsUtils.initial(root, stage);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("images/icon.png"));
        stage.setTitle("Writing Tracker");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();

    }
}


// "--add-opens"， "javafx.graphics/javafx.css=ALL-UNNAMED"，
