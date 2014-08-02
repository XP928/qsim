/*
 * PlotGenerator.java
 */
package gg1simulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Administrator
 */
public class PlotGenerator {

    public static JFreeChart generateHistogram(ArrayList<Double> list, String name) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        double[] values = new double[list.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = list.get(i);
        }
        dataset.addSeries("name", values, 20);
        JFreeChart chart = ChartFactory.createHistogram(name, "Time (s)", "Relative Frequency", dataset, PlotOrientation.VERTICAL, false, false, false);
        
        return chart;        
    }

    public static JFreeChart generateHistogram(HashMap<Integer, Double> map, String name, int bar, int type) {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        double max = -1;
        for (Entry<Integer, Double> e : map.entrySet()) {
            max = Math.max(max, e.getKey());
        }
        double[] values = new double[(int) max + 1];
        for (Entry<Integer, Double> e : map.entrySet()) {
            values[e.getKey()] += e.getValue();
        }
        int length;
        if (values.length < bar) {
            length = values.length;
        } else {
            length = bar;
        }
        double sum = Utility.getSum(map);
        for (int i = 0; i < length; i++) {
            d.setValue(values[i]/sum, "Relative Frequency", "" + i);
        }
        JFreeChart chart = ChartFactory.createBarChart(name, "Number of jobs", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        if (type == 0) {
            BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
            barRenderer.setSeriesPaint(0, Color.BLUE);
        }
        try {
            OutputStream os = new FileOutputStream("F:\\CSCClasses\\CSC 895\\Plot\\" + name + ".jpg");
            ChartUtilities.writeChartAsJPEG(os, chart, 500, 300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }        
        return chart;
    }

    public static JFreeChart generateHistogramChart(ArrayList<Double> list, int bars, String name) {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        double max = Collections.max(list);
        System.out.println("length: " + list.size() + name + ": " + max);
        double width = max / bars;
        int size = list.size();
        int index;
        int counterList[] = new int[bars];
        for (int i = 0; i < bars; i++) {
            counterList[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            index = (int) (list.get(i) / width);
            if (index >= bars) {
                continue;
            }
            counterList[index]++;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < bars; i++) {
            d.setValue((double) counterList[i] / size, "Relative Frequency", df.format((i + 1) * width).toString());
        }
        JFreeChart chart = ChartFactory.createBarChart(name, "Time", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return chart;
    }
    
    public static JFreeChart generateHistogramChart1(ArrayList<Double> list, int bars, String name){
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        int size = list.size();
        int counterList[] = new int[bars];
        for (int i = 0; i < bars; i++) {
            counterList[i] = 0;
        }
        for (int i = 0; i < size; i++) {
            for(int j = 1; j <= bars; j++){
                if(list.get(i) == j){
                    counterList[j - 1]++;
                }
            }
        }
        
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < bars; i++) {
            d.setValue((double) counterList[i] / size, "Relative Frequency", df.format((i + 1)).toString());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(name, "Time", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return chart;
    }
    
    public static JFreeChart generateHistogramChart2(ArrayList<Double> list, int bars, String name, double interval){
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        double max = Collections.max(list);
        System.out.println("length: " + list.size() + name + ": " + max);
        
        int size = list.size();
        int index;
        int counterList[] = new int[bars];
        for (int i = 0; i < bars; i++) {
            counterList[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            index = (int) (list.get(i) / interval);
            if (index >= bars) {
                continue;
            }
            counterList[index]++;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < bars; i++) {
            d.setValue((double) counterList[i] / size, "Relative Frequency", df.format((i + 1) * interval).toString());
        }
        JFreeChart chart = ChartFactory.createBarChart(name, "Time", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return chart;
    }

    public static JFreeChart generateHistogramChart(int[] list, double max, String name, int type) {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        double width = max / list.length;
        int size = Utility.getSum(list);

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < list.length; i++) {
            d.setValue((double) list[i] / size, "Relative Frequency", df.format((i + 1) * width).toString());
        }
        JFreeChart chart = ChartFactory.createBarChart(name, "Time", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        if (type == 0) {
            BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
            barRenderer.setSeriesPaint(0, Color.BLUE);
        }

        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        try {
            OutputStream os = new FileOutputStream("F:\\CSCClasses\\CSC 895\\Plot\\" + name + ".jpg");
            ChartUtilities.writeChartAsJPEG(os, chart, 500, 300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return chart;
    }
    
    
    public static JFreeChart generateHistogramChart2(int[] list, int total, double interval, String name, int type) {
        DefaultCategoryDataset d = new DefaultCategoryDataset();

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < list.length; i++) {
            d.setValue((double) list[i] / total, "Relative Frequency", df.format((i + 1) * interval).toString());
        }
        JFreeChart chart = ChartFactory.createBarChart(name, "Time", "Relative Frequency", d, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        if (type == 0) {
            BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
            barRenderer.setSeriesPaint(0, Color.BLUE);
        }

        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        try {
            OutputStream os = new FileOutputStream("F:\\CSCClasses\\CSC 895\\Plot\\" + name + ".jpg");
            ChartUtilities.writeChartAsJPEG(os, chart, 500, 300);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return chart;
    }       
    
}
