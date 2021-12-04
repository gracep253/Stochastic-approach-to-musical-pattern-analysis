import Jama.Matrix;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws Exception {
        int samples = 200;
        System.out.println(comb(samples,2));
        double[] means = new double[comb(samples,2)];
        double[] norms = new double[comb(samples,2)];
        int i = 0;
        for (int x=0; x < samples-1; x++) {
            for (int y = x + 1; y< samples; y++) {
                String path1 = "src/MIDIs/download (" + (x) + ").mid";
                File f1 = new File(path1); // put path to file
                ProbabilityFinder finder1 = new ProbabilityFinder(f1); // makes probability finder object
                TreeMap<Integer, TreeMap<Integer, Integer>> map1 = finder1.getNotes(); // { first note, { second note, occurrences } }
                double[][] matrix1 = finder1.makeMatrix(false, true);
                //map1.forEach((key, value) -> System.out.println(key + ":" + value));

                String path2 = "src/MIDIs/download (" + (y) + ").mid";
                File f2 = new File(path2); // put path to file
                ProbabilityFinder finder2 = new ProbabilityFinder(f2); // makes probability finder object
                TreeMap<Integer, TreeMap<Integer, Integer>> map2 = finder2.getNotes(); // { first note, { second note, occurrences } }
                double[][] matrix2 = finder2.makeMatrix(false, true);
                //map2.forEach((key, value) -> System.out.println(key + ":" + value));

                // prints full map
                //map1.forEach((key, value) -> System.out.println(key + ":" + value));
                // gets total notes after another note
                // int totalOccurrences = finder.getTotal(62);
                // gets total times number 2 comes after number 1 (in this example 64 is number 2 and 62 is number 1)
                // int times = map.get(62).get(64);

        /*System.out.println("matrix one");
        for(double[] u : matrix1){
            for(double d: u){
                System.out.print(d + " ");
            }
            System.out.println();
        }


        System.out.println("matrix two");
        for(double[] u : matrix2){
            for(double d: u){
                System.out.print(d + " ");
            }
            System.out.println();
        }*/

                Matrix jamamatrix1 = new Matrix(finder1.makeMatrix(false, true));

            /*System.out.println("matrix1 extra decimal ");
            jamamatrix1.print(3, 3);
            System.out.println("Frobenius Norm of Matrix 1: " + jamamatrix1.normF());*/

                Matrix jamamatrix2 = new Matrix(finder2.makeMatrix(false, true));

            /*System.out.println("matrix2 extra decimal ");
            jamamatrix2.print(3, 3);
            System.out.println("Frobenius Norm of Matrix 2: " + jamamatrix2.normF());*/

                Matrix differenceMatrix = jamamatrix1.minus(jamamatrix2);

            /*System.out.println("difference Matrix");

            System.out.println("Frobenius Norm of difference Matrix: " + Math.abs(differenceMatrix.normF()));*/
                double norm = Math.abs(differenceMatrix.normF());
                norms[i] = norm;

                double sum = 0.0;
                int count = 0;
                for (int row = 0; row < 7; row++) {
                    for (int col = 0; col < 7; col++) {
                        sum += Math.abs(differenceMatrix.get(row, col));
                        if (jamamatrix1.get(row, col) != 0.0 || jamamatrix2.get(row, col) != 0.0) {
                            count++;
                        }
                    }
                }
                means[i] = sum/count;

                i++;
            }
        }

        //double[] value = means.toArray();
        //histogram for means
        //LaTeX form
        /*for (double x : means) {
            System.out.println(x + " \\\\");
        }
        System.out.println();*/
        //making graphs:
        int bins = 100; //number of bins
        HistogramDataset dataset_mean = new HistogramDataset();
        dataset_mean.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset_mean.addSeries("Histogram", means, bins);
        String plotTitle = "Histogram for \" means \"";
        String xaxis = "mean";
        String yaxis = "frequency";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
                dataset_mean, orientation, show, toolTips, urls);
        int width = 500;
        int height = 300;
        try {
            ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
        } catch (IOException e) {
        }

        //histogram for norms
        //print out values in LaTeX form
        /*for (Double x : norms) {
            System.out.println(x + " \\\\");
        }
        System.out.println();*/
        HistogramDataset dataset_norm = new HistogramDataset();
        dataset_norm.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset_norm.addSeries("HistogramN", norms, bins);
        String plotTitle2 = "Histogram for norms";
        String xaxis2 = "norm";
        String yaxis2 = "frequency";
        PlotOrientation orientation2 = PlotOrientation.VERTICAL;
        JFreeChart chart2 = ChartFactory.createHistogram(plotTitle2, xaxis2, yaxis2,
                dataset_norm, orientation2, show, toolTips, urls);
        try {
            ChartUtilities.saveChartAsPNG(new File("histogram_norm.PNG"), chart2, width, height);
        } catch (IOException e) {
        }

    }
    public static int comb(int n , int r)
    {
        if( r== 0 || n == r)
            return 1;
        else
            return comb(n-1,r)+comb(n-1,r-1);
    }
}
