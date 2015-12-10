package edu.stanford.nlp.sentimentUtils;

/**
 * @author ankit.gupta.1@stonybrook.edu
 *         Created by root on 10/14/15.
 */
public class LogisticClassifier {
    private static int nodeLabel = -1;

    public LogisticClassifier(double[][] coef, double[] intercept, double[] x) {
        double largest = Double.MIN_VALUE;
        for (int i = 0; i < 5; i++) {
            double z = dotProduct(coef[i], x) + intercept[i];
            double current = 1 / (1 + Math.exp(-z));
            if (current > largest) {
                largest = current;
                nodeLabel = i;
            }
        }
    }

    public int getNodeLabel() {
        return nodeLabel;
    }

    protected static double dotProduct(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
}
