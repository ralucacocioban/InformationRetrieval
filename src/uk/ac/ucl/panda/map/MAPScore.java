package uk.ac.ucl.panda.map;

import uk.ac.ucl.panda.utility.io.FileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Use this class to implement the Mean Average Precision (MAP) metric by
 * completing the getMAPScore() method below.
 *
 * @author Marc Sloan
 */
public class MAPScore {

    /**
     * -----------------------------------------------
     * Implement this method to
     * calculate the MAP score. The getResultsFromFile method below locates the
     * results file and stores them in a ResultsList object. Likewise the
     * getQRelsFromFile method locates the qrels file and stores the qrels in a
     * QRelList object
     *
     * @return the MAP score
     * @see uk.ac.ucl.panda.map.QRelList
     * @see uk.ac.ucl.panda.map.ResultsList
     */
    public static double getMAPScore() {
        ResultsList results = getResultsFromFile();
        QRelList qrels = getQRelsFromFile();

        Integer[] topics = results.getTopics();
        ArrayList<Double> avgPrecisions = new ArrayList<Double>();

        /* iterate through each topic/query */
        for (Integer topic : topics) {

            int relevantDocsRetrieved = 0;
            int relevantDocsPerTopic = getRelevantDocs(qrels.getTopicQRels(topic));

            ArrayList<ResultsList.Result> resultsForTopic = results.getTopicResults(topic);
            ArrayList<Double> precision = new ArrayList<Double>();

            /* get the relevance judgements for the current topic */
            HashMap<String, Boolean> relevancy = qrels.getTopicQRels(topic);

            for (int j = 0; j < resultsForTopic.size(); j++) {

                /* we need to store only the precisions for the relevant documents */

                /* check if the hashTable contains the document id and if the document is relevant */
                if (relevancy.containsKey(resultsForTopic.get(j).docID) && relevancy.get(resultsForTopic.get(j).docID)){

                    /* here the current document (j-th document in the results) is relevant
                    * -> we need to calculate and store it's precision
                    */
                    relevantDocsRetrieved++;
                    precision.add((double) (relevantDocsRetrieved) / (resultsForTopic.get(j).rank + 1));
                }
            }
            getAvgPrecision(precision, relevantDocsPerTopic, avgPrecisions);
        }

        return calculateAvgMeanPrecision(avgPrecisions);
    }


    /* averages the precisions calculated at each relevant document retrieved */
    public static void getAvgPrecision(ArrayList<Double> precision, int relevantDocsForTop,
                                       ArrayList<Double> avgPrecisions) {

        if (precision.size() > 0) {
            double sum = 0.0;

            /* calculate the average precision for the relevant documents for the current topic */
            for (Double val : precision) {
                sum += val;
            }

            avgPrecisions.add(sum / (double) relevantDocsForTop);
        } else {
            avgPrecisions.add(0.0);
        }
    }


    /* calculates how many relevant documents are for each topic */
    public static int getRelevantDocs(HashMap<String, Boolean> relevancyInformation) {

        int nbRelDocs = 0;

        for (String key : relevancyInformation.keySet()) {
            if (relevancyInformation.get(key) == true) {
                nbRelDocs++;
            }
        }

        return nbRelDocs;
    }

    /* calculate the average mean precision based on the array that contains precisions for each rel doc retrieved */

    public static double calculateAvgMeanPrecision(ArrayList<Double> avgPrecisions) {

        double avgMeanPrecision;
        double sum = 0.0;

        for (Double p : avgPrecisions) {
            sum += p;
        }

        avgMeanPrecision = sum / avgPrecisions.size();

        return avgMeanPrecision;
    }

    /**
     * Can be used to run your getMAPScore() method and prints out the MAP score for testing purposes
     */
    public static void main(String args[]) {
        System.out.println("MAP = " + getMAPScore());
    }

    /**
     * etc and var folder locations
     */
    private final static String PANDA_ETC = System.getProperty("panda.etc",
            "./etc/");
    private final static String PANDA_VAR = System.getProperty("panda.var",
            "./var/");
    protected static String fileseparator = System
            .getProperty("file.separator");

    /**
     * This method locates the results file (by looking in the Panda/var/
     * folder), opens it, then iterates through every line, saving each
     * individual ranked document for each topic in a ResultsList object
     *
     * @return ResultsList representing the results file
     * @see uk.ac.ucl.panda.map.ResultsList
     */
    public static ResultsList getResultsFromFile() {
        // Opens the results file in the Panda/var/ folder
        FileInputStream resultsFile = null;
        ResultsList resultsArray = new ResultsList();
        try {
            resultsFile = new FileInputStream(PANDA_VAR + fileseparator
                    + "results");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (resultsFile == null)
            return resultsArray;

        // Read in the file
        DataInputStream resultsStream = new DataInputStream(resultsFile);
        BufferedReader results = new BufferedReader(new InputStreamReader(
                resultsStream));

        StringTokenizer rToken;
        String rLine;
        String topic;
        String docID;
        String rank;
        String score;

        try {
            // iterate through every line in the file
            while ((rLine = results.readLine()) != null) {
                rToken = new StringTokenizer(rLine);
                // extract the meaningful information
                topic = rToken.nextToken();
                rToken.nextToken();
                docID = rToken.nextToken();
                rank = rToken.nextToken();
                score = rToken.nextToken();

                // add this result to our ResultsList
                resultsArray.addResult(Integer.parseInt(topic), docID,
                        Integer.parseInt(rank), Double.parseDouble(score));

            }
            if (resultsFile != null)
                resultsFile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resultsArray;
    }

    /**
     * This method locates the qrels file (by looking in the Panda/etc/ folder
     * for the QRel.config file and then locating the qrels file location),
     * opens it, then iterates through every line, saving each individual
     * document judgement for each topic in a QRelList object
     *
     * @return QRelList representing the qrels file
     * @see QRelList
     */
    public static QRelList getQRelsFromFile() {
        QRelList qrelList = new QRelList();
        BufferedReader buf = null;
        try {
            // Opens the qrels.config file in the Panda/etc/ folder
            buf = FileReader.openFileReader(PANDA_ETC + fileseparator
                    + "Qrels.config");

            String qrelString;

            // get the qrels file location
            qrelString = buf.readLine();
            FileInputStream qrelsFile = new FileInputStream(qrelString);

            buf.close();

            // read in the qrels file
            DataInputStream qrelsStream = new DataInputStream(qrelsFile);
            BufferedReader qrels = new BufferedReader(new InputStreamReader(
                    qrelsStream));

            String qLine;
            StringTokenizer qToken;
            String topic;
            String docID;
            String relevance;

            // iterate through the qrels file line by line
            while ((qLine = qrels.readLine()) != null) {
                qToken = new StringTokenizer(qLine);

                // extract the meaningful information
                topic = qToken.nextToken();
                qToken.nextToken();
                docID = qToken.nextToken();
                relevance = qToken.nextToken();

                // store this qrel in the QRelList
                qrelList.addQRel(Integer.parseInt(topic), docID,
                        (relevance.equals("0")) ? false : true);
            }

            if (qrelsStream != null)
                qrelsStream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return qrelList;

    }
}
