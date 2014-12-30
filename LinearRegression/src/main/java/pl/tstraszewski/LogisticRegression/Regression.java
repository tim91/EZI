package pl.tstraszewski.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.tstraszewski.InputDataReader;
import pl.tstraszewski.Testable;
import pl.tstraszewski.TestingReport;

/**
 * Created by Tomek on 2014-12-30.
 */
public abstract class Regression implements Testable {

    protected double currError=Double.MIN_VALUE,lastError=Double.MIN_VALUE;
    protected Matrix W;

    protected double errorTolerance = Math.pow(10,-9);

    public double countBinaryError(Matrix W, String dataToCompare) throws Exception {

        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(dataToCompare);
        Matrix Ytest = matrixes1.getValue();
        Matrix Xtest = matrixes1.getKey();

        Matrix Ypredicted = Xtest.times(W);

        double binaryError = 0.0;
        for(int i=0; i < Ytest.getRowDimension(); i++){
            double truth = Ytest.get(i,0);
            double predicted = Ypredicted.get(i,0);
            boolean theSameSig = (truth * predicted) >= 0;
            if(theSameSig == false){
                //bad classification
                binaryError++;
            }
        }
        return binaryError / Ytest.getRowDimension();
    }

    public double countLogisticError(Matrix W, String dataToCompare) throws Exception {

        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(dataToCompare);
        Matrix Ytest = matrixes1.getValue();
        Matrix Xtest = matrixes1.getKey();

        Matrix Ypredicted = Xtest.times(W);
        double logisticError = 0;
        for(int r=0; r<Ytest.getRowDimension(); r++ ){
            double powEl = -1 * Ytest.get(r,0) * Ypredicted.get(r,0);
            logisticError += Math.log(1+Math.exp(powEl));
        }

        return logisticError / Ytest.getRowDimension();
    }

    public boolean stop(double last, double curr, double eps){
        return Math.abs(last - curr) <= eps;
    }

    public TestingReport test(String pathToTestFile) {

        try {
            TestingReport tr = new TestingReport();
            tr.setName = pathToTestFile;
            tr.binaryError = countBinaryError(this.W,pathToTestFile);
            tr.logisticError = countLogisticError(this.W, pathToTestFile);

            return tr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public double countErrorElem(double wx,double y){
        return Math.log(1 + Math.exp(-1*y*wx));
    }

    public Matrix getW() {
        return W;
    }
}
