package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 12/5/15.
 */
public class PhraseLengthDistribution {
    private static long numberOfPhrase = 0;

    protected static void countPhrase(Tree tree) {
        for (Tree child : tree.children()) {
            if (child != null) countPhrase(child);
        }
        if (tree.isPhrasal()) {
            numberOfPhrase++;
        }
    }

    public static void main(String[] args) {
        /* Variable Initializations */
        String filePath = "";
        List<Tree> veryNegative = new LinkedList<>();
        List<Tree> negative = new LinkedList<>();
        List<Tree> neutral = new LinkedList<>();
        List<Tree> positive = new LinkedList<>();
        List<Tree> veryPositive = new LinkedList<>();
        int phraseLimit = 0;

        /* Reading command line arguments */
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-filePath")) {
                filePath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-filePath")) {
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }

        /* Reading sentences and converting them into trees from input file */
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);

        /* Distributing tree on the basis of root labels */
        for (Tree tree : trainingTrees) {
            System.err.println(tree.pennString());
            break;
        }
    }
}