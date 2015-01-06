package pl.straszewskiRosolak.LogisticRegression.Report;

/**
 * Created by Tomek on 2014-12-30.
 */
public class TrainingReport {

    public double iterations;
    public double processingTime;

    @Override
    public String toString() {
        return "TrainingReport{" +
                "iterations=" + iterations +
                ", processingTimeMs=" + processingTime +
                ", processingTimeS=" + processingTime/1000 +
                '}';
    }
}
