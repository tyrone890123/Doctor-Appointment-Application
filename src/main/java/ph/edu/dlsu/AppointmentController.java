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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import static javafx.stage.Modality.WINDOW_MODAL;
import static ph.edu.dlsu.AllFunctions.checksched;

public class AppointmentController implements Initializable {

    double xOffset, yOffset;

    @FXML
    public BorderPane borderpane;
    @FXML
    public Button chooseTimeBtn, logoutBtn, exit, cancelSchedBtn;
    @FXML
    public TextArea symptomsBox;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ScrollPane scrollPane, scrollPane1;
    @FXML
    public Group group, group1;
    @FXML
    public Label profileName, age, weight, height;
    @FXML
    public ImageView profilePic;

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

        Doctors ref = new Doctors();

        try{
            String[] accounts=ref.getAccounts();
            Stack<String> dnames=new Stack<>();
            for(int i=0;i<accounts.length;i++){
                String[] holder1=accounts[i].split(" ");
                dnames.push(holder1[0]);
            }

            AtomicInteger valuehold = new AtomicInteger();

            int dvalue=dnames.size();
            while(!dnames.empty()){
                Button btn = new Button(dnames.pop());
                btn.setLayoutY(60*dvalue);
                btn.setPrefSize(195,25);

                int num=dvalue-1;
                int finalNum = num;
                group.getChildren().add(btn);
                scrollPane.setContent(group);
                String[] finalUser = user;
                btn.setOnAction(e -> {
                    group1.getChildren().clear();
                    assert finalUser != null;
                    try {
                        pmenu(finalNum, finalUser[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                dvalue--;
            }



        }catch(Exception l){

        }

    }

    private void pmenu(int value, String curr) throws IOException {
        //showsched
        Doctors ref = new Doctors();
        String[] accounts=ref.getAccounts();
        String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

        String[] getdir=accounts[value].split(" ");
        File sched = new File("database/Patient//"+getdir[0]+"//"+mydate+".txt");
        File sched2 = new File("database/Doctor//"+getdir[0]+"//"+mydate+".txt");

        String holder=checksched(sched);
        String[] holder2=holder.split("\n");

        datePicker.setValue(LocalDate.now());

        File file = new File("database/Doctor//"+getdir[0].trim());
        File[] listOfFiles = file.listFiles();
        MyQueue dates=new MyQueue();

        for(int i=0;i<listOfFiles.length;i++){
            String[] holder3=listOfFiles[i].getName().split("[.]");
            dates.push(holder3[0]);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        List<LocalDate> taken = new ArrayList<>();
        while(!dates.empty()){
            LocalDate holder9=LocalDate.parse(dates.pop().toString(), formatter);
            if(LocalDate.now().compareTo(holder9)<0){
                taken.add(holder9);
            }
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




        datePicker.setOnAction(e->{
            try {
                group1.getChildren().clear();
                File schedP = new File("database/Patient//"+getdir[0].trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
                String holder4=checksched(schedP);
                String[] holder5=holder4.split("\n");


                for(int i=0;i<holder5.length;i++){
                    Label it = new Label(holder5[i]);
                    it.setFont(new Font("Segoe UI",12));
                    it.setLayoutY(holder5.length>11?30*i:((400/holder5.length)*i));
                    group1.getChildren().add(it);
                }

                scrollPane1.setContent(group1);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        for(int i=0;i<holder2.length;i++){
            Label it = new Label(holder2[i]);
            it.setFont(new Font("Segoe UI",12));
            it.setLayoutY(holder2.length>11?30*i:((400/holder2.length)*i));
            group1.getChildren().add(it);
        }
        scrollPane1.setContent(group1);


        //INPUT SCHED
        Patients ref5 = new Patients();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sched));
            BufferedReader reader2 = new BufferedReader(new FileReader(sched2));

            String program="";
            String line=reader.readLine();

            while (line != null)
            {
                program = program + line + System.lineSeparator();
                line = reader.readLine();
            }
            String[] holder5;
            holder5=program.split("\n");

            String program2="";
            String line2=reader2.readLine();
            while (line2 != null)
            {
                program2= program2 + line2 + System.lineSeparator();
                line2 = reader2.readLine();

                String finalProgram = program;
                String finalProgram1 = program2;

                String finalProgram2 = program2;
                String finalProgram3 = program;

                chooseTimeBtn.setOnAction(e -> {
                    String holder1=symptomsBox.getText();
                    try {
                        int val=ref5.covidchecker(symptomsBox.getText());
                        if(val>0){
                            holder1=holder1+"(SUSPECTED OF COVID)";
                            chooseTime(sched,sched2, finalProgram3, curr,holder1,getdir[0],val);
                        }else{
                            chooseTime(sched,sched2, finalProgram3, curr,holder1,getdir[0],val);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

                cancelSchedBtn.setOnAction(e -> {
                    try {
                        deleteSched(sched, sched2, curr, getdir[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void chooseTime(File patient, File Doctor, String one, String name, String symptoms, String docname, int val) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("chooseTime.fxml"));
            Parent root = loader.load();

            ChooseTimeController chooseTimeController = loader.getController();
            chooseTimeController.chooseTimeHere(patient,Doctor, one, name,symptoms,docname,val);

            Stage nextStage = new Stage();
            Scene scene = new Scene(root);
            nextStage.setTitle("Automated Appointment App: Choose Time");
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

    public void deleteSched(File Patient, File Doctor, String one, String docname) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteTime.fxml"));
        Parent root = loader.load();

        DeleteTimeController deleteTimeController = loader.getController();
        deleteTimeController.deleteSchedHere(Patient,Doctor, one, docname);

        Stage nextStage = new Stage();
        Scene scene = new Scene(root);
        nextStage.setTitle("Automated Appointment App: Delete Time");
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
    @FXML
    public void home() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("patientmenu.fxml"));
        borderpane.getChildren().setAll(pane);
    }

    @FXML
    public void previous() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("previousAppointment.fxml"));
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
