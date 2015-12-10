package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/5/15.
 * Function : Will create the dataset after counting the number of phrases covered by the sentences.
 */
public class DatasetShorten {

    private static long numberOfPhrase = 0;
    public DatasetShorten() {

    }

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
            } else if (args[argIndex].equalsIgnoreCase("-phraseLimit")) {
                phraseLimit = Integer.parseInt(args[argIndex + 1]);
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }

        /* Reading sentences and converting them into trees from input file */
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(inputPath);

        /* Distributing tree on the basis of root labels */
        for (Tree tree : trainingTrees) {
            switch ((int) tree.score()) {
                case 0:
                    veryNegative.add(tree);
                    break;
                case 1:
                    negative.add(tree);
                    break;
                case 2:
                    neutral.add(tree);
                    break;
                case 3:
                    positive.add(tree);
                    break;
                case 4:
                    veryPositive.add(tree);
                    break;
            }
        }

        /* Displaying sentences from each class one by one and stopping if phraseLimit is crossed*/
        int i = 0;
        while (numberOfPhrase > phraseLimit) {
            countPhrase(veryNegative.get(i));
            countPhrase(negative.get(i));
            countPhrase(nuetral.get(i));
            countPhrase(positive.get(i));
            countPhrase(veryPositive.get(i));
            System.err.println(veryNegative.get(i).toString());
            System.err.println(negative.get(i).toString());
            System.err.println(nuetral.get(i).toString());
            System.err.println(positive.get(i).toString());
            System.err.println(veryPositive.get(i).toString());
            i++;
        }
    }
}
