package ph.edu.dlsu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DiagnosisController implements Initializable {

    @FXML
    public Button save;
    @FXML
    public TableView table;
    @FXML
    public TableColumn c1, c2, c3;
    @FXML
    public TextArea diagnosisBox;
    @FXML
    public Group group;
    @FXML
    public ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Person> data = FXCollections.observableArrayList(new Person("12:00", "Joshua", "Ded"));
        System.out.println(data);
        c1.setCellValueFactory(new PropertyValueFactory<Person, String>("time"));
        c1.setCellFactory(TextFieldTableCell.forTableColumn());

        c2.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        c2.setCellFactory(TextFieldTableCell.forTableColumn());

        c3.setCellValueFactory(new PropertyValueFactory<Person, String>("symptoms"));
        c3.setCellFactory(TextFieldTableCell.forTableColumn());

        table.setItems(data);
        table.getColumns().addAll(c1, c2, c3);

    }


}
