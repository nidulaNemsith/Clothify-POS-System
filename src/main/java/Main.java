import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    public static void main(String[] args) {

        launch();

    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/open-page-form.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/logo.png")));
            stage.getIcons().add(image);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}