package pl.straszewskiRosolak.LogisticRegression.Report;

/**
 * Created by Tomek on 2014-12-29.
 */
public interface Testable {

    public TrainingReport train(String pathToTrainFile);
    public TestingReport test(String pathToTestFile);
}
