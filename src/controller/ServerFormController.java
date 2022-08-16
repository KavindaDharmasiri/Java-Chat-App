package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ServerFormController {
    public static String name = null;
    public TextField TXTServerMessage;
    public ScrollPane scroll;
    public TextFlow txtFlow;
    Socket accept = null;
    Socket socket1 = null;
    int z, x = -1;

    public void initialize() {
        System.out.println("awaoo");
        setClient();
    }

    private void setClient() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(5001);
                socket1 = serverSocket.accept();

                setImg(socket1);
                while (true) {

                    setImg(socket1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {

                ServerSocket serverSocket = new ServerSocket(5000);

                System.out.println("Server started!");
                accept = serverSocket.accept();

                System.out.println("Client Connected!");
                InputStreamReader inputStreamReader = new InputStreamReader(accept.getInputStream(), StandardCharsets.UTF_8);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String record = bufferedReader.readLine();
                System.out.println(record);

                textSave(record);


                while (true) {
                    if (!record.equals("exit")) {
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                        String record1 = bufferedReader1.readLine();
                        System.out.println(record1);

                        textSave(record1);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setImg(Socket socket) throws IOException {

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String record = bufferedReader.readLine();

        InputStream inputStream = socket.getInputStream();

        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        inputStream.read(imageAr);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageAr));

        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(200);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Text t1 = new Text("\n\n");
        Text t12 = new Text(record + " \t: ");

        t12.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: blue;");
        Platform.runLater(() -> txtFlow.getChildren().addAll(t12, imageView, t1));
        scroll.setVvalue(1.0);
        return;
    }

    private void textSave(String rec1) {
        String name = rec1.split(":")[0];
        String record = rec1.split(":")[1];

        Text t1 = new Text(name + ": ");
        Text t = new Text(record + "\n\n");

        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: black;");
        t1.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: blue;");

        Platform.runLater(() -> txtFlow.getChildren().addAll(t1, t));
        scroll.setVvalue(1.0);
        return;
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        Text t = new Text("Sameera \t: ");
        Text t1 = new Text(TXTServerMessage.getText() + "\n\n");

        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: green;");
        t1.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: black;");

        Platform.runLater(() -> txtFlow.getChildren().addAll(t, t1));
        scroll.setVvalue(1.0);

        PrintWriter printWriter = new PrintWriter(accept.getOutputStream());
        printWriter.println(TXTServerMessage.getText());
        printWriter.flush();
        TXTServerMessage.setText("");
    }

    public void uploadImg(MouseEvent event) throws IOException {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter exxtFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter exxtFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(exxtFilterJPG, exxtFilterPNG);
        File file = chooser.showOpenDialog(null);
        BufferedImage bufferedImage = ImageIO.read(file);
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);

        OutputStream outputStream = socket1.getOutputStream();

        PrintWriter printWriter = new PrintWriter(socket1.getOutputStream());
        printWriter.println(name);
        printWriter.flush();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(200);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Text t = new Text("Sameera" + " \t: ");
        Text t1 = new Text("\n\n");
        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: green;");
        txtFlow.getChildren().addAll(t, imageView, t1);
        scroll.setVvalue(1.0);
    }
}
