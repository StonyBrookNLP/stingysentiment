package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.*;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class LabelDistribution {

    private static int[][] labelConfusion;
    private static Map<String, Integer> labelAnnotationsMap = new HashMap<>();
    private static Set<String> collidedLeaf;

    public LabelDistribution() {
    }

    protected static int countTree(Tree tree) {
        List<Integer> childrenLabel = new LinkedList<>();
        Integer gold = RNNCoreAnnotations.getGoldClass(tree);
        if (tree.isPreTerminal()) return gold;
        for (Tree child : tree.children()) {
            if (child != null) childrenLabel.add(countTree(child));
        }
        String key = gold.toString();
        for (Integer label : childrenLabel) {
            key += label.toString();
        }
        int val = labelAnnotationsMap.get(key);
        labelAnnotationsMap.put(key, val+1);
        return gold;
    }

    public static void main(String[] args) {
        collidedLeaf = new HashSet<>();
        for (Integer i = 0; i< 5; i++) {
            String curr = i.toString();
            for (Integer j = 0; j < 5; j++) {
                curr += j.toString();
                for (Integer k = 0; k < 5; k++) {
                    curr += k.toString();
                    labelAnnotationsMap.put(curr, 0);
                    curr = curr.substring(0, curr.length() - 1);
                }
                curr = curr.substring(0, curr.length() - 1);
            }
        }
        String filePath = "";
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-filePath")) {
                filePath = args[argIndex + 1];
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int i = 0;
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);
        for (Tree tree : trainingTrees) {
            countTree(tree);
        }
        for (Map.Entry entry : labelAnnotationsMap.entrySet()) {
            if (entry.getValue() != (Integer) 0) System.err.println(entry.getKey() + " : " + entry.getValue());
        }
//        System.err.println(labelAnnotationsMap.toString());
    }
}
