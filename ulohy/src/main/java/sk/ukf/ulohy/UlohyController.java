package sk.ukf.ulohy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class UlohyController {
    @FXML
    private ListView<Uloha> listView;
    @FXML
    private TextField nazov;
    @FXML
    private ComboBox<String> priorita, stav;
    @FXML
    private Button pridajButton, zmazButton, spustiButton;
    @FXML
    private TextArea vypis;

    private final ObservableList<Uloha> ulohy = FXCollections.observableArrayList();

    // Pri naplnani poli z vybranej ulohy nesmu listenery menit samotnu ulohu.
    private boolean nacitavanie = false;

    @FXML
    private void initialize() {
        priorita.getItems().addAll("nízka", "stredná", "vysoká");
        stav.getItems().addAll(Uloha.ROZPRACOVANA, Uloha.HOTOVA);
        priorita.getSelectionModel().selectFirst();
        stav.getSelectionModel().selectFirst();

        ulohy.addAll(
                new Uloha("Dokončiť projekt", "vysoká", Uloha.ROZPRACOVANA),
                new Uloha("Pripraviť prezentáciu", "stredná", Uloha.ROZPRACOVANA),
                new Uloha("Opraviť chyby v aplikácii", "vysoká", Uloha.HOTOVA),
                new Uloha("Skontrolovať zadania", "nízka", Uloha.ROZPRACOVANA),
                new Uloha("Nahrať projekt na GitHub", "stredná", Uloha.ROZPRACOVANA)
        );
        listView.setItems(ulohy);

        zmazButton.setDisable(true);

        // Vyber ulohy -> hodnoty sa zobrazia v poliach.
        listView.getSelectionModel().selectedItemProperty().addListener((obs, stara, uloha) -> {
            nacitavanie = true;
            if (uloha != null) {
                nazov.setText(uloha.getNazov());
                priorita.setValue(uloha.getPriorita());
                stav.setValue(uloha.getStav());
                pridajButton.setDisable(true);
                zmazButton.setDisable(false);
            } else {
                nazov.clear();
                priorita.getSelectionModel().selectFirst();
                stav.getSelectionModel().selectFirst();
                pridajButton.setDisable(false);
                zmazButton.setDisable(true);
            }
            nacitavanie = false;
        });

        // Editacia priamo vo vybranej polozke: kazda zmena v poli sa hned prejavi v Uloha.
        nazov.textProperty().addListener((obs, stara, nova) -> upravVybranu());
        priorita.valueProperty().addListener((obs, stara, nova) -> upravVybranu());
        stav.valueProperty().addListener((obs, stara, nova) -> upravVybranu());

        listView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) zmaz();
            else if (e.getCode() == KeyCode.ESCAPE) listView.getSelectionModel().clearSelection();
        });

        nazov.setOnAction(e -> {
            if (listView.getSelectionModel().getSelectedItem() == null) pridaj();
        });
    }

    private void upravVybranu() {
        Uloha uloha = listView.getSelectionModel().getSelectedItem();
        if (nacitavanie || uloha == null) return;

        if (!nazov.getText().isBlank()) {
            uloha.setNazov(nazov.getText().trim());
        }
        uloha.setPriorita(priorita.getValue());
        uloha.setStav(stav.getValue());

        // Atributy Uloha su obycajne Stringy, ObservableList sa o ich zmene nedozvie.
        listView.refresh();
    }

    @FXML
    private void pridaj() {
        String text = nazov.getText();
        if (text.isBlank()) return;

        ulohy.add(new Uloha(text.trim(), priorita.getValue(), stav.getValue()));

        nazov.clear();
        nazov.requestFocus();
    }

    @FXML
    private void zmaz() {
        Uloha uloha = listView.getSelectionModel().getSelectedItem();
        if (uloha == null) return;

        ulohy.remove(uloha);
        listView.getSelectionModel().clearSelection();
    }

    @FXML
    private void spustiVlakna() {
        Thread kontrola = new Thread(new PeriodickaUloha(3000, this::skontrolujRozpracovane));
        Thread mazanie = new Thread(new PeriodickaUloha(10000, this::odstranHotove));

        // daemon vlakna sa ukoncia spolu s aplikaciou
        kontrola.setDaemon(true);
        mazanie.setDaemon(true);

        kontrola.start();
        mazanie.start();

        spustiButton.setDisable(true);
    }

    // Vola sa kazdu 3. sekundu (v JavaFX vlakne).
    private void skontrolujRozpracovane() {
        int pocet = 0;
        for (Uloha uloha : ulohy) {
            if (uloha.getStav().equals(Uloha.ROZPRACOVANA)) {
                pocet++;
            }
        }
        vypis.appendText("Počet rozpracovaných úloh: " + pocet + "\n");
    }

    // Vola sa kazdych 10 sekund (v JavaFX vlakne).
    private void odstranHotove() {
        Uloha vybrana = listView.getSelectionModel().getSelectedItem();
        if (vybrana != null && vybrana.getStav().equals(Uloha.HOTOVA)) {
            listView.getSelectionModel().clearSelection();
        }

        boolean odstranene = ulohy.removeIf(uloha -> uloha.getStav().equals(Uloha.HOTOVA));
        if (odstranene) {
            vypis.appendText("Zmazané dokončené úlohy\n");
        }
    }
}
