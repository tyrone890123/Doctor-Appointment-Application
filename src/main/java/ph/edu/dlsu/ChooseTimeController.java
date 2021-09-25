package ph.edu.dlsu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ChooseTimeController {

    @FXML
    public Group group, group1;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Button exit;

    public void chooseTimeHere(File patient, File Doctor, String one, String name, String symptoms, String docname, int val) {
        Patients ref = new Patients();

        String[] holder1=one.split("\n");
        datePicker.setValue(LocalDate.now());

        File file = new File("database/Doctor//"+docname.trim());
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
                group.getChildren().clear();
                File schedP = new File("database/Patient//"+docname.trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
                String holder4 = ref.checksched(schedP);
                String[] holder5=holder4.split("\n");
                File file2=new File("database\\PatientProfiles/"+name+"\\"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+" "+docname+" "+".txt");
                for(int i=0;i<holder5.length;i++){
                    if(i==0||i==holder5.length-1||holder5[i].split(" ").length==2){
                        Label btn = new Label(holder5[i]);
                        btn.setLayoutX(120);
                        btn.setLayoutY(55*i+10);
                        btn.setPrefSize(200,50);
                        group.getChildren().add(btn);
                    }else{
                        if(!file2.exists()) {
                            Button btn = new Button(holder5[i]);
                            btn.setLayoutX(0);
                            btn.setLayoutY(55 * i);
                            btn.setPrefSize(200, 50);
                            int finalI = i;
                            btn.setOnAction(z -> {
                                File sched = new File("database\\Doctor\\" + docname + "\\" + datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim() + ".txt");
                                try {
                                    File list2 = new File("database\\Doctor\\covidrange\\" + datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim() + ".txt");
                                    if (list2.createNewFile()) {
                                        FileWriter writer = new FileWriter(list2);
                                        writer.write(name + " " + val);
                                        writer.close();
                                    } else {
                                        BufferedReader reader3 = new BufferedReader(new FileReader(list2));

                                        String program3 = "";
                                        String line3 = reader3.readLine();
                                        while (line3 != null) {
                                            program3 = program3 + line3 + System.lineSeparator();
                                            line3 = reader3.readLine();
                                        }
                                        if (!program3.contains(name)) {
                                            FileWriter writer = new FileWriter(list2);
                                            writer.write(program3 + name + " " + val);
                                            writer.close();
                                        }
                                    }

                                    ref.userinput(schedP, sched, name, symptoms, holder5[finalI].trim());
                                    file2.createNewFile();

                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("checkup.fxml"));
                                    try {
                                        Parent root = loader.load();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }

                                    Stage stage = (Stage) exit.getScene().getWindow();
                                    stage.close();

                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            });
                            group.getChildren().add(btn);
                        }
                        else{
                            Label btn = new Label(holder5[i]);
                            btn.setLayoutX(0);
                            btn.setLayoutY(55*i);
                            group.getChildren().add(btn);
                        }
                    }
                }
                scrollPane.setContent(group);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        File file2=new File("database\\PatientProfiles\\"+name+"\\"+mydate+" "+docname+" "+".txt");
        for(int i=0;i<holder1.length;i++) {
            if (i == 0 || i == holder1.length - 1 || holder1[i].split(" ").length == 2) {
                Label btn = new Label(holder1[i]);
                btn.setLayoutX(120);
                btn.setLayoutY(55 * i + 10);
                btn.setPrefSize(180, 25);
                group.getChildren().add(btn);
            } else {
                if(!file2.exists()) {
                    Button btn = new Button(holder1[i]);
                    btn.setLayoutX(0);
                    btn.setLayoutY(55 * i);
                    btn.setPrefSize(180, 25);
                    int finalI = i;
                    btn.setOnAction(e -> {
                        try {
                            File list2 = new File("database\\Doctor\\covidrange\\" + mydate + ".txt");
                            if (list2.createNewFile()) {
                                FileWriter writer = new FileWriter(list2);
                                writer.write(name + " " + val);
                                writer.close();
                            } else {
                                BufferedReader reader3 = new BufferedReader(new FileReader(list2));

                                String program3 = "";
                                String line3 = reader3.readLine();
                                while (line3 != null) {
                                    program3 = program3 + line3 + System.lineSeparator();
                                    line3 = reader3.readLine();
                                }
                                if(!program3.contains(name)) {
                                    FileWriter writer = new FileWriter(list2);
                                    writer.write(program3 + name + " " + val);
                                    writer.close();
                                }
                            }

                            ref.userinput(patient, Doctor, name, symptoms, holder1[finalI].trim());
                            file2.createNewFile();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkup.fxml"));
                            try {
                                Parent root = loader.load();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }

                            Stage stage = (Stage) exit.getScene().getWindow();
                            stage.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    group.getChildren().add(btn);
                }
                else {
                    Label btn = new Label(holder1[i]);
                    btn.setLayoutX(0);
                    btn.setLayoutY(55*i);
                    group.getChildren().add(btn);
                }
            }
        }
        scrollPane.setContent(group);
    }

    @FXML
    public void exit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment.fxml"));
        try {
            Parent root = loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
