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
public class ModifyPhraseBasedLogisticReg {

    private static long randomSeed = System.currentTimeMillis();
    private static long numberOfPhrase = 0, numberOfPhraseAffected = 0, numberOfPrePreterminal = 0, numberOfPreterminal = 0;
    private static double[][] coef = {{  4.58273089e+00,   3.24560747e+00,   1.21205189e+00,
            -1.73454847e-01,   4.62402412e+00,   3.35706015e+00,
            1.14106611e+00,  -4.41534522e-01,   0.00000000e+00,
            -9.59667875e-01,  -9.03365858e-01,  -5.94960475e-01,
            -5.04844777e-01,  -3.44387895e-01,  -2.95704868e-01,
            -2.93760411e-01,  -2.32517057e-01,  -3.70864313e-01,
            -1.72694083e-01,  -2.16415689e-01,  -3.75799051e-01,
            -8.02183499e-02,  -3.29884020e-01,  -1.97947880e-01,
            -5.05450202e-02,  -7.34203010e-02,   3.92022512e-02,
            -3.91938592e-01,   2.87606332e-01,   3.49482758e-01,
            1.57799214e-01,   1.23072694e-01,   2.63200430e-01,
            2.48086487e-01,   8.15204110e-02,   1.36094428e-02,
            5.86640299e-01,   5.65985259e-01,   3.96612464e-01,
            5.71625465e-01,   2.33937096e-01,   1.94902016e-01,
            -7.22118497e-02,   2.94348291e-01,  -2.20193653e-01,
            4.43370099e-01,   2.33175838e-01,  -4.87369882e-01,
            2.80895804e-01,   1.10060051e-01,   1.84973478e-01,
            -1.90936812e-02,   1.71494729e-01,   3.16547869e-01,
            -1.72025724e-01,  -1.20703188e-01,  -4.07937389e-02,
            5.58521315e-01,   5.01114743e-01,   3.13470409e-01,
            0.00000000e+00,   0.00000000e+00},
            {  3.29420230e+00,   3.17976783e+00,   1.82078039e+00,
                    5.82848636e-01,   3.31637837e+00,   3.23995307e+00,
                    1.74501941e+00,   6.12530569e-01,   0.00000000e+00,
                    1.12802440e-01,  -1.80006190e-02,   6.83042543e-02,
                    -4.13258257e-03,   8.31229631e-02,   1.68750179e-02,
                    1.10253452e-01,   1.72049111e-01,   1.66675409e-01,
                    2.13755425e-01,   2.11889767e-01,   9.14442323e-02,
                    2.42459504e-02,   1.58717775e-01,   1.72388601e-01,
                    1.20021531e-01,  -3.49736829e-02,   1.33877212e-01,
                    1.03365376e-01,   7.36984896e-02,   8.74411918e-02,
                    1.04604119e-01,  -9.71838218e-02,   6.75257564e-02,
                    1.02896634e-01,   2.75758771e-02,   2.25215121e-02,
                    2.76388986e-02,   5.25580607e-02,  -2.15544665e-02,
                    7.82409223e-02,  -5.65836651e-02,   2.41993347e-01,
                    -6.60314468e-02,  -6.32609020e-01,   8.72127320e-02,
                    -6.07336858e-02,   6.51680407e-02,   2.12143230e-01,
                    6.54298144e-01,  -3.47054145e-01,  -1.08411445e-01,
                    -3.21280808e-01,  -5.07197083e-01,   2.51227649e-01,
                    -3.34507252e-01,   5.72569073e-01,  -8.41976395e-01,
                    -5.24239048e-01,  -4.44395503e-01,  -2.68287876e-01,
                    0.00000000e+00,   0.00000000e+00},
            {  3.48701532e-01,   1.47727105e+00,   1.97093973e+00,
                    1.27650295e+00,   5.01468444e-01,   1.54122834e+00,
                    2.00892895e+00,   1.37430838e+00,   0.00000000e+00,
                    1.35465832e+00,   9.65118986e-01,   6.78019303e-01,
                    5.44969334e-01,   4.28816517e-01,   2.76586612e-01,
                    3.09733632e-01,   2.38080542e-01,   1.79168146e-01,
                    1.55272885e-01,   2.11042506e-01,   1.44782721e-01,
                    1.54120752e-02,   1.99189486e-01,  -3.66956066e-02,
                    -1.19211680e-01,  -1.08267136e-01,  -9.63678555e-02,
                    1.51753417e-01,  -1.52855587e-01,  -1.41651768e-01,
                    -1.39333669e-01,  -1.18877285e-01,  -1.14897162e-01,
                    3.65324876e-02,  -2.14843380e-01,  -1.03531654e-01,
                    -1.52889765e-01,  -1.35243674e-01,  -4.53844563e-01,
                    -2.58192966e-01,  -5.57347155e-01,  -1.99307416e-01,
                    -5.49706054e-01,  -7.07543815e-01,   7.23310076e-02,
                    -3.68886733e-01,  -6.77024450e-02,   1.91134753e-01,
                    -4.25956388e-01,   3.13061078e-02,  -4.99393895e-01,
                    4.15336957e-01,  -9.59214935e-02,  -6.50668323e-01,
                    8.50880222e-01,  -4.00319907e-01,   7.00532079e-02,
                    -7.22405733e-01,   1.64277125e-01,  -9.26230667e-02,
                    0.00000000e+00,   0.00000000e+00},
            { -3.51739363e+00,  -2.44914685e+00,  -1.39906189e+00,
                    -1.77012154e-01,  -3.40822226e+00,  -2.63537293e+00,
                    -1.38192993e+00,  -8.48826196e-02,   0.00000000e+00,
                    7.78208460e-02,   1.72673685e-01,   1.18754289e-01,
                    1.23323292e-01,   8.29032553e-02,  -1.74118597e-02,
                    1.47773478e-02,   6.88186395e-02,   8.83822462e-02,
                    -5.71202354e-02,  -6.67079000e-02,   1.15768032e-01,
                    -7.28936647e-02,  -3.51744202e-02,  -4.17387088e-02,
                    -1.23257628e-01,  -6.23125966e-03,  -6.99456000e-02,
                    4.56024461e-02,  -2.38766833e-01,  -2.91559176e-01,
                    -8.97447102e-02,   1.27936530e-01,   1.57712954e-01,
                    -2.20936659e-01,   3.85406952e-02,  -6.69699858e-02,
                    -2.02781282e-01,  -1.42508131e-01,  -7.95707221e-03,
                    -5.11967754e-01,  -5.86426853e-02,  -1.79035083e-01,
                    2.21147804e-01,   4.64806104e-01,  -2.02657031e-01,
                    1.47496308e-01,   2.44346938e-02,   5.43295579e-01,
                    -5.39436479e-01,  -3.48243123e-01,  -1.44847998e-01,
                    -2.61844712e-01,  -4.95312752e-02,  -3.03142006e-01,
                    2.67854283e-01,   5.52143829e-02,   1.03268889e+00,
                    1.02823787e+00,  -3.50502369e-01,  -3.16575725e-01,
                    0.00000000e+00,   0.00000000e+00},
            { -4.70824110e+00,  -5.45349949e+00,  -3.60471012e+00,
                    -1.50888458e+00,  -5.03364866e+00,  -5.50286865e+00,
                    -3.51308454e+00,  -1.46042181e+00,   0.00000000e+00,
                    -5.85613735e-01,  -2.16426194e-01,  -2.70117371e-01,
                    -1.59315266e-01,  -2.50454841e-01,   1.96550975e-02,
                    -1.41004022e-01,  -2.46431236e-01,  -6.33614881e-02,
                    -1.39213992e-01,  -1.39808684e-01,   2.38040660e-02,
                    1.13453989e-01,   7.15117851e-03,   1.03993595e-01,
                    1.72992797e-01,   2.22892379e-01,  -6.76600749e-03,
                    9.12173527e-02,   3.03175983e-02,  -3.71300466e-03,
                    -3.33249528e-02,  -3.49481171e-02,  -3.73541978e-01,
                    -1.66578949e-01,   6.72063970e-02,   1.34370685e-01,
                    -2.58608151e-01,  -3.40791515e-01,   8.67436378e-02,
                    1.20294333e-01,   4.38636410e-01,  -5.85528642e-02,
                    4.66801546e-01,   5.80998441e-01,   2.63306945e-01,
                    -1.61245988e-01,  -2.55076127e-01,  -4.59203680e-01,
                    3.01989182e-02,   5.53931109e-01,   5.67679861e-01,
                    1.86882245e-01,   4.81155122e-01,   3.86034811e-01,
                    -6.12201528e-01,  -1.06760361e-01,  -2.19971968e-01,
                    -3.40114399e-01,   1.29506005e-01,   3.64016260e-01,
                    0.00000000e+00,   0.00000000e+00}};
    private static double[] intercept = {-3.47826734, -3.18395268, -2.27364298,  3.3479172 ,  5.58794579};


