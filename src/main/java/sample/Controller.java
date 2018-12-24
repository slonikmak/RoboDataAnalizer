package sample;

import com.oceanos.ros.core.connections.UDPClient;
import com.oceanos.ros.data.analizer.ChartView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Controller {

    private ObservableList<ChartListItemData> chartListItemData = FXCollections.observableArrayList();

    private UDPClient client;

    private ChartView chartView;


    @FXML
    AnchorPane chartPane;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField separatorField;

    @FXML
    private TextField timeIndexField;

    @FXML
    private ListView<ChartListItemData> chartsList;

    @FXML
    void addChart(ActionEvent event) {
        chartListItemData.add(new ChartListItemData(Color.RED, chartListItemData.size()));
    }

    @FXML
    void clearChart(){

        ///chartPane.getChildren().clear();
        //chartView = new ChartView("Chart", chartPane.getWidth(), chartPane.getHeight());
        //chartPane.getChildren().add(chartView);
        chartView.clear();
        fillChart();
    }

    @FXML
    void connect(ActionEvent event) {
        chartPane.getChildren().clear();

        try {
            client = new UDPClient(hostField.getText(), Integer.valueOf(portField.getText()), 256);
            client.start();

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        chartView = new ChartView("Chart", chartPane.getWidth(), chartPane.getHeight());


        chartPane.getChildren().add(chartView);

        fillChart();
        client.setOnRecived(c->{
            String[] data = new String(c).split(separatorField.getText());
            long time = Long.parseLong(data[Integer.parseInt(timeIndexField.getText())]);
            chartListItemData.forEach(chartData->{
                int index = chartListItemData.indexOf(chartData);
                //System.out.println("add to series "+time+" "+Double.parseDouble(data[chartData.index.get()])+" "+index);

                chartView.addDataToSeries(time, Double.parseDouble(data[chartData.index.get()]), index);
            });
        });
    }

    @FXML
    void deleteChart(ActionEvent event) {

    }

    private void fillChart(){
        chartListItemData.forEach(c->{
            Color color = c.color.get();
            chartView.createDynamicSeries(""+c.index, new java.awt.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity()));
        });
    }

    public void initialize(){
        chartsList.setItems(chartListItemData);
        chartsList.setCellFactory(param -> new ChartViewCell());
    }

    class ChartListItemData{
        ObjectProperty<Color> color = new SimpleObjectProperty<>();
        IntegerProperty index = new SimpleIntegerProperty();

        public ChartListItemData(Color color, int index) {
            this.color.set(color);
            this.index.set(index);
        }
    }

    class ChartViewCell extends ListCell<ChartListItemData> {

        @Override
        protected void updateItem(ChartListItemData itemData, boolean empty) {
            super.updateItem(itemData, empty);

            if(empty || itemData == null) {

                setText(null);
                setGraphic(null);

            } else {
                HBox hBox = new HBox();
                hBox.setSpacing(5);
                TextField index = new TextField();
                index.setPrefWidth(50);
                ColorPicker colorPicker = new ColorPicker();
                index.textProperty().bindBidirectional(itemData.index, new StringConverter<Number>() {
                    @Override
                    public String toString(Number object) {
                        return object.toString();
                    }

                    @Override
                    public Number fromString(String string) {
                        if (string.equals("")) return 0;
                        return Integer.valueOf(string);
                    }
                });
                colorPicker.valueProperty().bindBidirectional(itemData.color);
                hBox.getChildren().addAll(index, colorPicker);

                setText(null);
                setGraphic(hBox);
        }
    }

    }




}