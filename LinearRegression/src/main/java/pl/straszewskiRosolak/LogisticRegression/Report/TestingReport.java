package pl.straszewskiRosolak.LogisticRegression.Report;

/**
 * Created by Tomek on 2014-12-30.
 */
public class TestingReport {

    public double binaryError;
    public double logisticError;
    public String setName;

    @Override
    public String toString() {
        return "TestingReport{" +
                "binaryError=" + binaryError +
                ", logisticError=" + logisticError +
                ", setName='" + setName + '\'' +
                '}';
    }
}
