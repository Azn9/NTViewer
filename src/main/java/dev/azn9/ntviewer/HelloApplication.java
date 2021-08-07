package dev.azn9.ntviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);

        HelloController controller = fxmlLoader.getController();

        stage.setTitle("DataViewer");
        stage.setScene(scene);
        stage.show();

        controller.start(this);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}