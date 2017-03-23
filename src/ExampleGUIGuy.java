/*
 * CSCI320 - Networking and Distributed Computing - Spring 2017
 * Instructor: Thyago Mota
 * Description: Prg02 - Performance Monitor Client GUI
 * Your name:
 */

import javax.swing.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.data.xy.*;

class PerformanceMonitorClientGUI extends JFrame {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int MAX_DATA_POINTS = 100;
    static final String TITLE = "Percentage of Memory Used";
    private ArrayList<XYSeries> dataPoints;

    PerformanceMonitorClientGUI() {
        setSize(PerformanceMonitorClientGUI.WIDTH, PerformanceMonitorClientGUI.HEIGHT);
        this.dataPoints = new ArrayList<XYSeries>();
        setVisible(true);
    }

    private XYSeries getXYSeriesFromKey(String hostname) {
        Iterator<XYSeries> it = this.dataPoints.iterator();
        while (it.hasNext()) {
            XYSeries xySeries = it.next();
            if (xySeries.getKey().equals(hostname))
                return xySeries;
        }
        return null;
    }

    void updateChart() {
        JFreeChart chart = this.buildChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis(0);
        xAxis.setVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
        validate();
        repaint();
    }

    JFreeChart buildChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        Iterator<XYSeries> it = this.dataPoints.iterator();
        while (it.hasNext())
            dataset.addSeries(it.next());
        return ChartFactory.createXYLineChart(
                PerformanceMonitorClientGUI.TITLE,
                "Time (5s intervals)",
                "% Memory Used",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    void addDataPoint(String hostname, double x, double y) throws CloneNotSupportedException {
        XYSeries xySeries = this.getXYSeriesFromKey(hostname);
        if (xySeries == null) {
            xySeries = new XYSeries(hostname);
            this.dataPoints.add(xySeries);
        }
        xySeries.add(x, y);
        int numDataPoints = xySeries.getItemCount();
        if (numDataPoints > PerformanceMonitorClientGUI.MAX_DATA_POINTS)
            xySeries = xySeries.createCopy(numDataPoints - PerformanceMonitorClientGUI.MAX_DATA_POINTS, numDataPoints - 1);
    }

    // the code below is provided to help you understand how JFreeChart works
    public static void main(String[] args) {
        try {
            PerformanceMonitorClientGUI gui = new PerformanceMonitorClientGUI();
            gui.addDataPoint("gaia", 1, 10.5);
            gui.addDataPoint("gaia", 2, 24);
            gui.addDataPoint("gaia", 3, 2.8);
            gui.addDataPoint("aspen", 1, 78.3);
            gui.addDataPoint("aspen", 2, 65.2);
            gui.addDataPoint("aspen", 3, 9.1);
            gui.updateChart();
        }
        catch (Exception ex) {
            System.out.println("Unexpected error!");
            System.out.println(ex);        }
    }
}