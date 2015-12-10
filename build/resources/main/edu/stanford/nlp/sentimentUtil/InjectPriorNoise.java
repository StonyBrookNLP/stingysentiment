package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class InjectPriorNoise {

    private static long randomSeed = System.currentTimeMillis();
    private static Map<String, Integer> priorHash = new HashMap<>();
    private static Integer result = 0;

    public InjectPriorNoise() {
    }

    protected static String convertLabelToSign(Label label) {
        if (label.value() == "0" || label.value() == "1") return "-";
        if (label.value() == "3" || label.value() == "4") return "+";
        else return "0";
    }

    protected static void countTree(Tree tree, Label parentLabel) {
        if (tree.isPrePreTerminal()) {
            Label label = tree.label();
            String key = convertLabelToSign(parentLabel);
            for (Tree child : tree.children()) {
                key += convertLabelToSign(child.label());
            }
            int value = priorHash.get(key);
            result++;
            label.setValue(((Integer) value).toString());
            return;
        }
        for (Tree child : tree.children()) {
            if (child != null) countTree(child, tree.label());
        }
    }

    protected static void convertPrioriFileIntoHashMap(String filePath) {
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    String pair[] = line.split(",");
                    String key = pair[0];
                    Integer value = Integer.parseInt(pair[1]);
                    priorHash.put(key, value);
                }
            } catch (Exception ex) {
                System.err.println("Exception while reading the line from the file");
            }
        } catch(Exception ex) {
            System.err.println("Exception while reading the file");
        }
    }

    public static void main(String[] args) {
        String filePath = "";
        String priorFilePath = "";
        int level = 0;
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-filePath")) {
                filePath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-prior")) {
                priorFilePath = args[argIndex + 1];
                argIndex += 2;
            }  else if (args[argIndex].equalsIgnoreCase("-level")) {
                level = Integer.parseInt(args[argIndex + 1]);
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        int i = 0;
        convertPrioriFileIntoHashMap(priorFilePath);
        System.err.println(priorHash);
        List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(filePath);
        for (Tree tree : trainingTrees) {
            randomSeed = System.currentTimeMillis();
            countTree(tree, tree.label());
            System.err.println(tree.toString());
        }
        System.err.println(result);
    }
}
