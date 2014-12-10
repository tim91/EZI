package put.cs.idss.dw.weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SimpleLogistic;
import weka.core.Instance;
import weka.core.Instances;

public class CompareClassifiers {

	public CompareClassifiers() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws Exception {
		//classifiers that we want to compare
		ArrayList<Classifier> classifiers = new ArrayList<Classifier>();
		//classifiers.add(...
		
		String[] sets = new String[]{"badges2","credit-a-mod", "credit-a"};
		
		//add classifiers to test
		List<Classifier> classifiersToTest = new ArrayList<Classifier>();
		classifiersToTest.add(new NaiveBayes());
		classifiersToTest.add(new SimpleLogistic());
//		classifiersToTest.add(new NaiveBayesClassifier());
		classifiersToTest.add(new HistogramClassifier(3));
		
		for (String set : sets) {
			System.out.println("\n\n\n*********************\nSET: "+set+"\n*********************");
			String dataset = set; // badges2 / credit-a-mod / credit-a
			for (Classifier classifier : classifiersToTest) {
				System.out.println("\n\n");
				System.out.println("Classifier :" + classifier.getClass().getSimpleName());
			for(double d = 0.2; d <=1.0; d+=0.05){
				System.out.println();
				double partOfDataset = d; // part of randomized train set (0 .. 1)
				long seed = 1;
				
				BufferedReader readerTrain = new BufferedReader(new FileReader("data/"+dataset+"-train.arff"));
				Instances trainSetTmp = new Instances(readerTrain);
				int newTrainSetSize = (int)((double)trainSetTmp.numInstances() * partOfDataset);
				trainSetTmp.randomize(new Random(seed));
				Instances trainSet = new Instances(trainSetTmp, 0, newTrainSetSize);
				readerTrain.close();
				
				BufferedReader readerTest = new BufferedReader(new FileReader("data/"+dataset+"-test.arff"));
				Instances testSet = new Instances(readerTest);
				testSet.randomize(new Random(seed));
				readerTest.close();
				
				if (trainSet.classIndex() == -1) trainSet.setClassIndex(trainSet.numAttributes() - 1);
				if (testSet.classIndex() == -1) testSet.setClassIndex(testSet.numAttributes() - 1);
				System.out.print((""+d).replace(".", ","));
//				System.out.println("Data loaded and randomized:");
//				System.out.println(" - train set size: " + trainSet.numInstances());
//				System.out.println(" - test set size:  " + testSet.numInstances());
//				
				// trainingTimes...
				// testingTimes...
				String classifierName;
				long start;
//				System.out.println();
				
					classifierName = classifier.getClass().getSimpleName();
					start = System.currentTimeMillis();
					classifier.buildClassifier(trainSet);
					System.out.print((" "+(System.currentTimeMillis() - start)).replace("\\.", ","));
					double correct = 0;
					start = System.currentTimeMillis();
					double sumOfPow = 0;
					for(int i = 0; i < testSet.numInstances(); i++) {
						Instance instance = testSet.instance(i);
						double truth = instance.classValue();
						double [] dist = classifier.distributionForInstance(instance);
						double prediction = classifier.classifyInstance(instance);
						double aa = truth - dist[truth ==0.0? 1 : 0];
						sumOfPow += Math.pow(aa, 2);
						if(truth == prediction){
							correct++;
						}
					}
					System.out.print((" "+(System.currentTimeMillis() - start)).replace(".", ","));
					System.out.print((" "+(correct/testSet.numInstances()) + " " + sumOfPow).replace(".", ","));
					
				}
			
			
			
		}
		}
		// build each classifier...
		
		// 0/1 loss, squaredError ...
		
		// testing...
		
		
		
		// System.out.println( ... RESULTS ...
	}

}
