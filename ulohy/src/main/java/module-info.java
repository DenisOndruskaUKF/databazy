module sk.ukf.ulohy {
    requires javafx.controls;
    requires javafx.fxml;

    opens sk.ukf.ulohy to javafx.fxml;
    exports sk.ukf.ulohy;
}
