package utils;

import javafx.event.EventHandler;
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

    /**
     * 初始化日期选择器
     *
     * @param datepicker 需要初始化的对象
     */
    public static void datepickerInitial(DatePicker datepicker){
        String pattern = "yyyy-MM-dd";
        StringConverter<LocalDate> converter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter =DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datepicker.setConverter(converter);
        datepicker.setPromptText(pattern.toLowerCase());

    }
}
