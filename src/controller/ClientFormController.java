package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
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
    Socket socket1 = null;

    public void initialize() throws IOException {
        stage.close();

        new Thread(() -> {
            try {
                socket1 = new Socket("localhost", 5001);

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
                socket = new Socket("localhost", 5000);
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String record = bufferedReader.readLine();

                setText(record);

                while (true) {
                    if (!record.equals("exit")) {
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                        String record1 = bufferedReader1.readLine();

                        setText(record1);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    private void setText(String record) {
        Text t = new Text("Sameera \t: ");
        Text t1 = new Text(record + "\n\n");

        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: green;");
        t1.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: black;");

        Platform.runLater(() -> txtFlow.getChildren().addAll(t, t1));
        scroll.setVvalue(1.0);
        return;
    }

    private void setImg(Socket socket1) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(socket1.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String record = bufferedReader.readLine();
        System.out.println(record);

        InputStream inputStream = socket1.getInputStream();

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
        Text t12 = new Text("Sameera" + " \t: ");

        t12.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: green;");
        Platform.runLater(() -> txtFlow.getChildren().addAll(t12, imageView, t1));
        scroll.setVvalue(1.0);
        return;
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException {

        Text t1 = new Text(name + " \t: ");
        Text t = new Text(txtClientMessage.getText() + "\n\n");
        t1.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: blue;");
        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: black;");
        txtFlow.getChildren().addAll(t1, t);
        scroll.setVvalue(1.0);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(name + " : " + txtClientMessage.getText());
        printWriter.flush();
        txtClientMessage.setText("");
    }

    public void getPhoto(MouseEvent event) throws IOException, InterruptedException {
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
        System.out.println("Flushed: " + System.currentTimeMillis());

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(200);
        imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Text t = new Text(name + " \t: ");

        Text t1 = new Text("\n\n");
        t.setStyle("-fx-font: normal bold 15px 'serif'; -fx-font-style:bold;-fx-stroke-with: 10px;-fx-fill: blue;");
        txtFlow.getChildren().addAll(t, imageView, t1);
        scroll.setVvalue(1.0);

    }
}
