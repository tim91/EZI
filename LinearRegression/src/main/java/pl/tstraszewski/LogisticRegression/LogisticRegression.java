package pl.tstraszewski.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.tstraszewski.InputDataReader;
import pl.tstraszewski.Testable;
import pl.tstraszewski.TestingReport;
import pl.tstraszewski.TrainingReport;

/**
 * Created by Tomek on 2014-12-29.
 */
public class LogisticRegression extends Regression implements Testable {


    private double countBeta(double wx, double y) {

        double m = (Math.exp(y * wx) + 1);
        return 1 / m;
    }






    @Override
    public TrainingReport train(String pathToTrainFile) {

        System.out.println("start");
        long stt = System.currentTimeMillis();
        Pair<Matrix, Matrix> matrixes = null;
        try {
            matrixes = InputDataReader.readData(pathToTrainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("WCZYTANIE DANYCH: " + (System.currentTimeMillis() - stt) + "ms" );
        stt = System.currentTimeMillis();
        double lambda = 0;



        Matrix X = matrixes.getKey();
        Matrix Y = matrixes.getValue();

        Matrix B = new Matrix(X.getRowDimension(), X.getRowDimension(), 0);

        W = new Matrix(X.getColumnDimension(), 1, 0.0);

        long st = System.currentTimeMillis();
        Matrix XT = X.transpose();
//        System.out.println("TRANSPOSE: " + (System.currentTimeMillis() - stt) + "ms" );
//        stt = System.currentTimeMillis();
        Matrix I = Matrix.identity(B.getRowDimension(), B.getRowDimension()).times(1);
//        System.out.println("IDENTITY: " + (System.currentTimeMillis() - stt) + "ms" );
//        stt = System.currentTimeMillis();



        long its = 0;
        Matrix xRow;
        Matrix wx;

        double det = XT.times(X).det();
        if(det < Math.pow(10,-8)){
            //osobliwy
            lambda = Math.pow(10,-5);
        }else{
            lambda = 0;
        }
//        System.out.println("wyznacznik: " + (Syu mnie dane
        while(true) {


            //count Beta
            double error = 0;
            for (int r = 0; r < X.getRowDimension(); r++) {
                xRow = X.getMatrix(r, r, 0, X.getColumnDimension() - 1);
                wx = xRow.times(W);
                B.set(r, r, countBeta(wx.get(0, 0), Y.get(r, 0)));

                error += countErrorElem(wx.get(0, 0), Y.get(r, 0));
            }
//            System.out.println("macierz beta: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            System.out.println("Error: " + error);

            if(lastError == Double.MIN_VALUE){
                lastError = error;
            }else{
                currError = error;

                if(stop(currError,lastError,errorTolerance)){
//                    System.out.println("BREAK");

                    TrainingReport tr = new TrainingReport();
                    tr.processingTime = System.currentTimeMillis() - st;
                    tr.iterations = its;

                    return tr;
                }

                lastError = currError;
            }

            //hesjan
            Matrix ILambda = Matrix.identity(XT.getRowDimension(), X.getColumnDimension()).times(lambda);
//            System.out.println("I lambda: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            Matrix BI = I.minus(B);
//            System.out.println("BI: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();

            Matrix temp = multiplyByDiagonalMatrix(XT,B);
//            System.out.println("H1: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            temp = multiplyByDiagonalMatrix(temp,(BI));
//            System.out.println("H2: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            temp = temp.times(X);
//            System.out.println("H3: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            Matrix H = ILambda.plus(temp);
//            System.out.println("H4: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();

            //Gradient
            Matrix Gtemp = multiplyByDiagonalMatrix(XT,B);
//            System.out.println("G1: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            Matrix G = W.times(lambda).minus(Gtemp.times(Y));
//            System.out.println("G2: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
            //nowy wektor wag
            this.W = this.W.minus(H.inverse().times(G));
//            System.out.println("W: " + (System.currentTimeMillis() - stt) + "ms" );
//            stt = System.currentTimeMillis();
//            W.print(1, 6);
            its++;
            System.out.println("Iteracja: " + its);
        }

    }

    public Matrix multiplyByDiagonalMatrix(Matrix A, Matrix diagonal){
        Matrix temp = new Matrix(A.getRowDimension(),diagonal.getColumnDimension());
        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = i; j < diagonal.getColumnDimension(); j++) {
                for (int k = j; k < A.getColumnDimension(); k++) {
                    temp.set(i,j, A.get(i,k) * diagonal.get(k,j));
                    break;
                }
//                    break;
            }
        }
        return temp;
    }


}
