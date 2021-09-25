package ph.edu.dlsu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    public static Stage nextStage;
    double xOffset, yOffset;
    @FXML
    public VBox leftPanel;
    @FXML
    public HBox topPanel;
    @FXML
    public ImageView profilePic;
    @FXML
    public AnchorPane anchor1, setupPane;
    @FXML
    public Label helloName, helloName1, status;
    @FXML
    public TextField setupName, setupAge, fileDir;
    @FXML
    public ComboBox specialty;
    @FXML
    public Button setProfileBtn, exit, profileImageBtn;


    ObservableList<String> list = FXCollections.observableArrayList(
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
        specialty.setItems(list);
    }

    @FXML
    public void exit(ActionEvent event) {
        System.exit(1);
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
    public void setProfile(ActionEvent event) throws IOException {
        File list = new File("database/loggedIn.txt");
        BufferedReader reader = new BufferedReader(new FileReader(list));
        String[] user = reader.readLine().split(" ");
        System.out.println(user);

        File file = new File("database/DoctorProfiles//" + user[0] + ".txt");
        File file2 = new File("database/Doctor//" + user[0]);
        File file3 = new File("database/Patient//" + user[0]);
        //Doctors ref = new Doctors();

        if(file.createNewFile() && file2.mkdir() && file3.mkdir()) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(setupName.getText() + "~" + setupAge.getText() + "~" + specialty.getValue() + "~" + fileDir.getText());
                writer.close();

                // Please check this
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
        }
        else {
            //LOAD THE NEXT FXML FILE
            System.out.println("Check");


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


        }
    }



}
