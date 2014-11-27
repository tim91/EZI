package put.cs.idss.dw.weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import weka.classifiers.bayes.NaiveBayesSimple;
import weka.core.Instance;
import weka.core.Instances;

public class Test {

	private static class PatternScore {
		public int truth = 0;
		public int teachersPrediction = 0;
		public double zeroProb = 0.0;
		public double oneProb = 0.0;
	}
	
	private static ArrayList<PatternScore> patternScore = null;
	
	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	public static double roundDouble(double x, int n) {
		String s = "#.";
		for (int i = 0; i < n; i++) {
			s += "#";
		}
		DecimalFormat twoDForm = new DecimalFormat(s);
		return Double.parseDouble(twoDForm.format(x));
	}
	
	private static void loadPattern(String fileName) {
		patternScore = new ArrayList<Test.PatternScore>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					String[] lineArray = line.split("\t");
					PatternScore ps = new PatternScore();
					ps.truth = Integer.parseInt(lineArray[0]);
					ps.teachersPrediction = Integer.parseInt(lineArray[1]);
					ps.zeroProb = Double.parseDouble(lineArray[2]);
					ps.oneProb = Double.parseDouble(lineArray[3]);
					patternScore.add(ps);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Pattern results loaded.");
	}

	public static void main(String[] args) throws Exception {
		Locale.setDefault(Locale.ENGLISH);
		
		String dataSetName = "data/spambase";
		
		BufferedReader readerTrain = new BufferedReader(new FileReader(dataSetName + "-train.arff"));
		Instances trainSet = new Instances(readerTrain);
		readerTrain.close();
		//trainSet.randomize(new Random(1));
		
		BufferedReader readerTest = new BufferedReader(new FileReader(dataSetName + "-test.arff"));
		Instances testSet = new Instances(readerTest);
		readerTest.close();
		//trainSet.randomize(new Random(1));
		
		if (trainSet.classIndex() == -1) trainSet.setClassIndex(trainSet.numAttributes() - 1);
		if (testSet.classIndex() == -1) testSet.setClassIndex(testSet.numAttributes() - 1);
		
		System.out.println("Data loaded.");
		
		loadPattern("data/pattern.csv");
		
		NaiveBayesClassifier naiveBayes = new NaiveBayesClassifier();
		naiveBayes.buildClassifier(trainSet);
		System.out.println("Classifier has been learned.");
		
		double sum = 0.0;
		double teachersAcc = 0.0;
		double myAcc = 0.0;
		
		for(int i = 0; i < testSet.numInstances(); i++) {
			Instance instance = testSet.instance(i);
			int truth = (int) instance.classValue();
			
			//teachers result
			PatternScore ps = patternScore.get(i);
			int teachersPrediction = ps.teachersPrediction;
			
			//my result
			double[] myDistribution = naiveBayes.distributionForInstance(instance);
			myDistribution[0] = roundDouble(myDistribution[0], 4);
			myDistribution[1] = roundDouble(myDistribution[1], 4);
			int myPrediction = (int)naiveBayes.classifyInstance(instance);
			
			System.out.println("\nResult for test instance " + i + " :");
			System.out.println(" - truth:                  " + truth);
			System.out.println(" - teacher's prediction:   " + ps.teachersPrediction);
			System.out.println(" - my prediction:          " + myPrediction);
			System.out.println(" - teacher's distribution: [ " + ps.zeroProb + " , " + ps.oneProb + " ]");
			System.out.println(" - my distribution:        [ " + myDistribution[0] + " , " + myDistribution[1] + " ]");
			if(ps.teachersPrediction != myPrediction || myDistribution[0] != ps.zeroProb) {
				System.out.println(" ! SOMETHING IS WRONG :(");
			}
			
			sum++;
			if(teachersPrediction == truth) teachersAcc++;
			if(myPrediction == truth) myAcc++;
		}
		
		System.out.println("\nTeacher's accuracy: " + roundDouble((teachersAcc / sum),4));
		System.out.println("My accuracy:        " + roundDouble((myAcc / sum),4));
		if(teachersAcc != myAcc) {
			System.out.println("SOMETHING IS WRONG :(");
		} else {
			System.out.println("Your algorithm works correctly :)");
		}
	}

}
