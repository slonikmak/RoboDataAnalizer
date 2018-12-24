package com.oceanos.ros.data.analizer;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

public class ChartView extends AnchorPane {

    //"Robo Data Chart"
    private final String chartName;
    private final String dataSeparator = ",";
    XYLineAndShapeRenderer renderer;

    private XYDataset dataset;
    private JFreeChart chart;

    public ChartView(String name, double width, double height){

        setPrefWidth(width);
        setPrefHeight(height);

        dataset = new TimeSeriesCollection();

        this.chartName = name;
        initChart();

        ChartCanvas canvas =new ChartCanvas(chart);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        /*canvas.setWidth(width);
        canvas.setHeight(height);*/

        getChildren().add(canvas);
    }

    private void initChart() {
        chart = ChartFactory.createTimeSeriesChart(chartName, null, "data", dataset);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);

        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(20000);
        axis = plot.getRangeAxis();
        axis.setRange(-180, 180);
        /*plot.getDomainAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getDomainAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        plot.getRangeAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        chart.getLegend().setItemFont(new Font(fontName, Font.PLAIN, 14));*/
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(false);
            renderer.setDrawSeriesLineAsPath(true);
            // set the default stroke for all series
            renderer.setAutoPopulateSeriesStroke(false);
            renderer.setDefaultStroke(new BasicStroke(3.0f,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL), false);
            renderer.setSeriesPaint(0, Color.RED);
            //renderer.setDefaultLegendTextFont(new Font(fontName, Font.PLAIN, 14));
        }
    }

    public void addDataSet(String filename, int timeIndex, int dataIndex, int startLine) throws IOException {
        TimeSeries series =new TimeSeries(Paths.get(filename).getFileName());

        ((TimeSeriesCollection)dataset).addSeries(series);
        Files.lines(Paths.get(filename)).skip(startLine).forEach(l->{
            l = l.replaceAll(";","");
            String[] dataStrings = l.split(dataSeparator);
            long timeStamp = Long.parseLong(dataStrings[timeIndex]);
            double data = Double.parseDouble(dataStrings[dataIndex]);
            //System.out.println(timeStamp+" "+data);
            series.add(new Millisecond(new Date(timeStamp)), data);
        });

       /* Platform.runLater(()->{

        });*/

    }

    public int createDynamicSeries(String name, Paint paint){
        TimeSeries series = new TimeSeries(name);

        ((TimeSeriesCollection)dataset).addSeries(series);
        int index = ((TimeSeriesCollection)dataset).getSeriesIndex(series.getKey());
        renderer.setSeriesPaint(index, paint);
        return index;
    }

    public void addDataToSeries(long time, double data, int index){
        //System.out.println("add to series "+time+" "+data+" "+index);
        TimeSeries series = ((TimeSeriesCollection)dataset).getSeries(index);
        Platform.runLater(()->{
            series.add(new TimeSeriesDataItem(new Millisecond(new Date(time)), data));
            //series.add(new Millisecond(new Date(time)), data);
        });
    }

    public void removeSeries(int index){
        ((TimeSeriesCollection)dataset).removeSeries(index);
    }

    public void clear(){
        ((TimeSeriesCollection)dataset).removeAllSeries();
    }

}
