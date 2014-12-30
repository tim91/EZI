package pl.tstraszewski.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.tstraszewski.InputDataReader;
import pl.tstraszewski.Testable;
import pl.tstraszewski.TestingReport;
import pl.tstraszewski.TrainingReport;

import java.util.Random;

/**
 * Created by Tomek on 2014-12-30.
 */
public class StochasticLogisticRegression extends Regression implements Testable {

    @Override
    public TrainingReport train(String pathToTrainFile) {

        Pair<Matrix, Matrix> matrixes = null;
        long stt = System.currentTimeMillis();
        try {
            matrixes = InputDataReader.readData(pathToTrainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("WCZYTANIE DANYCH: " + (System.currentTimeMillis() - stt) + "ms" );

        Matrix X = matrixes.getKey();
        Matrix Y = matrixes.getValue();

        double c = 1;
        int epo = 0;
        stt = System.currentTimeMillis();
        W = new Matrix(X.getColumnDimension(), 1, 0.0);
        Random rand = new Random();
        while(true){
            epo++;

            for(int i=0; i< X.getRowDimension(); i++){
                double error = 0;
                for (int r = 0; r < X.getRowDimension(); r++) {
                    Matrix xRow = X.getMatrix(r, r, 0, X.getColumnDimension() - 1);
                    Matrix wx = xRow.times(W);
                    error += countErrorElem(wx.get(0, 0), Y.get(r, 0));
                }

//                System.out.println("Error: " + error);

                if(lastError == Double.MIN_VALUE){
                    lastError = error;
                }else{
                    currError = error;

                    if(stop(currError,lastError,errorTolerance)){
//                    System.out.println("BREAK");
                        System.out.println(currError + " epoka: " + epo);
                        TrainingReport tr = new TrainingReport();
                        tr.iterations = i;
                        tr.processingTime = System.currentTimeMillis() - stt;
                        return tr;
                    }

                    lastError = currError;
                }

                /**
                 * Tutaj trzeba dobierc alfe
                 */
                double alpha = 2
                        / Math.sqrt(X.getRowDimension() - i);
                //gradient
                Matrix xi = X.getMatrix(i,i,0,X.getColumnDimension()-1);
                Matrix G = xi.times(W);
                double powVal = G.get(0,0);
                double yi = Y.get(i,0);
                powVal *= yi;
                double gv = alpha* (yi/ (1 + Math.exp(powVal)));

                xi = xi.times(gv);

                W = W.plus(xi.transpose());
//                System.out.println(i);
            }
            System.out.println(currError + " epoka: " + epo);
        }

    }

}
