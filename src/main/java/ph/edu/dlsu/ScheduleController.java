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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    double xOffset, yOffset;

    @FXML
    public TableView<Person> tableView;
    @FXML
    public TableColumn timeColumn, patientColumn, symptomsColumn;
    @FXML
    public Button addTimeBtn;
    @FXML
    public ImageView profilePic;
    @FXML
    public Label profileName, profileSpecialty;
    @FXML
    public DatePicker datePicker;
    @FXML
    public BorderPane borderpane;
    @FXML
    public Button exit;
    @FXML
    public TextField addTimeText;



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
        int value = Integer.parseInt(user[1]); //the holder in the loggedIn.txt file
        Doctors ref = new Doctors();
        datePicker.setValue(LocalDate.now());
        String[] accounts = new String[0];
        try {
            accounts = ref.getAccounts();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //Initialize the file for the profile name, picture, and specialty
        File file1 = new File("database/DoctorProfiles//" + user[0] + ".txt");
        BufferedReader reader1 = null;
        try {
            reader1 = new BufferedReader(new FileReader(file1));
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

        String[] getDir = accounts[value].split(" ");

        File file = new File("database/Doctor//" + getDir[0].trim());
        File[] listOfFiles = file.listFiles();
        MyQueue dates = new MyQueue();

        for(int i = 0; i < listOfFiles.length; i++) {
            String[] holder = listOfFiles[i].getName().split("[.]");
            dates.push(holder[0]);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        List<LocalDate> taken = new ArrayList<>();
        while(!dates.empty()) {
            taken.add(LocalDate.parse(dates.pop().toString(), formatter));
        }

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if(taken.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            }
                        }
                    }
                };
            }
        });


        File initialSched = new File("database/Doctor//" + getDir[0].trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim() + ".txt");
        File initialSchedP = new File("database/Patient//" + getDir[0].trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
        if(initialSched.exists()) {
            table(initialSched, initialSchedP);
        }
        else {
            makeSched(value, datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim());
        }

        datePicker.setOnAction(e -> {
            File sched = new File("database/Doctor//"+getDir[0].trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
            File schedP = new File("database/Patient//"+getDir[0].trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
            if(sched.exists()){
                table(sched,schedP);
            }else{
                if(LocalDate.now().compareTo(datePicker.getValue())<0){
                    makeSched(value, datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim());
                }else{
                    File empty = new File("empty.txt");
                    table(empty,empty);
                }
            }
        });
    }
    public void table(File sched, File schedP) {

        ObservableList<Person> data = FXCollections.observableArrayList();
        System.out.println(data);
        AllFunctions allFunctions = new AllFunctions();
        try {
            String[] holder = allFunctions.checksched(sched).split("\n");
            for(int i = 0; i < holder.length; i++) {
                String[] splitData = holder[i].split(" ", 2);
                if(splitData.length == 2) {
                    if(splitData[1].contains(",")) {
                        String[] splitData2 = splitData[1].split(",", 2);
                        data.add(new Person(splitData[0], splitData2[0], splitData2[1]));
                    }
                    else {
                        data.add(new Person(splitData[0], splitData[1], ""));
                    }
                }
                else {
                    data.add(new Person(splitData[0], "", ""));
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


        timeColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("time"));
        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                int currentTime = t.getTablePosition().getRow();
                try {
                    String originalText = AllFunctions.checksched(sched);
                    String originalText2 = AllFunctions.checksched(schedP);
                    String replace = originalText.replace(t.getTableView().getItems().get(currentTime).getTime(), t.getNewValue());
                    FileWriter writer = new FileWriter(sched);
                    writer.write(replace);
                    writer.close();

                    String replaced2=originalText2.replace(t.getTableView().getItems().get(currentTime).getTime(),t.getNewValue());
                    FileWriter writer2 = new FileWriter(schedP);
                    writer2.write(replaced2);
                    writer2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTime(t.getNewValue());
            }
        });

        patientColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        patientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        patientColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            }
        });
//
        symptomsColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("symptoms"));
        symptomsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        symptomsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String >>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSymptoms(t.getNewValue());
            }
        });
//
        tableView.setItems(data);


        addTimeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    String originalText = AllFunctions.checksched(sched);
                    String originalText2 = AllFunctions.checksched(schedP);
                    if(tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getSymptoms().equals("")) {
                        if(tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getName().equals("")) {
                            String toBeReplaced = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime();
                            String newVal = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+"\n"+addTimeText.getText();
                            String replaced2 = originalText.replace(toBeReplaced, newVal);
                            String replacedPatient = originalText2.replace(toBeReplaced, newVal);

                            FileWriter writer = new FileWriter(sched);
                            writer.write(replaced2);
                            writer.close();
                            FileWriter writer2 = new FileWriter(schedP);
                            writer2.write(replacedPatient);
                            writer2.close();
                        }
                        else {
                            String toBeReplaced = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getName();
                            String newVal = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getName()+"\n"+addTimeText.getText();
                            String replaced2 = originalText.replace(toBeReplaced, newVal);

                            FileWriter writer = new FileWriter(sched);
                            writer.write(replaced2);
                            writer.close();
                            FileWriter writer2 = new FileWriter(schedP);
                            writer2.write(replaced2);
                            writer2.close();
                        }
                    }
                    else {
                        String originalTextPatient = AllFunctions.checksched(schedP);
                        String toBeReplaced = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getName()+","+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getSymptoms();
                        String newVal = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getName()+","+tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getSymptoms()+"\n"+addTimeText.getText();
                        String replaced2=  originalText.replace(toBeReplaced, newVal);
                        String toBeReplaced2 = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+"Taken";
                        String newVal2 = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).getTime()+" "+"Taken"+"\n"+addTimeText.getText();
                        String replaced3 = originalTextPatient.replace(toBeReplaced2, newVal2);

                        FileWriter writer = new FileWriter(sched);
                        writer.write(replaced2);
                        writer.close();
                        FileWriter writer2 = new FileWriter(schedP);
                        writer2.write(replaced3);
                        writer2.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                data.add(tableView.getSelectionModel().getFocusedIndex() + 1, new Person(addTimeText.getText(), "", ""));
                addTimeText.clear();
            }
        });

    }

    private void makeSched(int value, String date) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("schedulemake.fxml"));
            Parent root = loader.load();

            ScheduleMakeController scheduleController = loader.getController();
            scheduleController.makeSchedHere(value, date);

            Stage nextStage = new Stage();
            Scene scene = new Scene(root);
            nextStage.setTitle("Automated Appointment App: Create Schedule");
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

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @FXML
    public void home() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("doctormenu.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void checkup() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("checkup.fxml"));
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
