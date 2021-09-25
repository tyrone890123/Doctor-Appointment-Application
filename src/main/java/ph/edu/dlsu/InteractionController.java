package ph.edu.dlsu;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class InteractionController implements Initializable {

    double xOffset, yOffset;
    @FXML
    public AnchorPane middlePanel;
    @FXML
    public BorderPane borderpane;
    @FXML
    public Label profileName, profileSpecialty;
    @FXML
    public ImageView profilePic;
    @FXML
    public Button exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Doctors ref = new Doctors();
        try {
            File list = new File("database/loggedIn.txt");
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

            String[] accounts=ref.getAccounts();
            assert user != null;
            String[] getDir = accounts[Integer.parseInt(user[1])].split(" ");
            String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
            File list1 = new File("database\\Doctor\\interactions\\"+mydate+".txt");
            BufferedReader reader2 = new BufferedReader(new FileReader(list1));
            String program ="";
            String line1=reader2.readLine();
            while (line1 != null) {
                program = program + line1 + System.lineSeparator();
                line1 = reader2.readLine();
            }
            Graph<String> a = new Graph<>();
            String[] doctorPatientSplit = program.split("\n");

            for(int i = 0; i < doctorPatientSplit.length; i++){
                String[] holder1 = doctorPatientSplit[i].split(" ");
                a.addEdge(holder1[0].trim(),holder1[1].trim());
            }

            LinkedList<String> temp=new LinkedList<>();
            temp.push(getDir[0]);
            Circle h=new Circle(255,150,20);
            h.setFill(Color.LIGHTYELLOW);
            middlePanel.getChildren().add(h);
            Label rootname=new Label(getDir[0]);
            rootname.setLayoutX(h.getCenterX()-16);
            rootname.setLayoutY(h.getCenterY()-10);
            middlePanel.getChildren().addAll(rootname);

            int iterator=0;
            HashMap<String,Circle> coords=new HashMap<>();
            coords.put(getDir[0].trim(),h);

            while(iterator!=temp.size()){
                String holder=temp.get(iterator);
                String[] vertices=a.adjList.get(holder).toArray(new String[a.adjList.get(holder).size()]);
                for(int i = 0; i < vertices.length; i++){
                    if(!temp.contains(vertices[i])){
                        double spacerx=100*Math.sin(0.46*i);
                        double spacery=100*Math.cos(0.46*i);

                        Circle z = new Circle(coords.get(holder.trim()).getCenterX()+spacerx,coords.get(holder.trim()).getCenterY()+spacery,20);
                        for(int w = 0; w < getDir.length; w++){
                            if(vertices[i].equals(getDir[w])){
                                z.setFill(Color.LIGHTYELLOW);
                                break;
                            }else{
                                z.setFill(Color.WHITE);
                            }
                        }
                        Line w = new Line(z.getCenterX(),z.getCenterY(),coords.get(holder.trim()).getCenterX(),coords.get(holder.trim()).getCenterY());
                        middlePanel.getChildren().addAll(z,w);
                        z.toBack();
                        w.toBack();
                        Label childname=new Label(vertices[i]);
                        childname.setLayoutX(z.getCenterX()-16);
                        childname.setLayoutY(z.getCenterY()-10);
                        middlePanel.getChildren().addAll(childname);
                        coords.put(vertices[i],z);
                        temp.add(vertices[i]);
                    }
                }
                iterator++;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
    public void schedule() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("schedule.fxml"));
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
