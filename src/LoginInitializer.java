import controller.LoginFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

/**
 * @Created_By_: Kavinda Gimhan
 * @Date_: 8/4/2022
 * @Time_: 2:39 PM
 * @Project_Name : live Chat
 **/

public class LoginInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setScene
                (new Scene(FXMLLoader.load(getClass().getResource("interfaces/LoginForm.fxml"))));
        LoginFormController.stage1 = primaryStage;
        primaryStage.show();
    }
}
