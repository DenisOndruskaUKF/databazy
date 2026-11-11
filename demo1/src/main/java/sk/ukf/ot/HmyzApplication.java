package sk.ukf.ot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HmyzApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HmyzApplication.class.getResource("hmyz-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 520);
        stage.setTitle("Hmyz");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
