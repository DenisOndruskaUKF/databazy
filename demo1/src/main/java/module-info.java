module sk.ukf.ot {
    requires javafx.controls;
    requires javafx.fxml;

    opens sk.ukf.ot to javafx.fxml;
    exports sk.ukf.ot;
}
