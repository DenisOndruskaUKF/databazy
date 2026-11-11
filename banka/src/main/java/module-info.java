module sk.ukf.banka {
    requires javafx.controls;
    requires javafx.fxml;

    opens sk.ukf.banka to javafx.fxml;
    exports sk.ukf.banka;
}
