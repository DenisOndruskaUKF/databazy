package sk.ukf.robot;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

public class RobotController {
    @FXML
    private Pane plocha;
    @FXML
    private ComboBox<String> typRobota;
    @FXML
    private Button pridajButton, pohybButton, obratButton;

    private boolean vsetkoZastavene = false;

    @FXML
    private void initialize() {
        typRobota.getItems().addAll("Humanoid", "Dron", "Rover");
        typRobota.getSelectionModel().selectFirst();

        // Ovladacie prvky nebudu brat focus, aby robot po kliknuti na tlacidlo
        // nestratil focus (a cerveny ramik).
        typRobota.setFocusTraversable(false);
        pridajButton.setFocusTraversable(false);
        pohybButton.setFocusTraversable(false);
        obratButton.setFocusTraversable(false);
    }

    @FXML
    private void pridaj() {
        Robot robot;
        switch (typRobota.getValue()) {
            case "Humanoid":
                robot = new Humanoid();
                break;
            case "Dron":
                robot = new Dron();
                break;
            case "Rover":
                robot = new Rover();
                break;
            default:
                return;
        }

        plocha.getChildren().add(robot);

        // jeden krok hned, aby sa robot objavil na svojom mieste a nie v lavom hornom rohu
        robot.krok();

        // novy robot sa rozbehne iba ak pohyb vsetkych nie je zastaveny
        if (!vsetkoZastavene) {
            robot.startuj();
        }
    }

    @FXML
    private void prepniPohyb() {
        vsetkoZastavene = !vsetkoZastavene;

        for (Node node : plocha.getChildren()) {
            if (node instanceof Robot robot) {
                if (vsetkoZastavene) {
                    robot.zastav();
                } else {
                    robot.startuj();
                }
            }
        }

        if (vsetkoZastavene) {
            pohybButton.setText("Spusti všetkých");
        } else {
            pohybButton.setText("Zastav všetkých");
        }
    }

    @FXML
    private void obratSmer() {
        String typ = typRobota.getValue();

        for (Node node : plocha.getChildren()) {
            // getClass() ukaze, aky konkretny potomok to naozaj je
            if (node instanceof Robot robot && robot.getClass().getSimpleName().equals(typ)) {
                robot.obratSmer();
            }
        }
    }
}
