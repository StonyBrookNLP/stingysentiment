package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.*;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class PhraseModifier {

    private static int[][] labelConfusion;
    private static Map<String, Integer> labelTrainMap = new HashMap<>();
    private static Set<String> notFoundLeafNodes = new HashSet<>();
    private static int[][] leafLabelConfusion = new int[5][5];
    private static int leafMinus1Phrases = 0, leafMinus2Phrases = 0, leaves = 0;
    private static Set<String> phraseLeaves1 = new HashSet<>();
    private static Set<String> phraseLeaves2 = new HashSet<>();
    private static Set<String> phraseLeaves = new HashSet<>();

    public PhraseModifier() {
    }

    protected static void countTrainTree(Tree tree) {
        if (tree.isPreTerminal()) {
            phraseLeaves.add(tree.toString());
            return;
        }
        if (tree.isPrePreTerminal()) {
            phraseLeaves1.add(tree.toString());
        }
        for (Tree child : tree.children()) {
//            if (child.isPrePreTerminal()) {
//                phraseLeaves2.add(tree.toString());
//            }
            phraseLeaves2.add(tree.toString());
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
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int k = 0;
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(trainPath);
        for (Tree tree : trainingTrees) {
            countTrainTree(tree);
        }
        System.err.println("Phrase at Leaf level = " + phraseLeaves.size());
//        for (String string : phraseLeaves) {
//            System.err.println(string);
//        }
        System.err.println("Phrase at Leaf - 1 level = " + phraseLeaves1.size());
//        for (String string : phraseLeaves1) {
//            System.err.println(string);
//        }
        System.err.println("Phrase at Leaf - 2 level = " + phraseLeaves2.size());
//        for (String string : phraseLeaves2) {
//            System.err.println(string);
//        }
    }
}
