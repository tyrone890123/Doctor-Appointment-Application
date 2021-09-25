package ph.edu.dlsu;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DoctorMenuController implements Initializable {

    double xOffset, yOffset;
    @FXML
    public BorderPane borderpane;
    @FXML
    public Button exit, homeBtn, schedBtn, interactionBtn;
    @FXML
    public ImageView profilePic;
    @FXML
    public Label profileName, profileSpecialty;

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

        File file = new File("database/DoctorProfiles//" + user[0] + ".txt");
        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] line = new String[0];
        try {
            line = reader1.readLine().split("~");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Sets the profile picture in the home screen
        FileInputStream input = null;
        try {
            input = new FileInputStream(line[3]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        profilePic.setImage(image);
        // Sets the name of the doctor in the home screen
        profileName.setText(line[0]);
        // Sets the specialty of the doctor in the home screen
        profileSpecialty.setText(line[2]);
    }

    @FXML
    public void editProfile() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("editProfile.fxml"));
            Parent root = loader.load();

            Stage nextStage = new Stage();
            Scene scene = new Scene(root);
            nextStage.setTitle("Automated Appointment App: Edit Profile");
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

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @FXML
    public void checkup() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("checkup.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void schedule() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("schedule.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void interaction() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("interaction.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void risk() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("risk.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void exit() {
        System.exit(1);
    }

    @FXML
    public void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        Stage nextStage = new Stage();
        Scene scene = new Scene(root);
        nextStage.setTitle("Automated Appointment App: Login");
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

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nextStage.setX(mouseEvent.getScreenX() - xOffset);
                nextStage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();

        File file = new File("database/loggedIn.fxml");
        file.delete();
    }

    @FXML
    public void contactUs() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("aboutUs.fxml"));
        Parent root = loader.load();

        Stage nextStage = new Stage();
        Scene scene = new Scene(root);
        nextStage.setTitle("Automated Appointment App: About Us");
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

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nextStage.setX(mouseEvent.getScreenX() - xOffset);
                nextStage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
    }
}
