package edu.stanford.nlp.sentimentUtil;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.trees.Tree;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/** @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/6/15.
 */
public class ModifyTreeBasedLogisticReg {

    private static long randomSeed = System.currentTimeMillis();
    private static double[][] coef = {{4.37841126e+00, 3.08620176e+00, 1.01774564e+00, -2.79829727e-01, 4.51326362e+00, 3.26203444e+00, 9.43267862e-01, -4.44302855e-01, 4.66458918e-01, 3.50016594e-01, 2.72220961e-01, 2.05917688e-01, 2.08448885e-01, 2.73034912e-02, -1.00211844e-02, 1.05562588e-01, -1.49733720e-01, -4.61478910e-02, 5.96202972e-03, 9.77804754e-02, 4.95524156e-02, -3.82646356e-01, -8.36148355e-02, 2.69187560e-01, -5.88660037e-01, 2.62102444e-01, -4.74889501e-01, -2.20776414e-01, -1.61816617e-01, -1.32379325e-01, -6.34054641e-02, -6.43984947e-03, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00},
            {3.35734679e+00, 3.26195202e+00, 1.84579241e+00, 5.86882335e-01, 3.22030710e+00, 3.17108716e+00, 1.68750275e+00, 5.49761740e-01, -1.68249330e-01, -2.24061889e-02, -1.44553167e-02, -1.16302189e-02, -3.69509443e-02, 5.72430058e-02, 1.24967542e-02, 6.58227065e-02, 8.04122360e-02, -7.57403232e-02, -4.80901110e-02, -4.64824737e-02, 1.19806362e-01, -7.19091948e-03, 6.28276608e-03, -3.19619951e-02, 4.84236280e-01, -9.75629035e-02, 3.04573534e-01, -4.04726015e-02, -1.49986502e-01, 1.45282577e-02, -3.30758085e-01, -6.34713716e-02, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00},
            {2.86722760e-01, 1.45757805e+00, 1.97474152e+00, 1.21292461e+00, 4.78342103e-01, 1.54704529e+00, 2.18262974e+00, 1.37638849e+00, -8.24064630e-01, -7.83216528e-01, -6.17263973e-01, -4.74162426e-01, -3.92931088e-01, -3.01328234e-01, -1.72287674e-01, -1.55698394e-01, -7.37672487e-02, 3.42379265e-04, -2.20590883e-02, -3.96358280e-02, 6.76541524e-02, 2.13485342e-01, 1.58763444e-01, 3.63574866e-01, 5.30124501e-01, 2.12580238e-01, 5.63471631e-01, 3.40003020e-01, 4.74789197e-01, 1.03084846e-01, 6.72666705e-01, 1.55873469e-01, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00},
            {-3.44112364e+00, -2.44472692e+00, -1.35628147e+00, -1.40618302e-01, -3.32986191e+00, -2.59357438e+00, -1.32380382e+00, -6.76291641e-02, -1.43467494e-01, -9.41305328e-02, -8.56556086e-02, -1.02287098e-02, -8.58953142e-02, -2.38575469e-02, -5.24003876e-02, -1.40098184e-02, -3.09206072e-02, -2.80467435e-02, -7.95838055e-02, -6.10845587e-02, -1.64380364e-01, -1.12525537e-01, 1.05604640e-01, -3.21944641e-01, 4.20223667e-01, 1.01723541e-01, 2.86206017e-01, 1.56623743e-01, 2.70506754e-01, 8.62789311e-02, -3.81497194e-02, -8.08755618e-02, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00},
            {-4.58135717e+00, -5.36100491e+00, -3.48199810e+00, -1.37935891e+00, -4.88205091e+00, -5.38659251e+00, -3.48959653e+00, -1.41421822e+00, 6.69322536e-01, 5.49736656e-01, 4.45153937e-01, 2.90103667e-01, 3.07328461e-01, 2.40639284e-01, 2.22212491e-01, -1.67708240e-03, 1.74009340e-01, 1.49592578e-01, 1.43770975e-01, 4.94223848e-02, -7.26325663e-02, 2.88877471e-01, -1.87036014e-01, -2.78855790e-01, -8.45924410e-01, -4.78843321e-01, -6.79361682e-01, -2.35377747e-01, -4.33492832e-01, -7.15127098e-02, -2.40353436e-01, -5.08668613e-03, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00}};
    private static double[] intercept = {-3.84626965, -3.07342403, -1.32912944, 3.39968831, 4.84913481};

    public ModifyTreeBasedLogisticReg() {
    }

    protected static double[] convertChildLabelToVector(Label label) {
        double[] result = {0,0,0,0};
        if (label.value().equals("0") || label.value().equals("1") || label.value().equals("2") || label.value().equals("3")) {
            result[Integer.parseInt(label.value())] = 1;
        } else if (!label.value().equals("4")) {
            result = null;
        }
        return result;
    }

    protected static double[] convertDepthToVector(Tree tree, Tree root) {
        double[] result = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if (root.depth(tree) < 30 && root.depth(tree) > 0) {
            result[root.depth(tree) - 1] = 1;
        } else if (root.depth(tree) != 30) {
            result = null;
        }
        System.err.println("Depth of the current Node : " + root.depth(tree));
        return result;
    }

    protected static void changeLabel(Tree tree, Tree root) {
        Label label = tree.label();
        double[] result = {};
        for (Tree child : tree.children()) {
            result = ArrayUtils.addAll(result, convertChildLabelToVector(child.label()));
        }
        System.err.println("Current Node -> " + tree.toString());
        System.err.println("Vector from children : " + Arrays.toString(result));
        result = ArrayUtils.addAll(result, convertDepthToVector(tree, root));
        System.err.println("Vector after including depth : " + Arrays.toString(result));
        LogisticClassifier logisticClassifier = new LogisticClassifier(coef, intercept, result);
        int value = logisticClassifier.getNodeLabel();
        label.setValue(((Integer) value).toString());
    }

    protected static void countTree(Tree tree, Tree root) {
        if (tree.isPrePreTerminal()) {
            return;
        }
        for (Tree child : tree.children()) {
            if (child != null) countTree(child, root);
            if (!child.isPreTerminal() && !child.isLeaf()) changeLabel(child, root);
        }
        if (tree != root && !tree.isLeaf() && !tree.isPreTerminal()) changeLabel(tree, root);
    }

    public static void main(String[] args) {
        String filePath = "";
        String priorFilePath = "";
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
            countTree(tree, tree);
            if (i++ > 1) break;
            System.err.println(tree.toString());
        }
    }
}
