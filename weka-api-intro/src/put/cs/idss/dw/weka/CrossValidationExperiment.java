package put.cs.idss.dw.weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.attributeSelection.LinearForwardSelection;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class CrossValidationExperiment {
	
	public static void runExperiment(Classifier classifier, String trainSetPath,
			String testSetPath, long seed) throws Exception {
		BufferedReader readerTrain = new BufferedReader(new FileReader(trainSetPath));
		Instances trainSet = new Instances(readerTrain);
		trainSet.randomize(new Random(seed));
		readerTrain.close();
		
		BufferedReader readerTest = new BufferedReader(new FileReader(testSetPath));
		Instances testSet = new Instances(readerTest);
		testSet.randomize(new Random(seed));
		readerTest.close();
		
		if (trainSet.classIndex() == -1) trainSet.setClassIndex(trainSet.numAttributes() - 1);
		if (testSet.classIndex() == -1) testSet.setClassIndex(testSet.numAttributes() - 1);
		
		System.out.println("Data loaded.");
		
		classifier.buildClassifier(trainSet);
		System.out.println("Classifier has been learned.");
		
		System.out.println("Test evaluation...");
		
		double loss01 = 0;
		double squaredError = 0;
		for(int i = 0; i < testSet.numInstances(); i++) {
			Instance instance = testSet.instance(i);
			
			int truth = (int) instance.classValue();
			double[] distribution = classifier.distributionForInstance(instance);
			int prediction = distribution[1] >= distribution[0] ? 1 : 0;

			loss01 += truth == prediction ? 0 : 1;
			squaredError += Math.pow(1.0 - distribution[truth], 2);
		}
		
		loss01 /= (double)testSet.numInstances();
		squaredError /= (double)testSet.numInstances();
		
		System.out.println("Train/Test evaluation for " + classifier.getClass().getSimpleName());
		System.out.println(" - 0/1 loss:           " + loss01);
		System.out.println(" - squared-error loss: " + squaredError);
		System.out.println("--------------------------------------");
	}

	public static void runCVExperiment(Classifier classifier, String dataSetPath,
			int folds, double partOfDataSet, long seed) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(dataSetPath));
		Instances dataSetTmp = new Instances(reader);
		dataSetTmp.randomize(new Random(seed));
		Instances dataSet = new Instances(dataSetTmp, 0, (int)((double)dataSetTmp.numInstances() * partOfDataSet));
		reader.close();
		
		if (dataSet.classIndex() == -1) dataSet.setClassIndex(dataSet.numAttributes() - 1);
		
		System.out.println("Data loaded. Data set size: " + dataSet.numInstances());
	
		double loss01 = 0;
		double squaredError = 0;
		for (int n = 0; n < folds; n++) {
			System.out.println("Cross-Validation: fold " + (n+1) + "/" + folds);
			
			Instances train = dataSet.trainCV(folds, n);
			Instances test = dataSet.testCV(folds, n);
			
			Classifier classifierCopy = Classifier.makeCopy(classifier);
			classifierCopy.buildClassifier(train);
			
			double _loss01 = 0;
			double _squaredError = 0;
			for(int i = 0; i < test.numInstances(); i++) {
				Instance instance = test.instance(i);
				
				int truth = (int) instance.classValue();
				double[] distribution = classifierCopy.distributionForInstance(instance);
				int prediction = distribution[1] >= distribution[0] ? 1 : 0;

				_loss01 += truth == prediction ? 0 : 1;
				_squaredError += Math.pow(1.0 - distribution[truth], 2);
			}
			
			_loss01 /= (double)test.numInstances();
			_squaredError /= (double)test.numInstances();
			
			loss01 += _loss01;
			squaredError += _squaredError;
		}
		
		loss01 /= (double)folds;
		squaredError /= (double)folds;
		
		System.out.println("CV evaluation for " + classifier.getClass().getSimpleName());
		System.out.println(" - 0/1 loss:           " + loss01);
		System.out.println(" - squared-error loss: " + squaredError);
		System.out.println("--------------------------------------");
	}
	
	public static void compareCrossValidationScenarios(Classifier classifier, Instances dataSet,
			int folds, int numTopAttributes, long seed) throws Exception {
		
		if (dataSet.classIndex() == -1) dataSet.setClassIndex(dataSet.numAttributes() - 1);
		
		Instances dataSet2 = new Instances(dataSet);
		dataSet2.randomize(new Random(1));
		
		ClassifierSubsetEval cse = new  ClassifierSubsetEval(); 
		cse.setClassifier(Classifier.makeCopy(classifier));
		cse.setUseTraining(true);
		
		WrapperSubsetEval wse = new WrapperSubsetEval();
		wse.setClassifier(Classifier.makeCopy(classifier));
		wse.setFolds(folds);
		
		LinearForwardSelection lfs = new LinearForwardSelection();
		lfs.setNumUsedAttributes(numTopAttributes);
		
		AttributeSelection as = new AttributeSelection();
		as.setInputFormat(dataSet);
		as.setEvaluator(cse);
		as.setSearch(lfs);
		
		Instances filteredInstances = Filter.useFilter(dataSet, as);
		
		//Scenario 1
		weka.classifiers.Evaluation eval = new Evaluation(filteredInstances);
		String[] options = {};
		eval.crossValidateModel(Classifier.makeCopy(classifier), filteredInstances, folds, new Random(seed), options);
		System.out.println("Scenario 1:\n"+eval.toSummaryString()+"\n--------------------\n");
		System.out.println(eval.toMatrixString());
		
		//----------
		
		AttributeSelectedClassifier asc = new AttributeSelectedClassifier();
		WrapperSubsetEval wse2 = new WrapperSubsetEval();
		wse2.setClassifier(Classifier.makeCopy(classifier));
		
		LinearForwardSelection lfs2 = new LinearForwardSelection();
		lfs2.setNumUsedAttributes(numTopAttributes);
		
		asc.setSearch(lfs2);
		asc.setEvaluator(cse);
		asc.setClassifier(Classifier.makeCopy(classifier));
		
		//Scenario 2
		weka.classifiers.Evaluation eval2 = new Evaluation(dataSet2);
		String[] options2 = {};
		eval2.crossValidateModel(Classifier.makeCopy(asc), dataSet2, folds, new Random(seed), options);
		System.out.println("Scenario 2:\n"+eval2.toSummaryString());
		System.out.println(eval2.toMatrixString());
	}

	static public Instances generateRandomBinaryModel(String name, int numInstances, int numAttributes, int seed) {
		
		Random random = new Random(seed);
		
		FastVector attributes = new FastVector();
		for(int i = 0; i < numAttributes - 1; i++) {
			Attribute attr = new Attribute("feature_"+(i+1));
			attributes.addElement(attr);
		}
		
		FastVector classValues = new FastVector();
		classValues.addElement("0");
		classValues.addElement("1");
		Attribute label = new Attribute("class", classValues);
		attributes.addElement(label);
		
		Instances instances = new Instances(name, attributes, 0);
		
		double[][] dataset = new double[numInstances][];
		
		for(int i = 0; i < numInstances; i++) {
			double[] x = new double[numAttributes - 1];
			for(int j = 0; j < numAttributes - 1; j++) {
				x[j] = random.nextDouble();
			}
			dataset[i] = x;
		}
		
		
		for(int i = 0; i < numInstances; i++) {
			Instance inst = new Instance(numAttributes);
			inst.setDataset(instances);
			
			double[] x = dataset[i];
			
			for(int j = 0; j < numAttributes - 1; j++) {
				inst.setValue(j, x[j]);
			}
			
			String classValue = "0";
			
			if(random.nextDouble() > 0.5)
				classValue = "1";
								
			inst.setValue(numAttributes - 1, classValue);
			instances.add(inst);
		}
		
		return instances;
	}

	
	public static void main(String[] args) throws Exception {
		
		Logistic classifier = new Logistic();
		classifier.setRidge(0.00001);
		
		Instances dataSet = generateRandomBinaryModel("dataset", 200, 100, 1);
		
		compareCrossValidationScenarios(classifier, dataSet, 10, 2, 1);
	}

}
