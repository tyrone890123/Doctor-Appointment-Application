package ph.edu.dlsu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;


public class AboutUsController {

    @FXML
    public Button exit;

    @FXML
    public void exit() {
        System.exit(1);
    }
}
