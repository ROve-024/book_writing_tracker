import com.alibaba.fastjson.JSONObject;
import controller.utlis.JsonUtils;
import controller.utlis.InitialWindows;
import controller.utlis.Utils;
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

        JSONObject user = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json");
        if (user.get("status").toString().equals(Utils.encryptByMD5("true"))) {
            try {
                root = FXMLLoader.load(new File("src/main/java/view/MainPage.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(new File("src/main/java/view/Login.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert root != null;
        InitialWindows.initial(root, stage);
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();


    }
}
