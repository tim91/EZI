package pl.straszewskiRosolak.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.straszewskiRosolak.InputDataReader;
import pl.straszewskiRosolak.LogisticRegression.Report.Testable;
import pl.straszewskiRosolak.LogisticRegression.Report.TrainingReport;

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

        Pair<Matrix, Matrix> matrixes = null;
        try {
            matrixes = InputDataReader.readData(pathToTrainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }



        Matrix X = matrixes.getKey();
        Matrix Y = matrixes.getValue();

        Matrix B = new Matrix(X.getRowDimension(), X.getRowDimension(), 0);

        W = new Matrix(X.getColumnDimension(), 1, 0.0);

        //time start
        long st = System.currentTimeMillis();

        Matrix XT = X.transpose();
        Matrix I = Matrix.identity(B.getRowDimension(), B.getRowDimension()).times(1);

        long its = 0;
        Matrix xRow;
        Matrix wx;

        double lambda = 0;
        //liczymy wyznacznkik
        double det = XT.times(X).det();

        if(det < Math.pow(10,-8)){
            //osobliwy
            lambda = Math.pow(10,-5);
        }

        while(true) {

            //obliczamy współcznynnik beta
            double error = 0;
            for (int r = 0; r < X.getRowDimension(); r++) {
                xRow = X.getMatrix(r, r, 0, X.getColumnDimension() - 1);
                wx = xRow.times(W);
                B.set(r, r, countBeta(wx.get(0, 0), Y.get(r, 0)));

                error += countErrorElem(wx.get(0, 0), Y.get(r, 0));
            }
            System.out.println("Error: " + error);

            //sprawdzamy warunek stopu
            if(lastError == Double.MIN_VALUE){
                lastError = error;
            }else{
                currError = error;

                if(stop(currError,lastError,errorTolerance)){
                    System.out.println("KONIEC");

                    TrainingReport tr = new TrainingReport();
                    tr.processingTime = System.currentTimeMillis() - st;
                    tr.iterations = its;

                    System.out.println("Wektor wag: ");
                    W.print(1, 6);
                    return tr;
                }

                lastError = currError;
            }

            //hesjan
            Matrix ILambda = Matrix.identity(XT.getRowDimension(), X.getColumnDimension()).times(lambda);
            Matrix BI = I.minus(B);
            Matrix temp = multiplyByDiagonalMatrix(XT,B);
            temp = multiplyByDiagonalMatrix(temp,(BI));
            temp = temp.times(X);
            Matrix H = ILambda.plus(temp);

            //Gradient
            Matrix Gtemp = multiplyByDiagonalMatrix(XT,B);
            Matrix G = W.times(lambda).minus(Gtemp.times(Y));

            //nowy wektor wag
            this.W = this.W.minus(H.inverse().times(G));
            its++;
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
            }
        }
        return temp;
    }


}
