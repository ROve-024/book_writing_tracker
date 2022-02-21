package utils;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 初始化应用窗口时使用，如拖动窗口
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class WindowsUtils {
    static double x = 0;
    static double y = 0;

    /**
     * 初始化窗口，拖拽行为
     *
     * @param root  fxml文件的根节点
     * @param stage 应用窗口，顶级容器
     */
    public static void initial(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

    }

    /**
     * 初始化日期选择器
     *
     * @param datePicker 需要初始化的对象
     */
    public static void datepickerInitial(DatePicker datePicker){
        datePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }

    /**
     * 初始化导航栏
     *
     * @param homepageButton 主页按钮
     * @param myTaskButton 我的任务中心
     * @param recycleBinButton 回收站
     */
    public static void toolBarInitial(JFXButton homepageButton, JFXButton myTaskButton, JFXButton recycleBinButton){
        JSONObject buffer = JsonUtils.getBuffer();
        String page = buffer.get("page").toString();
        if (page.equals("homePage")){
            homepageButton.setStyle("-fx-text-fill: #eeeeee; -fx-font-size: 21px; -fx-background-color:  #323838");
        } else if (page.equals("myTask")){
            myTaskButton.setStyle("-fx-text-fill: #eeeeee; -fx-font-size: 21px; -fx-background-color:  #323838");
        } else {
            recycleBinButton.setStyle("-fx-text-fill: #eeeeee; -fx-font-size: 21px; -fx-background-color:  #323838");
        }
    }

}
