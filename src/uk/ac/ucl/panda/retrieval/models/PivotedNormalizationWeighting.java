package uk.ac.ucl.panda.retrieval.models;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by ralucamelon on 19/03/2015.
 */
public class PivotedNormalizationWeighting implements Model{


    /* Pivoted Normalization Weighting parameter
    *  s is a constant ; usually set to 0.20  */
    private float s = 0.1;


    /**
     *  Calculated the score between a query term and a document under evaluation
     *  It calculates the score based on the Pivoted Normalization Weighting method/formula
     *
     *
     *  @param tf - Term Frequency, number of times term appears in the document
     *  @param df - Document Frequency, number of documents the term appears in
     *  @param idf - Inverse Document Frequency
     *  @param DL - number of terms in document
     *  @param avgDL - average number of terms in all documents
     *  @param DocNum - number of documents in the collection
     *  @param CL - Collection Length, number of terms in document collection
     *  @param CTF - Collection Term Frequency, number of times the term appears in the collection
     *  @param qTF - Query Term Frequency, number of times the term appears in the query
     *
     *  @return score the score for the term
     */

    public double getscore(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF) {

        double score;
        score = (( 1 + Math.log(1.0 + Math.log(tf))) / (1.0 - s + s * (DL/avgDL))) * qTF * Math.log((DocNum + 1.0)/df);

        return score;
    }


    /**
     *  The following functions are not needed for Text Retrieval assignment
     */

    @Override
    public double getVSMscore(Vector<String> query, HashMap<String, Integer> TermVector) {
        return 0;
    }

    @Override
    public double defaultScore(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF) {
        return 0.0d;
    }

    public double defaultScore(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        s = (float)a;
        return defaultScore(tf,df, idf, DL, avgDL, DocNum, CL, CTF, qTF);
    }

    public double getscore(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        s = (float) a;
        return getscore(tf, df, idf, DL, avgDL, DocNum, CL, CTF, qTF);
    }

    @Override
    public
    double getmean(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        double score;
        score = (( 1 + Math.log(1.0 + Math.log(tf))) / ( 1.0 - s + s * (DL/avgDL))) * qTF * Math.log((DocNum + 1.0)/df);
        return score;
    }

    @Override
    public
    double getvar(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0;
    }

    @Override
    public
    double defaultmean(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        s = (float) a;
        return defaultScore(tf,df, idf, DL, avgDL, DocNum, CL, CTF, qTF);
    }

    @Override
    public
    double defaultvar(double tf, double df, double idf, double DL, double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0;
    }
}
