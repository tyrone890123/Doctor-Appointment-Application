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
import java.util.ResourceBundle;

public class RiskController implements Initializable {

    double xOffset, yOffset;
    @FXML
    public BorderPane borderpane;
    @FXML
    public AnchorPane middlePanel;
    @FXML
    public Button exit;
    @FXML
    public Label profileName, profileSpecialty;
    @FXML
    public ImageView profilePic;

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

        Doctors ref = new Doctors();
        try {
            String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
            File covpatients = new File("database\\Doctor\\covidrange\\" + mydate + ".txt");
            String holder = ref.checksched(covpatients);
            String[] holder2 = holder.split("\n");
            BSTreeInt a = new BSTreeInt();
            for (int j = 0; j < holder2.length; j++) {
                String[] holder3 = holder2[j].split(" ");
                a.add(Integer.parseInt(holder3[1].trim()), holder3[0]);
            }
            showTree(a);

        } catch (IOException ioException) {
            try {
                ref.errorscreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTree(BSTreeInt a) throws IOException {
        Doctors ref = new Doctors();
        try {
            preorder(a.root,500,20,0);
        }catch (Exception w){
            ref.errorscreen();
        }
    }

    private void preorder(BSTreeInt.Node a, int b, int c, int depth) {
        if(a!=null){
            Label menu = new Label(""+ a.name);
            menu.setTextFill(Color.web("#959595"));
            menu.setPrefSize(250,50);
            Circle circle = new Circle(b-235, c+20, 30);
            menu.setLayoutX(circle.getCenterX()-((a.name.length()*6)/2));
            menu.setLayoutY(c);
            circle.setFill(Color.WHITE);
            middlePanel.getChildren().addAll(menu,circle);
            menu.toFront();

            if(a.left!=null){
                preorder(a.left,b-100/(depth+1),c+80,depth+1);
                Label holder=new Label();
                holder.setLayoutX(b-100/(depth+1));
                holder.setLayoutY(c+80);
                Circle circle2 = new Circle(holder.getLayoutX()-235, holder.getLayoutY()+20, 30);
                Line line = new Line(circle.getCenterX(), circle.getCenterY(), circle2.getCenterX(), circle2.getCenterY());
                middlePanel.getChildren().addAll(line);
                menu.toFront();
                line.toBack();
            }
            if(a.right!=null){
                preorder(a.right,b+100/(depth+1),c+80,depth+1);
                Label holder=new Label();
                holder.setLayoutX(b+100/(depth+1));
                holder.setLayoutY(c+80);
                Circle circle2 = new Circle(holder.getLayoutX()-235, holder.getLayoutY()+20, 30);
                Line line = new Line(circle.getCenterX(), circle.getCenterY(), circle2.getCenterX(), circle2.getCenterY());
                middlePanel.getChildren().add(line);
                menu.toFront();
                line.toBack();
            }
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
    public void interaction() throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("interaction.fxml"));
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
