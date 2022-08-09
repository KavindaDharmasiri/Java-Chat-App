package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFormController {
    public TextField TXTServerMessage;
    public ScrollPane scroll;
    public TextFlow txtFlow;
    Socket accept=null;

    public void initialize() {
        new Thread(() -> {
            try {

                ServerSocket serverSocket= new ServerSocket(5000);
                System.out.println("Server started!");
                accept= serverSocket.accept();
                System.out.println("Client Connected!");
                InputStreamReader inputStreamReader = new InputStreamReader(accept.getInputStream(),"UTF8");

                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                String record= bufferedReader.readLine();
                System.out.println(record);

                Text t = new Text(record+"\n");

                Platform.runLater(()->txtFlow.getChildren().addAll(t));
                scroll.setVvalue(1.0);

                while (true) {
                    if (!record.equals("exit")) {

                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                        String record1 = bufferedReader1.readLine();
                        System.out.println(record1);

                        Text t1 = new Text(record1+"\n");

                        Platform.runLater(()->txtFlow.getChildren().addAll(t1));
                        scroll.setVvalue(1.0);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        Text t = new Text("Sameera : "+TXTServerMessage.getText()+"\n");

        Platform.runLater(()->txtFlow.getChildren().addAll(t));
        scroll.setVvalue(1.0);

        PrintWriter printWriter= new PrintWriter(accept.getOutputStream());
        printWriter.println(TXTServerMessage.getText());
        printWriter.flush();
        TXTServerMessage.setText("");
    }

    public void uploadImg(MouseEvent event) {
    }
}
