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

    private double countErrorElem(double wx,double y){
        return Math.log(1 + Math.exp(-1*y*wx));
    }

    private double getErrorDiff(double last, double curr){
        return Math.abs(last - curr);
    }

    private Matrix W;

    @Override
    public TrainingReport train(String pathToTrainFile) {

        Pair<Matrix, Matrix> matrixes = null;
        try {
            matrixes = InputDataReader.readData(pathToTrainFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double lambda = 0;

        double errorTolerance = Math.pow(10,-5);

        Matrix X = matrixes.getKey();
        Matrix Y = matrixes.getValue();

        Matrix B = new Matrix(X.getRowDimension(), X.getRowDimension(), 0);

        W = new Matrix(X.getColumnDimension(), 1, 0.0);

        Matrix XT = X.transpose();
        Matrix I = Matrix.identity(B.getRowDimension(), B.getRowDimension()).times(1);

        double currError=Double.MIN_VALUE,lastError=Double.MIN_VALUE;

        long st = System.currentTimeMillis();
        long its = 0;
        while(true) {

            //count Beta
            double error = 0;
            for (int r = 0; r < X.getRowDimension(); r++) {
                Matrix xRow = X.getMatrix(r, r, 0, X.getColumnDimension() - 1);
                Matrix wx = xRow.times(W);
                B.set(r, r, countBeta(wx.get(0, 0), Y.get(r, 0)));

                error += countErrorElem(wx.get(0, 0), Y.get(r, 0));
            }

//            System.out.println("Error: " + error);

            if(lastError == Double.MIN_VALUE){
                lastError = error;
            }else{
                currError = error;

                if(getErrorDiff(currError,lastError) <= errorTolerance){
//                    System.out.println("BREAK");

                    TrainingReport tr = new TrainingReport();
                    tr.processingTime = System.currentTimeMillis() - st;
                    tr.iterations = its;
                    return tr;
                }

                lastError = currError;
            }

            //hesjan
            if(XT.times(X).det() < Math.pow(10,-8)){
                //osobliwy
                lambda = Math.pow(10,-5);
            }else{
                lambda = 0;
            }

            Matrix ILambda = Matrix.identity(XT.getRowDimension(), X.getColumnDimension()).times(lambda);
            Matrix BI = I.minus(B);
            Matrix H = ILambda.plus(XT.times(B).times(BI).times(X));

            //Gradient
            Matrix G = W.times(lambda).minus(XT.times(B).times(Y));

            //nowy wektor wag
            this.W = this.W.minus(H.inverse().times(G));

//            W.print(1, 6);
            its++;
        }

    }


    @Override
    public TestingReport test(String pathToTestFile) {

        try {
            TestingReport tr = new TestingReport();
            tr.setName = pathToTestFile;
            tr.binaryError = countBinaryError(this.W,pathToTestFile);
            tr.logisticError = countLogisticError();

            return tr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Matrix getW() {
        return W;
    }
}
