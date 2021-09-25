package ph.edu.dlsu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public class AllFunctions {

    public static String checksched(File sched) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sched));

        String program="";
        String line=reader.readLine();
        while (line != null)
        {
            program = program + line + System.lineSeparator();
            line = reader.readLine();
        }

        return program;
    }

    public static String[] splitter(String time){
        String[] array1 = time.split(":");
        return array1;
    }


    public void errorscreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("error.fxml"));
        Parent root = loader.load();

        Stage nextStage = new Stage();
        Scene scene = new Scene(root);
        nextStage.setTitle("Error!");
        nextStage.initModality(Modality.WINDOW_MODAL);
        nextStage.setScene(scene);
        nextStage.show();
    }



}

