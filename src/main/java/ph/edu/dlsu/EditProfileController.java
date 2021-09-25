package ph.edu.dlsu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    public static Stage nextStage;
    double xOffset, yOffset;
    @FXML
    public ComboBox specialty;
    @FXML
    public TextField setupName, setupAge, fileDir;
    @FXML
    public Button profileImageBtn, setProfileBtn, exit;

    ObservableList<String> listBox = FXCollections.observableArrayList(
            "Family Physician",
            "Internal Medicine Physician",
            "Pediatrician",
            "Obstetrician/Gynecologist (OB/GYN)",
            "Surgeon",
            "Psychiatrist",
            "Cardiologist",
            "Dermatologist",
            "Endocrinologist",
            "Gastroenterologist",
            "Infectious Disease Physician",
            "Nephrologist",
            "Ophthalmologist",
            "Otolaryngologist",
            "Pulmonologist",
            "Neurologist",
            "Physician Executive",
            "Radiologist",
            "Anesthesiologist",
            "Oncologist"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File list = new File("database/loggedIn.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(list));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] user = null;
        try {
            user = reader.readLine().split(" ");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }



        File file = new File("database/DoctorProfiles//"+ user[0] +".txt");
        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] line= new String[0];
        try {
            line = reader1.readLine().split("~");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        specialty.setItems(listBox);
        setupName.setText(line[0]);
        setupAge.setText(line[1]);
        specialty.setValue(line[2]);
        fileDir.setText(line[3]);

        setProfileBtn.setOnAction(e -> {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(setupName.getText() + "~" + setupAge.getText() + "~" + specialty.getValue() + "~" + fileDir.getText());
                writer.close();

                Parent root = FXMLLoader.load(getClass().getResource("doctormenu.fxml"));
                Stage nextStage = new Stage();
                Scene scene = new Scene(root);
                nextStage.setTitle("Automated Appointment App: Doctor");
                nextStage.initStyle(StageStyle.TRANSPARENT);
                nextStage.setScene(scene);
                scene.setFill(Color.TRANSPARENT);
                nextStage.show();

                Stage stage = (Stage) exit.getScene().getWindow();
                stage.close();

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        xOffset = mouseEvent.getSceneX();
                        yOffset = mouseEvent.getSceneY();
                    }
                });

                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        nextStage.setX(mouseEvent.getScreenX() - xOffset);
                        nextStage.setY(mouseEvent.getScreenY() - yOffset);
                    }
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    @FXML
    public void setImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(nextStage);
        fileDir.clear();
        fileDir.setText(selectedFile.getAbsolutePath());
        ImageView profileImage = new ImageView(selectedFile.toURI().toString());
        profileImage.setFitHeight(75);
        profileImage.setFitWidth(75);
        profileImage.setPreserveRatio(true);
        profileImageBtn.setGraphic(profileImage);
        profileImageBtn.setText("");

    }

    @FXML
    public void exit() {
        System.exit(1);
    }

}
