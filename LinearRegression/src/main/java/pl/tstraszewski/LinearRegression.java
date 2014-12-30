package pl.tstraszewski;

import Jama.Matrix;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by Tomek on 2014-11-16.
 */
//@Data
public class LinearRegression {

    private String testDataPath;
    private String trainDataPath;

    public void runLinearRegresion(){

    }

    public Matrix train(String trainData) throws Exception {

        Pair<Matrix,Matrix> matrixes = InputDataReader.readData(trainData);

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

//        W.print(1,6);
        return W;
    }

    public Matrix test(Matrix W, String testData) throws Exception {
                        //check
        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(testData);
        Matrix Xtest = matrixes1.getKey();
        Matrix Ycounted = Xtest.times(W);
        return Ycounted;
     }

    public double countBinaryError(Matrix data, String dataToCompare) throws Exception {
        Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData(dataToCompare);
        Matrix Ytest = matrixes1.getValue();

        double binaryError = 0.0;
        for(int i=0; i < Ytest.getRowDimension(); i++){
            double truth = Ytest.get(i,0);
            double predicted = data.get(i,0);
            boolean theSameSig = (truth * predicted) >= 0;
            if(theSameSig == false){
                //bad classification
                binaryError++;
            }
        }
        return binaryError / Ytest.getRowDimension();
    }

//    public static void main(String [] args){
//
//        try {
//
//        	for(int lambda = -3; lambda<=25; lambda++){
//        		Pair<Matrix,Matrix> matrixes = InputDataReader.readData("src/main/resources/DenBosch_train.data");
//
//                Matrix X = matrixes.getKey();
//                Matrix Y = matrixes.getValue();
//
//                Matrix XT = X.transpose();
//                Matrix H = XT.times(X);
//
////                if(H.det() < Math.pow(10,-8)){
//                    //Osobliwa
////                	System.out.println("Osobliwy");
//                    Matrix I = Matrix.identity(H.getRowDimension(),H.getRowDimension()).times(Math.pow(2, lambda));
//                    H = H.plus(I);
////                }
//
//                Matrix G = XT.times(Y);
//                Matrix W = H.inverse().times(G);
//
////                W.print(1,6);
//
//                //check
//                Pair<Matrix,Matrix> matrixes1 = InputDataReader.readData("src/main/resources/DenBosch_test.data");
//                Matrix Xtest = matrixes1.getKey();
//                Matrix Ytest = matrixes1.getValue();
//
//                Matrix Ycounted = Xtest.times(W);
//
//                Matrix Ydiff = Ytest.minus(Ycounted);
//
//                double sum=0.0;
//                for(int i=0; i < Ydiff.getRowDimension(); i++){
//                	sum += Math.pow(Ydiff.get(i, 0),2);
//                }
//
//                sum = sum / Xtest.getRowDimension();
//
////                Ydiff.print(1, 6);
//                System.out.println("Test Result: " + Math.sqrt(sum) + " lambda: " + Math.pow(2, lambda));
//
//                matrixes1 = InputDataReader.readData("src/main/resources/DenBosch_train.data");
//                Xtest = matrixes1.getKey();
//                Ytest = matrixes1.getValue();
//
//                Ycounted = Xtest.times(W);
//
//                Ydiff = Ytest.minus(Ycounted);
//
//                sum=0.0;
//                for(int i=0; i < Ydiff.getRowDimension(); i++){
//                	sum += Math.pow(Ydiff.get(i, 0),2);
//                }
//
//                sum = sum / Xtest.getRowDimension();
//
////                Ydiff.print(1, 6);
//                System.out.println("Train Result: " + Math.sqrt(sum) + " lambda: " + Math.pow(2, lambda));
//        	}
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void setTrainDataPath(String trainDataPath) {
        this.trainDataPath = trainDataPath;
    }

    public void setTestDataPath(String testDataPath) {
        this.testDataPath = testDataPath;
    }
}
