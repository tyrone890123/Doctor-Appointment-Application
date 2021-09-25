package ph.edu.dlsu;

import java.io.*;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    double xOffset, yOffset;
    @FXML
    public Button exit,loginBtn, imapatientBtn;
    @FXML
    public TextField nameField, passwordField, nameFieldReg, passwordFieldReg, passwordFieldReg1;
    @FXML
    public Label status, statusReg, helloName;
    @FXML
    public AnchorPane loginPane, loginPane1, registerPane, registerPane1;

    @FXML
    public Button profileImageBtn, setProfileBtn;
    @FXML
    public ComboBox<String> specialty;
    @FXML
    public TextField setupName, setupAge;

    @FXML
    Doctors ref = new Doctors();

    @FXML
    public void exit() {
        System.exit(1);
    }
    
    @FXML
    private void login(ActionEvent actionEvent) {

        try {
            int holder = ref.login(nameField.getText(), passwordField.getText(), "database/doctor_list.txt");
            if(holder > - 1) {
                File list = new File("database/loggedIn.txt");
                FileWriter writer = new FileWriter(list);
                writer.write(nameField.getText() + " " + holder);
                writer.close();

                File list1 = new File("database/DoctorProfiles//" + nameField.getText() + ".txt");
                if(list1.exists()) {
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
                else {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("setup.fxml"));
                    loginPane.getChildren().setAll(pane);

                    Stage stage = (Stage) exit.getScene().getWindow();
                    stage.close();
                }

            }

            else {
                status.setText("Incorrect username/password.");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @FXML
    private void loginScreen(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("login.fxml"));
        registerPane.getChildren().setAll(pane);
    }

    @FXML
    private void loginScreen1(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("login.fxml"));
        loginPane1.getChildren().setAll(pane);
    }


    @FXML
    private void register(ActionEvent actionEvent) {
        File list = new File("database/doctor_list.txt");
        if(passwordFieldReg.getText().equals(passwordFieldReg1.getText())) {
            try {
                if(list.createNewFile()) {
                    FileWriter writer = new FileWriter(list);
                    writer.write(nameFieldReg.getText() + " " + passwordFieldReg.getText());
                    writer.close();
                    statusReg.setText("Account successfully created.");
                    nameFieldReg.clear();
                    passwordFieldReg.clear();
                    passwordFieldReg1.clear();
                }
                else {
                    BufferedReader reader = new BufferedReader(new FileReader(list));

                    String program = "";
                    String line = reader.readLine();
                    while(line != null) {
                        program = program + line + System.lineSeparator();
                        line = reader.readLine();
                    }

                    String[] accountsSep = program.split("\n");
                    HashMap<String, String> accounts = new HashMap<>(); //CHECK THIS IF PROGRAM ERROR
                    for(int q = 0; q < accountsSep.length; q++) {
                        String[] holder1 = accountsSep[q].split(" ");
                        accounts.put(holder1[0], holder1[1]);
                    }

                    int holder = 0;
                    if(accounts.containsKey(nameFieldReg.getText())) {
                        statusReg.setText("Account already exists.");
                        holder++;
                    }

                    if(holder == 0) {
                        FileWriter writer = new FileWriter(list);
                        writer.write(program + nameFieldReg.getText() + " " + passwordFieldReg.getText());
                        writer.close();
                        statusReg.setText("Account successfully created.");
                        nameFieldReg.clear();
                        passwordFieldReg.clear();
                        passwordFieldReg1.clear();
                    }
                    else {
                        statusReg.setText("Account already exists.");
                    }
                }
            } catch (Exception w) {

            }
        }
        else {
            statusReg.setText("Password does not match.");
        }

    }

    @FXML
    private void registerScreen(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("register.fxml"));
        loginPane.getChildren().setAll(pane);
    }

    @FXML
    private void registerScreen1(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("register.fxml"));
        registerPane1.getChildren().setAll(pane);
    }

    @FXML
    private void loginScreenP(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("loginPatient.fxml"));
        loginPane.getChildren().setAll(pane);
    }

    @FXML
    private void loginScreenP1(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("loginPatient.fxml"));
        registerPane1.getChildren().setAll(pane);
    }

    @FXML
    private void loginPatient(ActionEvent actionEvent) {
        try {
            int holder = ref.login(nameField.getText(), passwordField.getText(), "database/patient_list.txt");
            if(holder > - 1) {
                File list = new File("database/loggedInP.txt");
                FileWriter writer = new FileWriter(list);
                writer.write(nameField.getText() + " " + holder);
                writer.close();

                File list1 = new File("database/PatientProfiles//" + nameField.getText() + "//profile.txt");
                if(list1.exists()) {
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
                }
                else {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("setupPatient.fxml"));
                    loginPane1.getChildren().setAll(pane);

                }
            }

            else {
                status.setText("Incorrect username/password.");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    private void registerScreenP(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("registerPatient.fxml"));
        loginPane1.getChildren().setAll(pane);
    }

    @FXML
    private void registerScreenP1(ActionEvent actionEvent) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("registerPatient.fxml"));
        registerPane.getChildren().setAll(pane);
    }
    @FXML
    private void registerPatient(ActionEvent actionEvent) {
        File list = new File("database/patient_list.txt");
        if(passwordFieldReg.getText().equals(passwordFieldReg1.getText())) {
            try {
                if(list.createNewFile()) {
                    FileWriter writer = new FileWriter(list);
                    writer.write(nameFieldReg.getText() + " " + passwordFieldReg.getText());
                    writer.close();
                    statusReg.setText("Account successfully created.");
                    nameFieldReg.clear();
                    passwordFieldReg.clear();
                    passwordFieldReg1.clear();
                }
                else {
                    BufferedReader reader = new BufferedReader(new FileReader(list));

                    String program = "";
                    String line = reader.readLine();
                    while(line != null) {
                        program = program + line + System.lineSeparator();
                        line = reader.readLine();
                    }

                    String[] accountsSep = program.split("\n");
                    HashMap<String, String> accounts = new HashMap<>(); //CHECK THIS IF PROGRAM ERROR
                    for(int q = 0; q < accountsSep.length; q++) {
                        String[] holder1 = accountsSep[q].split(" ");
                        accounts.put(holder1[0], holder1[1]);
                    }

                    int holder = 0;
                    if(accounts.containsKey(nameFieldReg.getText())) {
                        statusReg.setText("Account already exists.");
                        holder++;
                    }

                    if(holder == 0) {
                        FileWriter writer = new FileWriter(list);
                        writer.write(program + nameFieldReg.getText() + " " + passwordFieldReg.getText());
                        writer.close();
                        statusReg.setText("Account successfully created.");
                        nameFieldReg.clear();
                        passwordFieldReg.clear();
                        passwordFieldReg1.clear();
                    }
                    else {
                        statusReg.setText("Account already exists.");
                    }
                }
            } catch (Exception w) {

            }
        }
        else {
            statusReg.setText("Password does not match.");
        }
    }
}
