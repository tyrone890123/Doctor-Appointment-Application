package ph.edu.dlsu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public class SetupPatientController {

    public static Stage nextStage;
    double xOffset, yOffset;
    @FXML
    public TextField setupName, setupAge, setupWeight, setupHeight, fileDir;
    @FXML
    public Button profileImageBtn, exit;

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
    public void setProfileP(ActionEvent event) throws IOException {
        File list = new File("database/loggedInP.txt");
        BufferedReader reader = new BufferedReader(new FileReader(list));
        String[] user = reader.readLine().split(" ");
        System.out.println(user);



        File file = new File("database/PatientProfiles//" + user[0]);
        File actualProfile = new File("database/PatientProfiles//" + user[0] + "//profile.txt");


        if(file.mkdir()) {
            try {
                FileWriter writer = new FileWriter(actualProfile);
                writer.write(setupName.getText() + "~" + setupAge.getText() + "~" + setupWeight.getText() + "~" + setupHeight.getText() + "~" + fileDir.getText());
                writer.close();


                // Please check this
                Parent root = FXMLLoader.load(getClass().getResource("patientmenu.fxml"));
                Stage nextStage = new Stage();
                Scene scene = new Scene(root);
                nextStage.setTitle("Automated Appointment App: Patient");
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

            Parent root = FXMLLoader.load(getClass().getResource("patientmenu.fxml"));
            Stage nextStage = new Stage();
            Scene scene = new Scene(root);
            nextStage.setTitle("Automated Appointment App: Patient");
            nextStage.initStyle(StageStyle.TRANSPARENT);
            nextStage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            nextStage.show();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                }
            });

            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();

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