    public ModifyPhraseBasedLogisticReg() {
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
        double[] result = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if (root.depth(tree) < 30 && root.depth(tree) > 0) {
            result[root.depth(tree) - 1] = 1;
        } else if (root.depth(tree) != 30) {
            result = null;
        }
//        System.err.println("Depth of the current Node : " + root.depth(tree));
        return result;
    }

    protected static double[] prePrePreTerminal() {
        double[] result = {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        return result;
    }

    protected static double[] prePreTerminal() {
        double[] result = {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        return result;
    }

    protected static void changeLabel(Tree tree, double[] phraseVector) {
        Label label = tree.label();
        double[] result = {};
        for (Tree child : tree.children()) {
            result = ArrayUtils.addAll(result, convertChildLabelToVector(child.label()));
        }
//        System.err.println("Current Node -> " + tree.toString());
//        System.err.println("Vector from children : " + Arrays.toString(result));
        result = ArrayUtils.addAll(result, phraseVector);
//        System.err.println("Vector after including depth : " + Arrays.toString(result));
        LogisticClassifier logisticClassifier = new LogisticClassifier(coef, intercept, result);
        int value = logisticClassifier.getNodeLabel();
        label.setValue(((Integer) value).toString());
    }

    protected static void countTree(Tree tree, Tree root) {
        for (Tree child : tree.children()) {
            if (child != null) countTree(child, root);
        }
        if (tree.isPreTerminal()) numberOfPreterminal++;
        if (tree.isPrePreTerminal()) {
            changeLabel(tree, prePreTerminal());
            numberOfPrePreterminal++;
        }
        if (tree.isPhrasal()) {
            numberOfPhrase++;
            if ((tree.children()[0].isPrePreTerminal() && tree.children()[1].isPreTerminal()) || (tree.children()[0].isPreTerminal() && tree.children()[1].isPrePreTerminal())) {
                numberOfPhraseAffected++;
                changeLabel(tree, prePrePreTerminal());
            }
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
//            if (i++ > 1) break;
            System.err.println(tree.toString());
        }
        System.out.println("Number of phrases = " + numberOfPhrase);
        System.out.println("Number of preterminal = " + numberOfPreterminal);
        System.out.println("Number of prepreterminal = " + numberOfPrePreterminal);
        System.out.println("Number of phrases affected= " + numberOfPhraseAffected);
    }
}
