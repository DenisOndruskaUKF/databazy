package sk.ukf.banka;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BankaController {
    @FXML
    private TextField pociatocnyZostatok, suma;
    @FXML
    private Label zostatokLabel;
    @FXML
    private ComboBox<String> typ;
    @FXML
    private TextArea transakcie;
    @FXML
    private Button runOneButton, runFiveButton;

    private BankovyUcet ucet;

    @FXML
    private void initialize() {
        typ.getItems().addAll("Vklad", "Výber");
        typ.getSelectionModel().selectFirst();

        // kym neexistuje ucet, nie je co robit
        runOneButton.setDisable(true);
        runFiveButton.setDisable(true);
    }

    @FXML
    private void createAccount() {
        try {
            double zostatok = nacitajSumu(pociatocnyZostatok.getText());

            ucet = new BankovyUcet(zostatok);
            transakcie.clear();
            zapis(String.format("Účet vytvorený so zostatkom %.2f €", zostatok));

            runOneButton.setDisable(false);
            runFiveButton.setDisable(false);
        } catch (NumberFormatException e) {
            zapis("Neplatný počiatočný zostatok.");
        }
    }

    @FXML
    private void runOne() {
        try {
            double hodnota = nacitajSumu(suma.getText());
            String vybranyTyp = typ.getValue();

            Zamestnanec zamestnanec = new Zamestnanec(ucet, vybranyTyp, hodnota, this::zapis);

            Thread vlakno = new Thread(zamestnanec);
            vlakno.start();
        } catch (NumberFormatException e) {
            zapis("Neplatná suma.");
        }
    }

    @FXML
    private void runFive() {
        for (int i = 0; i < 5; i++) {
            String nahodnyTyp = Math.random() < 0.5 ? "Vklad" : "Výber";
            double nahodnaSuma = 10 + (int) (Math.random() * 191);   // 10 - 200

            Zamestnanec zamestnanec = new Zamestnanec(ucet, nahodnyTyp, nahodnaSuma, this::zapis);

            // kazdy zamestnanec ma svoje vlakno
            Thread vlakno = new Thread(zamestnanec);
            vlakno.start();
        }
    }

    // Vola sa aj z pracovnych vlakien, preto zmeny GUI posielame cez Platform.runLater().
    private void zapis(String text) {
        Platform.runLater(() -> {
            transakcie.appendText(text + "\n");
            if (ucet != null) {
                zostatokLabel.setText(String.format("%.2f €", ucet.getZostatok()));
            }
        });
    }

    private double nacitajSumu(String text) {
        double hodnota = Double.parseDouble(text.trim().replace(",", "."));
        if (hodnota < 0) {
            throw new NumberFormatException("Suma nesmie byť záporná.");
        }
        return hodnota;
    }
}
