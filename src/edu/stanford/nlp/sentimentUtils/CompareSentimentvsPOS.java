package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.ling.CategoryWordTagFactory;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 12/5/15.
 */
public class CompareSentimentvsPOS {

    public CompareSentimentvsPOS() {
    }

    protected static Map<String, Integer> POSSentimentCounter = new HashMap<String, Integer>();

    private static void comparator(Tree lexicalTree, Tree sentimentTree) {
        int i = 0;
        int value;
        if (POSSentimentCounter.get(lexicalTree.label().toString()) == null) value = 0;
        else value = POSSentimentCounter.get(lexicalTree.label().toString());
        POSSentimentCounter.put(lexicalTree.label().toString(), value++);
        for (Tree child : lexicalTree.children()) {
            System.err.println("1sentiment child = " +sentimentTree.getChild(i)+" i="+i);
            System.err.println("2sentiment child = " +sentimentTree.getChild(i)+" i="+i);
            if (!child.isPreTerminal()) comparator(child, sentimentTree.getChild(i++));
        }
    }
    public static void main(String[] args) {
        String sentimentTrees = null;
        String lexicalTrees = null;
        int phraseLength = 0;

        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-sentimentTrees")) {
                sentimentTrees = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-lexicalTrees")) {
                lexicalTrees = args[argIndex + 1];
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int i = 0;
        TreeReaderFactory trf = in -> new PennTreeReader(in,
                new LabeledScoredTreeFactory(
                        new CategoryWordTagFactory()),
                new BobChrisTreeNormalizer());
        Treebank treebank =  new DiskTreebank(trf);
        treebank.loadPath(new File(lexicalTrees));
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(sentimentTrees);
        for (Tree lexicalTree : treebank) {
            Tree sentimentTree = trainingTrees.get(i++);
            comparator(lexicalTree, sentimentTree);
//            System.err.println(sentimentTree.getLeaves());
        }
        System.err.println(POSSentimentCounter);
    }
}