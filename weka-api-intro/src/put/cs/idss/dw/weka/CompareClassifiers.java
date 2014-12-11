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
		// classifiers that we want to compare
		ArrayList<Classifier> classifiers = new ArrayList<Classifier>();

		//list of training sets
		String[] sets = new String[] { "badges2", "credit-a-mod", "credit-a" };

		// add classifiers to test
		List<Classifier> classifiersToTest = new ArrayList<Classifier>();
		classifiersToTest.add(new NaiveBayes());
		classifiersToTest.add(new SimpleLogistic());
		// classifiersToTest.add(new NaiveBayesClassifier());
		classifiersToTest.add(new HistogramClassifier(3));

		//for each test set
		for (String set : sets) {
			System.out.println("\n\n\n*********************\nSET: " + set
					+ "\n*********************");
			String dataset = set;
			//for each classifier
			for (Classifier classifier : classifiersToTest) {
				System.out.println("\n\n");
				System.out.println("Classifier :"
						+ classifier.getClass().getSimpleName());
				for (double d = 0.2; d <= 1.0; d += 0.05) {
					System.out.println();
					double partOfDataset = d;
											
					long seed = 1;
					
					//get data set
					BufferedReader readerTrain = new BufferedReader(
							new FileReader("data/" + dataset + "-train.arff"));
					Instances trainSetTmp = new Instances(readerTrain);
					int newTrainSetSize = (int) ((double) trainSetTmp
							.numInstances() * partOfDataset);
					trainSetTmp.randomize(new Random(seed));
					Instances trainSet = new Instances(trainSetTmp, 0,
							newTrainSetSize);
					readerTrain.close();

					BufferedReader readerTest = new BufferedReader(
							new FileReader("data/" + dataset + "-test.arff"));
					Instances testSet = new Instances(readerTest);
					testSet.randomize(new Random(seed));
					readerTest.close();

					if (trainSet.classIndex() == -1)
						trainSet.setClassIndex(trainSet.numAttributes() - 1);
					if (testSet.classIndex() == -1)
						testSet.setClassIndex(testSet.numAttributes() - 1);
					System.out.print(("" + d).replace(".", ","));

					String classifierName;
					long start;

					classifierName = classifier.getClass().getSimpleName();
					start = System.currentTimeMillis();
					classifier.buildClassifier(trainSet);
					System.out
							.print((" " + (System.currentTimeMillis() - start))
									.replace("\\.", ","));
					double correct = 0;
					start = System.currentTimeMillis();
					double sumOfPow = 0;
					for (int i = 0; i < testSet.numInstances(); i++) {

						Instance instance = testSet.instance(i);
						double truth = instance.classValue();
						double[] dist = classifier
								.distributionForInstance(instance);
						double prediction = classifier
								.classifyInstance(instance);

						// get probability
						double aa = 1 - dist[(int) truth];
						// count square error
						sumOfPow += Math.pow(aa, 2);

						// count 0/1 error
						if (truth == prediction) {
							correct++;
						}
					}
					System.out
							.print((" " + (System.currentTimeMillis() - start))
									.replace(".", ","));
					System.out.print((" " + (correct / testSet.numInstances())
							+ " " + sumOfPow).replace(".", ","));

				}

			}
		}
		// build each classifier...

		// 0/1 loss, squaredError ...

		// testing...

		// System.out.println( ... RESULTS ...
	}

}
