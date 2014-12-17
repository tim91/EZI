package pl.tstraszewski;

import Jama.Matrix;

/**
 * Created by Tomek on 2014-12-17.
 */
public class MainApplication {

    public static void main(String [] args){

        LinearRegression lr = new LinearRegression();
        lr.setTrainDataPath("src/main/resources/DenBosch_train.data");
        lr.setTestDataPath("src/main/resources/DenBosch_test.data");
        try {
            Matrix trainedClassifier = lr.train();
            lr.test(trainedClassifier);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
