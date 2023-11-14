package cpen221.mp1.histogram;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.util.*;

public class Histogram {

    private final int chartWidth  = 800;
    private final int chartHeight = 600;
    private final double fillFraction = 0.8;

    private CategoryChart chart;

    public Histogram(String chartTitle, String xAxisTitle, String yAxisTitle) {
        chart = new CategoryChartBuilder().width(chartWidth).height(chartHeight)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(fillFraction);
        chart.getStyler().setOverlapped(false);
    }

    public void addSeries(String seriesName, List xData, List yData) {
        chart.addSeries(seriesName, xData, yData);
    }

    public void showChart() {
        new SwingWrapper<>(chart).displayChart();
    }

    public static void main(String[] args) {
        Histogram h = new Histogram("RMP Gender Bias", "Rating+Gender", "Count");

        List<String> xData  = Arrays.asList("1", "2", "3", "4");
        List<Integer> yData = Arrays.asList(10, 5, 20, 4);
        h.addSeries("M", xData, yData);

        xData = Arrays.asList("1", "2", "3", "4");
        yData = Arrays.asList(7, 21, 6, 9);
        h.addSeries("W", xData, yData);

        h.showChart();
    }

}
