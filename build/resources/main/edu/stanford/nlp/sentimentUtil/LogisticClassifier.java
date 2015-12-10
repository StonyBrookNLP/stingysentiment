package edu.stanford.nlp.sentimentUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
//            System.err.println(Arrays.toString(x));
//            System.err.println(z);
//            System.err.println(current);
            if (current > largest) {
                largest = current;
                nodeLabel = i;
            }
        }
//        System.err.println(largest + " index:" + nodeLabel);
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
