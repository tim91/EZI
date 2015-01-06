package pl.straszewskiRosolak.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.straszewskiRosolak.InputDataReader;
import pl.straszewskiRosolak.LogisticRegression.Report.Testable;
import pl.straszewskiRosolak.LogisticRegression.Report.TestingReport;
import pl.straszewskiRosolak.LogisticRegression.Report.TrainingReport;

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

        int epo = 0;
        stt = System.currentTimeMillis();
        W = new Matrix(X.getColumnDimension(), 1, 0.0);

        while(true){
            epo++;

            for(int i=0; i< X.getRowDimension(); i++){

                double error = 0;
                //obliczamy bląd logistyczny w tej iteracji
                for (int r = 0; r < X.getRowDimension(); r++) {
                    Matrix xRow = X.getMatrix(r, r, 0, X.getColumnDimension() - 1);
                    Matrix wx = xRow.times(W);
                    error += countErrorElem(wx.get(0, 0), Y.get(r, 0));
                }

                //sprawdzamy warunek stopu
                if(lastError == Double.MIN_VALUE){
                    lastError = error;
                }else{
                    currError = error;

                    if(stop(currError,lastError,errorTolerance)){
                        System.out.println("\n\n\nBREAK");
                        System.out.println(currError + " epoka: " + epo);
                        TrainingReport tr = new TrainingReport();
                        tr.iterations = i;
                        tr.processingTime = System.currentTimeMillis() - stt;
                        System.out.println("Jeszcze raz sprawdzam blad logistyczny:");
                        TestingReport stestReport = this.test("src/main/resources/file3_train.txt");
                        System.out.println(stestReport.toString());
                        System.out.println("Wagi:");
                        W.print(1,6);
                        return tr;
                    }

                    lastError = currError;
                }

                //wyznaczamy nową wartość kroku
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
            }
            System.out.println(currError + " epoka: " + epo);

            //blad na zwbiorze treningowym po epoce
            TestingReport stestReport = this.test("src/main/resources/file3_train.txt");
            System.out.println(stestReport.toString());
        }

    }

}
