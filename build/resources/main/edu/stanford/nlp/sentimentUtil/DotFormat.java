package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 10/16/15.
 */
public class DotFormat {
    private static List<Set<Tree>> treePath = new LinkedList<>();

    public DotFormat() {

    }

    protected static void countTree(Tree tree, Tree root, int nodeNumber) {
        System.err.println("Node" + tree.nodeNumber(root) + " [label=\"" + tree.value() + "\"]");
        int i = 1;
        if (tree.isLeaf()) {
            return;
        }
        for (Tree child : tree.children()) {
            countTree(child, root, nodeNumber);
            System.err.println("Node" + tree.nodeNumber(root) + " -> " + "Node" + child.nodeNumber(root));
            i++;
        }
    }

    public static void main(String[] args) {
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
            System.err.println("digraph G{");
            countTree(tree, tree, 0);
            System.err.println("}");
        }
    }
}
