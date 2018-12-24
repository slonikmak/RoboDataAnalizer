package sample;

import com.oceanos.ros.data.analizer.ChartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TestChartView extends Application {

    private static boolean running = true;

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        ChartView chartView = new ChartView("sds", 600, 400);

        root.getChildren().add(chartView);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));


        HBox hBox = new HBox();
        hBox.setSpacing(5);
        Button btn = new Button("open file");
        Button btn2 = new Button("start filling data");
        Button btn3 = new Button("stop filling data");
        hBox.getChildren().addAll(btn, btn2, btn3);
        btn.setOnAction((e)->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                chartView.addDataSet(file.getAbsolutePath(), 0, 1, 0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        btn2.setOnAction(e->{

            new Thread(()->{
                long i =0;
                running = true;
                int index = chartView.createDynamicSeries("Series", Color.RED);
                while (running){
                    chartView.addDataToSeries(i*100, Math.random()*100, index);
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        });
        btn3.setOnAction(e->{
            running = false;
        });

        root.getChildren().addAll(hBox);

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
