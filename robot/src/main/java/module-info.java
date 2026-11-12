module sk.ukf.robot {
    requires javafx.controls;
    requires javafx.fxml;

    opens sk.ukf.robot to javafx.fxml;
    exports sk.ukf.robot;
}
