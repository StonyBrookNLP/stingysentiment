package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class LeavesLabelConfusionMatrix {

    private static int[][] labelConfusion;
    private static Map<String, Integer> labelTrainMap = new HashMap<>();
    private static Set<String> notFoundLeafNodes = new HashSet<>();
    private static int[][] leafLabelConfusion = new int[5][5];

    public LeavesLabelConfusionMatrix() {
    }

    protected static void countTrainTree(Tree tree) {
        if (tree.isPreTerminal()) {
            Integer gold = RNNCoreAnnotations.getGoldClass(tree);
            String key = tree.firstChild().value();
            labelTrainMap.put(key, gold);
            return;
        }
        for (Tree child : tree.children()) {
            countTrainTree(child);
        }
    }

    protected static void countTestTree(Tree tree) {
        if (tree.isPreTerminal()) {
            Integer gold = RNNCoreAnnotations.getGoldClass(tree);
            String key = tree.firstChild().value();
            Integer found = labelTrainMap.get(key);
            int trainLabel = (found == null) ? 2 : found;
            leafLabelConfusion[trainLabel][gold]++;
            if (found == null) notFoundLeafNodes.add(key);
            return;
        }
        for (Tree child : tree.children()) {
            countTestTree(child);
        }
    }

    public static void main(String[] args) {
        String trainPath = "";
        String testPath = "";
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-trainPath")) {
                trainPath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-testPath")) {
                testPath = args[argIndex + 1];
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int k = 0;
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(trainPath);
        for (Tree tree : trainingTrees) {
            countTrainTree(tree);
            System.err.println(tree.toString());
            if (k++ > 1) System.exit(0);
        }
        List<Tree> testingTrees = SentimentUtils.readTreesWithGoldLabels(testPath);
        for (Tree tree : testingTrees) {
            countTestTree(tree);
        }
        for (int i = 0; i < 5; i++) {
            String row = "";
            for (int j = 0; j < 5; j++) {
                row += ((Integer) leafLabelConfusion[i][j]).toString() + " ";
            }
            System.err.println(row);
        }
        System.err.println(notFoundLeafNodes);
    }
}
