package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class TreeDepthDistribution {

    private static Map<Integer , Integer> treeDepthMap = new HashMap<>();

    public TreeDepthDistribution() {
    }

    public static void main(String[] args) {
        String filePath = "";
        int level = 0;
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
            int key = tree.depth();
            int val = (treeDepthMap.get(key) == null) ? 0 : treeDepthMap.get(key);
            treeDepthMap.put(key, val + 1);
        }
        for (Map.Entry entry : treeDepthMap.entrySet()) {
            if (entry.getValue() != (Integer) 0) System.err.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
