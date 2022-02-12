package utils;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 初始化应用窗口时使用，如拖动窗口
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class InitialWindows {
    static double x = 0;
    static double y = 0;

    /**
     * 使用md5对数据进行加密
     *
     * @param root  fxml文件的根节点
     * @param stage 应用窗口，顶级容器
     */
    public static void initial(Parent root, Stage stage) {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        });

    }
}
