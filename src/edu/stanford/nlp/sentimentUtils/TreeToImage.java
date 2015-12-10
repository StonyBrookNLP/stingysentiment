package edu.stanford.nlp.sentimentUtils;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.Generics;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 12/5/15.
 */
public class TreeToImage {
    public TreeToImage() {

    }

    public static List<String> readFile(String filePath) {
        List<String> lines = new LinkedList<>();
        Path file = Paths.get(filePath);
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return lines;
    }


    private static void createSampleDotFile(List<Tree> treebank) {
        String fileName = "sampleTreeFile.dot";
        try {
            FileWriter fileWriter =
                    new FileWriter(fileName);
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            for (Tree tree : treebank) {
                bufferedWriter.write("digraph{");
                bufferedWriter.newLine();
                countTree(tree, tree, 0, bufferedWriter);
                bufferedWriter.write("}");
            }
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
        }
    }

    protected static void countTree(Tree tree, Tree root, int nodeNumber, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.write("Node" + tree.nodeNumber(root) + " [label=\"" + tree.value() + "\"]");
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tree.isLeaf()) {
            return;
        }
        for (Tree child : tree.children()) {
            countTree(child, root, nodeNumber, bufferedWriter);
            try {
                bufferedWriter.write("Node" + tree.nodeNumber(root) + " -> " + "Node" + child.nodeNumber(root));
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void printTree(List<Tree> treebank, String output) {
        createSampleDotFile(treebank);
        String sampleTreeFile = "sampleTreeFile.dot";
        String command = "dot -Tps " + sampleTreeFile + " -o " + output +".ps";
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String sentenceFile = null;
        String output = null;
        List<String> remainingArgs = Generics.newArrayList();
        for (int argIndex = 0; argIndex < args.length;) {
            if (args[argIndex].equalsIgnoreCase("-sentenceFile")) {
                sentenceFile = args[argIndex + 1];
                argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-o")) {
                output = args[argIndex + 1];
                argIndex += 2;
            } else {
                remainingArgs.add(args[argIndex]);
                argIndex++;
            }
        }
        if (sentenceFile == null) {
            System.err.println("Command Usage : java \"*\" edu.stanford.nlp.sentimentUtils.TreeToImage -sentenceFile sentiment_tree.txt -o sentiment_tree");
        } else if (output == null) {
            System.err.println("Please insert output file name extension(png, jpg) will be added automatically.");
        }
        String[] newArgs = new String[remainingArgs.size()];
        remainingArgs.toArray(newArgs);
        List<String> lines = new LinkedList<>();
        if (sentenceFile != null) lines = readFile(sentenceFile);
        int i = 1;
        /*Create the dot file from all the trees */
        List<Tree> treebank = new LinkedList<>();
        for (String line :lines) {
            try {
                Tree tree = (new PennTreeReader(new StringReader(line))).readTree();
                System.out.println(tree);
                treebank.add(tree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printTree(treebank, output);
    }
}
