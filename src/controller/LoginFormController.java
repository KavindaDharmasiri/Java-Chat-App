package controller;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.applet.Applet;
import java.applet.AudioClip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;

/**
 * @Created_By_: Kavinda Gimhan
 * @Date_: 8/4/2022
 * @Time_: 2:08 PM
 * @Project_Name : live Chat
 **/

public class LoginFormController {
    public static Stage stage1;
    public TextField txtUserName;
    public Button loginBtn;
    public AnchorPane loginApane;

    public void logInUser(MouseEvent event) throws IOException {

        if(!txtUserName.getText().equals("")) {
            ClientFormController.stage = stage1;
            Parent load = FXMLLoader.load(getClass().getResource("../interfaces/ClientForm.fxml"));
            Scene scene = new Scene(load);
            Stage stage = new Stage();
            ClientFormController.name = txtUserName.getText();
            ServerFormController.name = txtUserName.getText();
            stage.setScene(scene);
            stage.setTitle("Client");
            stage.show();
        }else{
            notification();
        }


    }

    private void notification() {
        AudioClip clip = Applet.newAudioClip(getClass().getResource("/assest/audio/success-notification-alert_A_major.wav"));

        Image image = new Image("/assest/img/icons8-cancel-30.png");
        Notifications notifications = Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text("Please Enter Your Name First..");
        notifications.title("Error Massage");
        notifications.hideAfter(Duration.seconds(5));
        notifications.darkStyle();
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();

        clip.play();
    }
}
