package edu.stanford.nlp.sentimentUtil;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ankit.gupta.1@stonybrook.edu
 * Created by root on 10/5/15.
 */
public class DatasetTrimmer {

    public DatasetTrimmer() {

    }

    public static void main(String[] args) {
        String inputPath = "";
        String outputPath = "";
        int sentenceNum = 0;
        for (int argIndex = 0; argIndex < args.length; ) {
            if (args[argIndex].equalsIgnoreCase("-inputPath")) {
                inputPath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-outputPath")) {
                outputPath = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-sentenceNum")) {
                sentenceNum = Integer.parseInt(args[argIndex + 1]);
                argIndex += 2;
            } else {
                throw new IllegalArgumentException("Unknown argument " + args[argIndex]);
            }
        }
        try {
            System.err.println(inputPath);
            File file = new File(inputPath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> positiveSentences, negativeSentences, veryPositiveSentences, veryNegativeSentences, neutralSentences;
            positiveSentences = new LinkedList<>();
            negativeSentences = new LinkedList<>();
            veryPositiveSentences = new LinkedList<>();
            veryNegativeSentences = new LinkedList<>();
            neutralSentences = new LinkedList<>();
            String line = "";
            String veryNegativeSentence = "";
            String negativeSentence = "";
            String neutralSentence = "";
            String positiveSentence = "";
            String veryPositiveSentence = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.charAt(1) == '0') veryNegativeSentences.add(line);
                    if (line.charAt(1) == '1') negativeSentences.add(line);
                    if (line.charAt(1) == '2') neutralSentences.add(line);
                    if (line.charAt(1) == '3') positiveSentences.add(line);
                    if (line.charAt(1) == '4') veryPositiveSentences.add(line);
                }
                System.err.println(outputPath);
                File fileOut = new File(outputPath);
                FileWriter fileWriter = new FileWriter(fileOut, false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < sentenceNum/5; i++) {
                    veryNegativeSentence = (veryNegativeSentences.size() <= i) ? veryNegativeSentences.get((int )(Math.random() * veryNegativeSentences.size())) : veryNegativeSentences.get(i);
                    negativeSentence = (negativeSentences.size() <= i) ? negativeSentences.get((int )(Math.random() * negativeSentences.size())) : negativeSentences.get(i);
                    neutralSentence = (neutralSentences.size() <= i) ? neutralSentences.get((int )(Math.random() * neutralSentences.size())) : neutralSentences.get(i);
                    positiveSentence = (positiveSentences.size() <= i) ? positiveSentences.get((int )(Math.random() * positiveSentences.size())) : positiveSentences.get(i);
                    veryPositiveSentence = (veryPositiveSentences.size() <= i) ? veryPositiveSentences.get((int )(Math.random() * veryPositiveSentences.size())) : veryPositiveSentences.get(i);
                    bufferedWriter.write(veryNegativeSentence + "\n");
                    bufferedWriter.write(negativeSentence + "\n");
                    bufferedWriter.write(neutralSentence + "\n");
                    bufferedWriter.write(positiveSentence + "\n");
                    bufferedWriter.write(veryPositiveSentence + "\n");
                }
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException ex) {
                throw new IOException("Line not valid");
            }
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("FileNotFoundException thrown : " + inputPath);
        } catch (IOException ex) {
            throw new IllegalArgumentException("IOException thrown : " + inputPath);
        }
    }
}
