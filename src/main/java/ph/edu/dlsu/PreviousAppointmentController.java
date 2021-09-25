package ph.edu.dlsu;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PreviousAppointmentController implements Initializable {

    double xOffset, yOffset;
    @FXML
    public BorderPane borderpane;
    @FXML
    public ScrollPane scrollPane, scrollPane1;
    @FXML
    public Group group, group1;
    @FXML
    public Button exit;
    @FXML
    public ImageView profilePic;
    @FXML
    public Label profileName, age, weight, height;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File list = new File("database/loggedInP.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(list));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] user = null;
        try {
            assert reader != null;
            user = reader.readLine().split(" ");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assert user != null;
        File file = new File("database/PatientProfiles//" + user[0] + "//profile.txt");
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
            input = new FileInputStream(line[4]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        profilePic.setImage(image);
        // Sets the name of the doctor in the home screen
        profileName.setText(line[0]);
        age.setText("Age: " + line[1]);
        weight.setText("Weigth: " + line[2] + "kg");
        height.setText("Height: " + line[3] + "cm");

        File file1 = new File("database/PatientProfiles//"+user[0]);
        File[] listOfFiles = file1.listFiles();

        for(int i=0;i<listOfFiles.length;i++){
            if(!listOfFiles[i].getName().equals("profile.txt")){
                String[] holder=listOfFiles[i].getName().split("[.]");
                Button btn = new Button(holder[0]);
                btn.setLayoutX(0);
                btn.setLayoutY(55*i);
                btn.setPrefSize(195,25);

                int finalI = i;
                btn.setOnAction(e -> {
                    try {
                        group1.getChildren().clear();
                        checkDiagnosis(listOfFiles[finalI]);
                    } catch (Exception z) {

                    }
                });
                group.getChildren().add(btn);
            }
            scrollPane.setContent(group);
        }
    }

    private void checkDiagnosis(File file) throws IOException {
        Doctors ref = new Doctors();

        try {
            String[] line = ref.checksched(file).split("\n");
            for (int i = 0; i < line.length; i++) {
                Label btn = new Label(line[i]);
                btn.setLayoutX(0);
                btn.setLayoutY(0 + (i * 10));
                btn.setPrefSize(195, 25);
                group1.getChildren().add(btn);
            }
            scrollPane1.setContent(group1);
        } catch (Exception z) {
            ref.errorscreen();
        }
    }
    
    @FXML
    public void home() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("patientmenu.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void appointment() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("appointment.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void exit() {
        System.exit(1);
    }

    @FXML
    public void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPatient.fxml"));
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

        File file = new File("database/loggedInP.fxml");
        file.delete();
    }
}
