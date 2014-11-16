package pl.tstraszewski;

import Jama.Matrix;
import javafx.util.Pair;

import java.util.Map;

/**
 * Created by Tomek on 2014-11-16.
 */
public class LinearRegression {




    public static void main(String [] args){

        try {
            Pair<Matrix,Matrix> matrixes = InputDataReader.readData("src/main/resources/test.data");

            Matrix X = matrixes.getKey();
            Matrix Y = matrixes.getValue();

            Matrix XT = X.transpose();
            Matrix H = XT.times(X);

            if(H.det() < Math.pow(10,-6)){
                //Osobliwa
                Matrix I = Matrix.identity(H.getRowDimension(),H.getRowDimension()).times(Math.pow(10,-5));
                H = H.plus(I);
            }

            Matrix G = XT.times(Y);
            Matrix W = H.inverse().times(G);

            W.print(1,6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
