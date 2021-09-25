package ph.edu.dlsu;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class CheckupController implements Initializable {

    double xOffset, yOffset;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public BorderPane borderpane;
    @FXML
    public TextArea diagnosisBox;
    @FXML
    public Group group;
    @FXML
    public Button exit, saveBtn, homeBtn;
    @FXML
    public ImageView profilePic;
    @FXML
    public Label profileName, profileSpecialty, error, diagnosisSavedLbl;

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

        assert user != null;
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

        //STARTING HERE ARE THE CODES FOR SHOWING THE PATIENTS SCHEDULE FOR DIAGNOSIS
        Doctors ref = new Doctors();
        String myDate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

        File file1 = new File("database/Doctor//" + user[0] + "//" + myDate + ".txt");
        String whole = null;

        try {
            whole = ref.checksched(file1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String[] holder = whole.split("\n");
        String[] patients = new String[holder.length - 2];
        int iteration = 0;
        for (int i = 0; i < holder.length; i++) {
            String[] splitData = holder[i].split(" ", 2);

            if (splitData.length == 2) {
                if (splitData[1].contains(",")) {
                    String[] splitData2 = splitData[1].split(",", 2);
                    patients[iteration] = splitData2[0];
                    iteration++;
                }
            }
        }


        int iterSpace = 0;
        for (int i = 0; i < patients.length; i++) {
            if (patients[i] != null) {
                Button btn = new Button(patients[i]);
                btn.setLayoutY(150 + iterSpace);
                btn.setPrefSize(180, 25);

                String[] finalUser = user;
                String[] finalUser1 = user;
                int finalI = i;
                btn.setOnAction(e -> {
                    diagnosisSavedLbl.setText(patients[finalI] + " is selected");
                    try {
                        File list2 = new File("database\\Doctor\\interactions\\"+myDate+".txt");
                        if (list2.createNewFile()){
                            FileWriter writer = new FileWriter(list2);
                            writer.write(finalUser[0] +" "+btn.getText()+"\n"+btn.getText()+" "+ finalUser[0]);
                            writer.close();
                        }
                        else {
                            BufferedReader reader3 = new BufferedReader(new FileReader(list2));

                            String program3="";
                            String line3=reader3.readLine();
                            while (line3 != null)
                            {
                                program3 = program3 + line3 + System.lineSeparator();
                                line3 = reader3.readLine();
                            }

                            FileWriter writer = new FileWriter(list2);
                            writer.write(program3+ finalUser[0] +" "+btn.getText()+"\n"+btn.getText()+" "+ finalUser[0]);
                            writer.close();
                        }
                        File file3=new File("database\\PatientProfiles\\"+btn.getText()+"\\"+myDate+" "+ finalUser[0] +" "+".txt");
                        diagnosis(file3);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                });

                group.getChildren().add(btn);
                scrollPane.setContent(group);
                iterSpace = iterSpace + 30;

            }
        }

        //END OF THE CODES


    }

    @FXML
    public void diagnosis(File file) throws IOException {
        Doctors ref = new Doctors();
        String whole = ref.checksched(file);

        diagnosisBox.setText(whole);
        saveBtn.setOnAction(e -> {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(diagnosisBox.getText());
                writer.close();
                diagnosisSavedLbl.setText("Diagnosis Sent To Patient");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }

    @FXML
    public void home() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("doctormenu.fxml"));
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
}


