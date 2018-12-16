package sample;

import com.oceanos.ros.data.analizer.ChartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TestChartView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        ChartView chartView = new ChartView("sds", 600, 400);

        root.getChildren().add(chartView);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));


        Button btn = new Button("open file");
        btn.setOnAction((e)->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                chartView.addDataSet(file.getAbsolutePath(), 0, 1, 0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        root.getChildren().add(btn);

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
