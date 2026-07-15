package com.neutrosophicache;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;

public class GraphGenerator {

    public static void generateHitRateGraph(
            double[] neutroRates,
            double[] lruRates,
            double[] lfuRates
            ) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[] workloads = {
                "Workload A (I1)",
                "Workload B (I2)",
                "Workload C (Mixed)"
        };

        for (int i = 0; i < workloads.length; i++) {
            dataset.addValue(neutroRates[i], "Neutrosophic", workloads[i]);
            dataset.addValue(lruRates[i],    "LRU",           workloads[i]);
            dataset.addValue(lfuRates[i],    "LFU",           workloads[i]);
        }

        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Hit Rate Comparison: Neutrosophic vs LRU vs LFU",
                "Workload Type",
                "Hit Rate (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // urls
        );

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        LineAndShapeRenderer renderer =
                new LineAndShapeRenderer();

        renderer.setSeriesPaint(0, new Color(31, 56, 100));
        renderer.setSeriesStroke(0,
                new BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesPaint(1, new Color(197, 90, 17));
        renderer.setSeriesStroke(1,
                new BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(1, true);

        renderer.setSeriesPaint(2, new Color(55, 86, 35));
        renderer.setSeriesStroke(2,
                new BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(2, true);

        plot.setRenderer(renderer);

        NumberAxis rangeAxis =
                (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 100);
        rangeAxis.setTickUnit(
                new org.jfree.chart.axis.NumberTickUnit(10));

        ChartFrame frame = new ChartFrame(
                "Hit Rate Comparison", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
