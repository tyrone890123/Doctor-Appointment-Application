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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DeleteTimeController {

    @FXML
    public Group group;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Button exit;

    public void deleteSchedHere(File Patient, File Doctor, String one, String docname) throws IOException {

        Patients ref = new Patients();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(Patient));
            BufferedReader reader2 = new BufferedReader(new FileReader(Doctor));

            String program="";
            String line=reader.readLine();
            int size=1;
            while (line != null)
            {
                program = program + line + System.lineSeparator();
                line = reader.readLine();
                size++;
            }
            String[] holder=new String[size];
            holder=program.split("\n");

            String program2="";
            String line2=reader2.readLine();
            while (line2 != null)
            {
                program2= program2 + line2 + System.lineSeparator();
                line2 = reader2.readLine();
            }
            String[] holder2=new String[size];
            holder2=program2.split("\n");

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
                    File sched = new File("database/Doctor//"+docname.trim()+"//"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+".txt");
                    String holder4 = ref.checksched(schedP);
                    String[] holder5=holder4.split("\n");

                    for(int i=0;i<holder5.length;i++){
                        if(i==0||i==holder5.length-1){
                            Label btn = new Label(holder5[i]);
                            btn.setLayoutX(60);
                            btn.setLayoutY(55*i+10);
                            btn.setPrefSize(180,50);
                            group.getChildren().add(btn);
                        }
                        else if(holder5[i].split(" ").length!=2){
                            Label btn = new Label(holder5[i]);
                            btn.setLayoutX(70);
                            btn.setLayoutY(55*i+10);
                            btn.setPrefSize(180,50);
                            group.getChildren().add(btn);
                        }
                        else{
                            BufferedReader reader3 = new BufferedReader(new FileReader(sched));
                            String program3="";
                            String line3=reader3.readLine();
                            while (line3 != null)
                            {
                                program3= program3 + line3 + System.lineSeparator();
                                line3 = reader3.readLine();
                            }
                            String[] holder6=program3.split("\n");


                            String[] holder3=holder6[i].split(" ",2);
                            if (holder3[1].contains(one)){
                                Button btn = new Button(holder5[i]);
                                btn.setLayoutX(10);
                                btn.setLayoutY(55*i);
                                btn.setPrefSize(180,50);

                                int finalI = i;
                                String[] finalHolder = holder5;
                                btn.setOnAction(v -> {
                                    File file3=new File("database/PatientProfiles\\"+one+"\\"+datePicker.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).trim()+" "+docname+" "+".txt");
                                    try {
                                        file3.delete();
                                        String[] temp=finalHolder[finalI].split(" ");
                                        ref.deleteitem(schedP,sched, temp[0]);
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment.fxml"));
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
                            }else{
                                Label btn = new Label(holder5[i]);
                                btn.setLayoutX(70);
                                btn.setLayoutY(55*i+10);
                                btn.setPrefSize(180,50);
                                group.getChildren().add(btn);
                            }
                        }
                    }

                    scrollPane.setContent(group);
                }catch (Exception v){

                }
            });

            //Group group = new Group();
            for(int i=0;i<holder.length;i++){
                if(i==0||i==holder.length-1){
                    Label btn = new Label(holder[i]);
                    btn.setLayoutX(60);
                    btn.setLayoutY(55*i+10);
                    btn.setPrefSize(180,50);
                    group.getChildren().add(btn);
                }
                else if(holder[i].split(" ").length!=2){
                    Label btn = new Label(holder[i]);
                    btn.setLayoutX(70);
                    btn.setLayoutY(55*i+10);
                    btn.setPrefSize(180,50);
                    group.getChildren().add(btn);
                }
                else{
                    String[] holder3=holder2[i].split(" ",2);
                    if (holder3[1].contains(one)){
                        Button btn = new Button(holder[i]);
                        btn.setLayoutX(10);
                        btn.setLayoutY(55*i);
                        btn.setPrefSize(180,50);

                        int finalI = i;
                        String[] finalHolder = holder;
                        btn.setOnAction(e -> {
                            String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                            File file2=new File("database\\PatientProfiles\\"+one+"\\"+mydate+" "+docname+" "+".txt");
                            try {
                                file2.delete();
                                String[] temp=finalHolder[finalI].split(" ");
                                ref.deleteitem(Patient,Doctor, temp[0]);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment.fxml"));
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
                    }else{
                        Label btn = new Label(holder[i]);
                        btn.setLayoutX(70);
                        btn.setLayoutY(55*i+10);
                        btn.setPrefSize(180,50);
                        group.getChildren().add(btn);
                    }
                }
            }

            scrollPane.setContent(group);


        } catch (Exception w) {
            ref.errorscreen();
        }
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
