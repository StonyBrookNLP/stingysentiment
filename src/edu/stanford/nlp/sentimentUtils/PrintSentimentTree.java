package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.util.List;

/**
 * Created by root on 12/5/15.
 */
public class PrintSentimentTree {
    public PrintSentimentTree() {
    }

    public static void main(String[] args) {
        String filePath = "sample.txt";

        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);
        (new TreeToImage()).printTree(trainingTrees, "sentiment_tree");
    }
}
