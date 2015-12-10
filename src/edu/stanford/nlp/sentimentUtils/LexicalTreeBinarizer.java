package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.ling.CategoryWordTagFactory;
import edu.stanford.nlp.parser.lexparser.TreeBinarizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.Generics;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 12/5/15.
 */
public class LexicalTreeBinarizer {

    public LexicalTreeBinarizer() {
    }

    protected static List<Tree> binarizer(Treebank treebank) {
        HeadFinder hf = new ModCollinsHeadFinder();
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        boolean insideFactor = false;
        boolean mf = false;
        int mo = 1;
        boolean uwl = false;
        boolean uat = false;
        double sst = 20.0;
        boolean mfs = false;
        boolean simpleLabels = false;
        boolean noRebinarization = false;
        TreeReaderFactory trf = in -> new PennTreeReader(in,
                new LabeledScoredTreeFactory(
                        new CategoryWordTagFactory()),
                new BobChrisTreeNormalizer());
        List<Tree> newTreebank = new LinkedList<>();

        TreeTransformer tt = new TreeBinarizer(hf, tlp, insideFactor, mf, mo,
                uwl, uat, sst, mfs,
                simpleLabels, noRebinarization);
        for (Tree t : treebank) {
            Tree newT = tt.transformTree(t);
            newTreebank.add(newT);
        }
        return newTreebank;
    }
    private static void editNodeLabels(Tree tree) {
        for (Tree child : tree.children()) {
            String label = child.label().toString();
            if (label.contains("@")) {
                label = label.split(":")[0];
                label = label.replace("@", "").replace(" ","");
                child.setValue(label);
            }
            editNodeLabels(child);
        }
    }

    public static void main(String[] args) {
        String treePath = null;
        String sentenceFile = null;
        List<String> remainingArgs = Generics.newArrayList();
        for (int argIndex = 0; argIndex < args.length;) {
            if (args[argIndex].equalsIgnoreCase("-treebank")) {
                treePath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-sentenceFile")) {
                sentenceFile = args[argIndex + 1];
                argIndex += 2;
            } else {
                remainingArgs.add(args[argIndex]);
                argIndex++;
            }
        }
        String[] newArgs = new String[remainingArgs.size()];
        remainingArgs.toArray(newArgs);
        TreeReaderFactory trf = in -> new PennTreeReader(in,
                new LabeledScoredTreeFactory(
                        new CategoryWordTagFactory()),
                new BobChrisTreeNormalizer());
        Treebank treebank =  new DiskTreebank(trf);
        treebank.loadPath(new File(treePath));
        List<Tree> newTreebank = binarizer(treebank);
        for (Tree tree :  newTreebank) {
            editNodeLabels(tree);
            System.err.println(tree.toString());
        }
    }
}
