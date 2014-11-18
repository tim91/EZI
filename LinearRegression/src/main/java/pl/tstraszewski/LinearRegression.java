package pl.tstraszewski;

import org.apache.commons.lang3.tuple.Pair;

import Jama.Matrix;

/**
 * Created by Tomek on 2014-11-16.
 */
public class LinearRegression {




    public static void main(String [] args){

        try {
            Pair<Matrix,Matrix> matrixes = InputDataReader.readData("src/main/resources/DenBosch_train.data");

            Matrix X = matrixes.getKey();
            Matrix Y = matrixes.getValue();

            Matrix XT = X.transpose();
            Matrix H = XT.times(X);

            if(H.det() < Math.pow(10,-8)){
                //Osobliwa
            	System.out.println("Osobliwy");
                Matrix I = Matrix.identity(H.getRowDimension(),H.getRowDimension()).times(Math.pow(10,-5));
                H = H.plus(I);
            }

            Matrix G = XT.times(Y);
            Matrix W = H.inverse().times(G);

            W.print(1,6);

            //check
            Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData("src/main/resources/DenBosch_train.data");
            Matrix Xtest = matrixes1.getKey();
            Matrix Ytest = matrixes1.getValue();
            
            Matrix Ycounted = Xtest.times(W);
            
            Matrix Ydiff = Ytest.minus(Ycounted);
            
            double sum=0.0;
            for(int i=0; i < Ydiff.getRowDimension(); i++){
            	sum += Math.pow(Ydiff.get(i, 0),2);
            }
            
            sum = sum / Xtest.getRowDimension();
            
            Ydiff.print(1, 6);
            System.out.println("Result: " + Math.sqrt(sum));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
