package pl.tstraszewski.LogisticRegression;

import Jama.Matrix;
import org.apache.commons.lang3.tuple.Pair;
import pl.tstraszewski.InputDataReader;

/**
 * Created by Tomek on 2014-12-30.
 */
public abstract class Regression {

    public double countBinaryError(Matrix counted, String dataToCompare) throws Exception {

        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(dataToCompare);
        Matrix Ytest = matrixes1.getValue();
        Matrix Xtest = matrixes1.getKey();

        Matrix Ypredicted = Xtest.times(counted);

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

    public double countLogisticError(Matrix counted, String dataToCompare) throws Exception {

        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(dataToCompare);
        Matrix Ytest = matrixes1.getValue();
        Matrix Xtest = matrixes1.getKey();

        Matrix Ypredicted = Xtest.times(counted);
        double logisticError = 0;
        for(int r=0; r<Ytest.getRowDimension(); r++ ){
            double powEl = -1 * Ytest.get(r,0) * Ypredicted.get(r,0);
            logisticError += Math.exp(powEl);
        }

        return logisticError / Ytest.getRowDimension();
    }
}
