package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.List;
import java.util.Random;

/**
 * Created by root on 12/5/15.
 */
public class InjectNoise {
    public InjectNoise() {
    }

    private static long randomSeed = System.currentTimeMillis();
    private static long numberOfPhrase = 0, numberOfPhraseAffected = 0, labelChanged = 0;


    private static void changeLabel(Tree tree) {
        Label label = tree.label();
        Random generator = new Random(randomSeed);
        int  random = ((int) (generator.nextDouble()*100)) % 5;
        if (Integer.parseInt(label.value()) != random) {
            labelChanged++;
        }
        label.setValue(((Integer) random).toString());
    }

    protected static void changePhraseLabel(Tree tree, Tree root, int phraseLength) {
        for (Tree child : tree.children()) {
            if (child != null) changePhraseLabel(child, root, phraseLength);
        }
        if (tree.isPhrasal()) {
            numberOfPhrase++;
            if (tree.getLeaves().size() == phraseLength) {
                numberOfPhraseAffected++;
                changeLabel(tree);
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "";
        String priorFilePath = "";
        Integer phraseLength = null;
        boolean alternate = false;

        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-filePath")) {
                filePath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-phraseLength")) {
                phraseLength = Integer.parseInt(args[argIndex + 1]);
                argIndex += 2;
            }  else if (args[argIndex].equalsIgnoreCase("-alternate")) {
                alternate = true;
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int i = 0;
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);
        if (alternate) {
            for (Tree tree : trainingTrees) {
                randomSeed = System.currentTimeMillis();
                for (int j = 3; j < tree.getLeaves().size(); j+=2) {
                    changePhraseLabel(tree, tree, j);
                }
                System.err.println(tree.toString());
            }

        } else {
            if (phraseLength == null) {
                System.err.println("Commang Usage : java \"*\" edu.stanford.nlp.sentimentUtils -filePath <path for test.txt relative to root> -phraseLength <2,3>");
            }
            for (Tree tree : trainingTrees) {
                randomSeed = System.currentTimeMillis();
                for (int j = 2; j <= phraseLength; j++) {
                    changePhraseLabel(tree, tree, j);
                }
                System.err.println(tree.toString());
            }
        }
        System.out.println("Number of phrases = " + numberOfPhrase);
        System.out.println("Number of phrases affected= " + numberOfPhraseAffected);
        System.out.println("Labels Changed= " + labelChanged);
    }
}
