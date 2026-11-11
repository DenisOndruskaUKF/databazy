package sk.ukf.ot;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class HmyzController {
    @FXML
    private Pane plocha;

    @FXML
    private void initialize() {
        // Scena este v initialize() neexistuje, preto handler klavesnice nastavime az neskor.
        Platform.runLater(() -> plocha.getScene().setOnKeyPressed(this::spracujKlaves));
    }

    @FXML
    private void pridajMuchu() {
        double x = Math.random() * (plocha.getWidth() - Mucha.VELKOST);
        double y = Math.random() * (plocha.getHeight() - Mucha.VELKOST);

        plocha.getChildren().add(new Mucha(x, y));
    }

    @FXML
    private void pridajChrobaka() {
        double x = Math.random() * (plocha.getWidth() - Chrobak.VELKOST);
        double y = plocha.getHeight() - Chrobak.VELKOST;   // pri spodnom okraji

        plocha.getChildren().add(new Chrobak(x, y));
    }

    private void spracujKlaves(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                // vsetok hmyz sa posunie o 50 px nahor a pokracuje v pohybe
                for (Node node : plocha.getChildren()) {
                    if (node instanceof Hmyz hmyz) {
                        hmyz.posunNahor(50);
                    }
                }
                break;
            case S:
                // zastavime iba muchy, chrobaky pokracuju
                for (Node node : plocha.getChildren()) {
                    if (node instanceof Mucha mucha) {
                        mucha.zastav();
                    }
                }
                break;
        }
    }
}
