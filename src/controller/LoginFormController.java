package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

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

        ClientFormController.stage = stage1;

        Parent load = FXMLLoader.load(getClass().getResource("../interfaces/ClientForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        ClientFormController.name = txtUserName.getText();
        ServerFormController.name = txtUserName.getText();
        stage.setScene(scene);
        stage.setTitle("Client");

        stage.show();


    }
}
