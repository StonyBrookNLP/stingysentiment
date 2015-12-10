package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.sentiment.SentimentModel;
import edu.stanford.nlp.util.Generics;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

/**
 * Created by root on 12/5/15.
 */
public class  ModelAnalyser {
    public ModelAnalyser() {

    }

    public static void displayOutput(SentimentModel sentimentModel) {
        System.err.println(sentimentModel.toString());
    }

    public static SimpleMatrix checkWordExistence(SentimentModel model, String word) {
        return model.getWordVector(word);
    }

    protected static void diff(SentimentModel model1, SentimentModel model2) {
        /* check if the two models contain same number of words and similar vector and weights */
//        model1.get
    }

    public static void main(String[] args) {
        String modelPath = null;
        String modelPath1 = null;
        String modelPath2 = null;
        List<String> remainingArgs = Generics.newArrayList();
        for (int argIndex = 0; argIndex < args.length;) {
            if (args[argIndex].equalsIgnoreCase("-model")) {
                modelPath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-model1")) {
                modelPath1 = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-model2")) {
                modelPath2 = args[argIndex + 1];
                argIndex += 2;
            } else {
                remainingArgs.add(args[argIndex]);
                argIndex++;
            }
        }
        String[] newArgs = new String[remainingArgs.size()];
        remainingArgs.toArray(newArgs);
        SentimentModel model = null;
        SentimentModel model1 = null;
        SentimentModel model2 = null;
        if (modelPath != null) {
            model = SentimentModel.loadSerialized(modelPath);
            displayOutput(model);
        }
        if (modelPath1 != null) model = SentimentModel.loadSerialized(modelPath1);
        if (modelPath2 != null) model = SentimentModel.loadSerialized(modelPath2);
        if (model1 != null && model2 != null) {
            diff(model1, model2);
        } else if (!(model1 == null && model2 == null)) {
            System.err.println("Command Usage: java \"*\" edu.stanford.nlp.sentimentUtils.ModelAnalyser -model1 <Model1's Path relative to root> -model2 <Model2's Path relative to root");
        }
    }
}
