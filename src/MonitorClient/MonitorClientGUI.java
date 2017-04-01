package MonitorClient;

import javax.swing.*;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.data.xy.*;

class MonitorClientGUI extends JFrame {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int MAX_DATA_POINTS = 100;
    static final String TITLE = "Percentage of Memory Used";
    private ArrayList<XYSeries> dataPoints;

    MonitorClientGUI() {
        setSize(MonitorClientGUI.WIDTH, MonitorClientGUI.HEIGHT);
        this.dataPoints = new ArrayList<XYSeries>();
        setVisible(true);
    }

    private XYSeries getXYSeriesFromKey(String hostname) {
        for (XYSeries xy : dataPoints) {
            if (xy.getKey().equals(hostname))
                return xy;
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
                MonitorClientGUI.TITLE,
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
        if (numDataPoints > MonitorClientGUI.MAX_DATA_POINTS)
            xySeries = xySeries.createCopy(numDataPoints - MonitorClientGUI.MAX_DATA_POINTS, numDataPoints - 1);
    }
}
