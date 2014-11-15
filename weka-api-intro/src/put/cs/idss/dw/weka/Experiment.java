package put.cs.idss.dw.weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

public class Experiment {

	public Experiment() {
		// TODO Auto-generated constructor stub
	}

	public static void runExperiment(Classifier classifier, String datasetPath,
			long seed, double split) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(datasetPath));
		Instances data = new Instances(reader);
		reader.close();
		
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		
		data.randomize(new Random(seed));
		
		int trainSetSize = (int)(data.numInstances() * split);
		int testSetSize = data.numInstances() - trainSetSize;
		Instances trainSet = new Instances(data,0,trainSetSize);
		Instances testSet = new Instances(data,trainSetSize,testSetSize);
		
		classifier.buildClassifier(trainSet);
		
		int theSame = 0;
		int different = 0;
		
		for(int i = 0; i < testSet.numInstances(); i++) {
			Instance instance = testSet.instance(i);
			double prediction = classifier.classifyInstance(instance);
			double truth = instance.classValue();
			
			if(prediction == truth)
				theSame ++;
			else
				different++;
			
		}
		
		System.out.println("The same: " + theSame + " different: " + different + " percentage: " + ((float)theSame/(float)testSet.numInstances()) + "% elements: " + testSet.numInstances());
	}

	public static void main(String[] args) {
		
		Experiment e = new Experiment();
		HistogramClassifier hc = new HistogramClassifier(3);
		try {
			e.runExperiment(hc, "iris.arff", 123456789, 0.5);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
