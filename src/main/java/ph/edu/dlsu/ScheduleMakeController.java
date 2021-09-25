package ph.edu.dlsu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ScheduleMakeController {

    @FXML
    public Button exit, save;
    @FXML
    public TextField timeIn, timeOut;
    @FXML
    public ComboBox intervalChoose;

    ObservableList<String> list = FXCollections.observableArrayList("30", "45", "60");
    public void makeSchedHere(int value, String date) throws IOException {
        Doctors ref = new Doctors();
        String myDate = date;
        String[] accounts = ref.getAccounts();
        String[] getDir=accounts[value].split(" ");

        File myObj = new File("database/Doctor//"+getDir[0].trim()+"//"+myDate+".txt");
        File myObj2 = new File("database/Patient//"+getDir[0].trim()+"//"+myDate+".txt");

        intervalChoose.setItems(list);

        save.setOnAction(e -> {
            try {
                FileWriter writer = new FileWriter(myObj);
                writer.write(intervalChoose.getValue()+"\n"+timeIn.getText()+"\n"+timeOut.getText());
                writer.close();

                FileWriter writer2 = new FileWriter(myObj2);
                writer2.write(intervalChoose.getValue()+"\n"+timeIn.getText()+"\n"+timeOut.getText());
                writer2.close();

                ref.scheduler(myObj,myObj2);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule.fxml"));
            try {
                Parent root = loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            ScheduleController scheduleController = loader.getController();
            scheduleController.table(myObj, myObj2);

            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });


    }


    @FXML
    public void exit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
