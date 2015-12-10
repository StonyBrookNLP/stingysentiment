package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.*;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class InjectRandomNoise {

    private static long randomSeed = System.currentTimeMillis();

    public InjectRandomNoise() {
    }

    protected static void countTree(Tree tree) {
        if (tree.isPrePreTerminal()) {
            randomSeed *= 2;
            Random generator = new Random(randomSeed);
            Label label = tree.label();
            int random = ((int) (generator.nextDouble()*100)) % 5;
            label.setValue(((Integer)  random).toString());
            return;
        }
        for (Tree child : tree.children()) {
            if (child != null) countTree(child);
        }
    }

    public static void main(String[] args) {
        String filePath = "";
        int level = 0;
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-filePath")) {
                filePath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-level")) {
                level = Integer.parseInt(args[argIndex + 1]);
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int i = 0;
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);
        for (Tree tree : trainingTrees) {
            randomSeed = System.currentTimeMillis();
            countTree(tree);
            System.err.println(tree.toString());
        }
    }
}
