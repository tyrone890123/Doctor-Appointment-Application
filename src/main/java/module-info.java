module ph.edu.dlsu {
    requires javafx.controls;
    requires javafx.fxml;

    opens ph.edu.dlsu to javafx.fxml;
    exports ph.edu.dlsu;
}