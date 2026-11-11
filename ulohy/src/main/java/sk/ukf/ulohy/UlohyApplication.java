package sk.ukf.ulohy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UlohyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UlohyApplication.class.getResource("ulohy-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 420);
        stage.setTitle("Zoznam úloh");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
