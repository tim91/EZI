package pl.tstraszewski;

import Jama.Matrix;
import pl.tstraszewski.LogisticRegression.LogisticRegression;

/**
 * Created by Tomek on 2014-12-17.
 */
public class MainApplication {

    public static void main(String [] args){

//        LinearRegression lr = new LinearRegression();
//        String testData = "src/main/resources/file3_test.txt";
//        String trainData = "src/main/resources/file3_train.txt";
////        lr.setTrainDataPath(trainData);
////        lr.setTestDataPath(testData);
//        try {
//            Matrix createdModel = lr.train(testData);
//            Matrix predictedTrainData = lr.test(createdModel,trainData);
//            double be = lr.countBinaryError(predictedTrainData,trainData);
//            System.out.println("Binary Error for train set: " + be);
//            Matrix predictedTestData = lr.test(createdModel, testData);
//            double binaryError = lr.countBinaryError(predictedTestData,testData);
//            System.out.println("Binary Error for test set: " + binaryError);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        LogisticRegression lr = new LogisticRegression();
        TrainingReport trainReport = lr.train("src/main/resources/file3_train.txt");
        System.out.println(trainReport.toString());
        TestingReport testReport = lr.test("src/main/resources/file3_train.txt");
        System.out.println(testReport.toString());
        TestingReport testReport1 = lr.test("src/main/resources/file3_test.txt");
        System.out.println(testReport1.toString());
    }
}
