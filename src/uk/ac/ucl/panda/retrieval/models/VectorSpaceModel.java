package uk.ac.ucl.panda.retrieval.models;

import java.util.HashMap;
import java.util.Vector;

/**
 * Implements the Vector Space Model. See course notes for a description of the
 * Vector Space Model
 *
 * @author Marc Sloan
 */
public class VectorSpaceModel implements Model {

    /**
     * This method should only be used in the Vector Space Model class. This
     * method will be called instead of the getScore method below
     *
     * @param query      A Vector of Strings representing the query
     * @param TermVector HashMap linking each term in the document with its term
     *                   frequency
     *                   <p/>
     *                   Example: query = [foreign, minor, germani]
     *                   TermVector = {spy=1, accept=1, servic=1, visit=2, languag=1, ...
     *                   <p/>
     *                   This method is called for every query and every document in the collection.
     */
    @Override
    public double getVSMscore(Vector<String> query,
                              HashMap<String, Integer> TermVector) {

        /* initialize the variables needed to apply VSM for query */
        Vector<Integer> query_tf = new Vector();
        Vector<Double> query_norm_weight = new Vector();

        /* initialize the variables needed to apply VSM for the document */
        Vector<Double> doc_normalized_weight = new Vector();

        double query_vector_len, doc_vector_len;
        double score = 0.0, query_sum = 0.0, doc_sum = 0.0;

        /*
        * using nnc.nnc weights -> weight for each term = term frequency in query or doc after normalizing it
        * */
        for (int i = 0; i < query.size(); i++) {
            query_tf.add(getQueryTF(query, query.get(i)));
        }

        for (String term : TermVector.keySet()) {
            doc_sum += Math.pow(TermVector.get(term), 2);
        }

        /* calculate the weight for the term based on nnc.nnc rules */
        for (Integer queryTf : query_tf) {
            query_sum += Math.pow(queryTf, 2);
        }

        query_vector_len = Math.sqrt(query_sum);
        doc_vector_len = Math.sqrt(doc_sum);

        /* normalizing the weights */
        for (int i = 0; i < query_tf.size(); i++) {
            query_norm_weight.add(query_tf.get(i) / query_vector_len);
        }

        for (String qTerm : query) {
            if (TermVector.containsKey(qTerm)) {
                doc_normalized_weight.add(TermVector.get(qTerm) / doc_vector_len);
            } else {
                doc_normalized_weight.add(0.0);
            }
        }

        /* calculate the final score between query and document under evaluation*/
        for (int i = 0; i < query_norm_weight.size(); i++) {
            score += query_norm_weight.get(i) * doc_normalized_weight.get(i);
        }

        return score;
    }

    public Integer getQueryTF(Vector<String> query, String term) {

        int occurances = 0;

        for (String queryTerm : query) {
            if (queryTerm.equals(term)) {
                occurances++;
            }
        }

        return occurances;
    }


    /**
     * The following functions are not needed for Text Retrieval assignment
     */

    public double getscore(double tf, double df, double idf, double DL,
                           double avgDL, int DocNum, double CL, int CTF, int qTF) {
        return 0.0d;
    }

    @Override
    public double defaultScore(double tf, double df, double idf, double DL,
                               double avgDL, int DocNum, double CL, int CTF, int qTF) {
        return 0.0d;
    }

    @Override
    public double defaultScore(double tf, double df, double idf, double DL,
                               double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0d;

    }

    @Override
    public double getscore(double tf, double df, double idf, double DL,
                           double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0d;

    }

    @Override
    public double getmean(double tf, double df, double idf, double DL,
                          double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {

        return 0.0d;
    }

    @Override
    public double getvar(double tf, double df, double idf, double DL,
                         double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0;
    }

    @Override
    public double defaultmean(double tf, double df, double idf, double DL,
                              double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0d;
    }

    @Override
    public double defaultvar(double tf, double df, double idf, double DL,
                             double avgDL, int DocNum, double CL, int CTF, int qTF, double a) {
        return 0.0;
    }

}
