package pl.tstraszewski;

import Jama.Matrix;
import pl.tstraszewski.LogisticRegression.LogisticRegression;
import pl.tstraszewski.LogisticRegression.StochasticLogisticRegression;

/**
 * Created by Tomek on 2014-12-17.
 */
public class MainApplication {

    public static void main(String [] args){

//        LogisticRegression lr = new LogisticRegression();
//        TrainingReport trainReport = lr.train("src/main/resources/file3_train.txt");
//        System.out.println(trainReport.toString());
//        TestingReport testReport = lr.test("src/main/resources/file3_train.txt");
//        System.out.println(testReport.toString());
//        TestingReport testReport1 = lr.test("src/main/resources/file3_test.txt");
//        System.out.println(testReport1.toString());
//        lr.getW().print(1,6);

        StochasticLogisticRegression slr = new StochasticLogisticRegression();
        TrainingReport strainReport = slr.train("src/main/resources/file3_train.txt");
        System.out.println(strainReport.toString());
        TestingReport stestReport = slr.test("src/main/resources/file3_train.txt");
        System.out.println(stestReport.toString());
        TestingReport stestReport1 = slr.test("src/main/resources/file3_test.txt");
        System.out.println(stestReport1.toString());
//        slr.getW().print(1,6);
    }
}
