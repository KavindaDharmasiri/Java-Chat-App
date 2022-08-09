package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientFormController {
    public static String name;
    public static Stage stage;
    public TextField txtClientMessage;
    public TextFlow txtFlow;
    public ScrollPane scroll;
    Socket socket = null;

    public void initialize() throws IOException {
        stage.close();
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                InputStreamReader inputStreamReader =
                        new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String record = bufferedReader.readLine();
                System.out.println(record);
                Text t = new Text("Sameera : "+record+"\n");

                Platform.runLater(()->txtFlow.getChildren().addAll(t));
                scroll.setVvalue(1.0);

                while (true) {
                    if (!record.equals("exit")) {
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                        String record1 = bufferedReader1.readLine();
                        System.out.println(record1);
                        Text t1 = new Text("Sameera : "+record1+"\n");
                        Platform.runLater(()->txtFlow.getChildren().addAll(t1));
                        scroll.setVvalue(1.0);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException {
        /* new message*/
        System.out.println(name);
        Text t = new Text(name+" : "+txtClientMessage.getText()+"\n");
        txtFlow.getChildren().addAll(t);
        scroll.setVvalue(1.0);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(name+" : "+txtClientMessage.getText());
        printWriter.flush();
        txtClientMessage.setText("");
    }

    public void getPhoto(MouseEvent event) throws IOException {
        socket = new Socket("localhost", 5000);

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter exxtFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter exxtFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(exxtFilterJPG, exxtFilterPNG);
        File file = chooser.showOpenDialog(null);
        BufferedImage bufferedImage = ImageIO.read(file);
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage,null);

        ImageView imageView = new ImageView(image);
        Text t = new Text(name+" : ");
        Text t1 = new Text("\n");
        txtFlow.getChildren().addAll( t,imageView , t1 );

        try{
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
            ImageIO.write(bufferedImage , "jpg" , bufferedOutputStream);

            bufferedOutputStream.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
