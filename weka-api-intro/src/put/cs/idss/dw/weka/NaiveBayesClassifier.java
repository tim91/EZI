package put.cs.idss.dw.weka;

import java.util.HashMap;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class NaiveBayesClassifier extends Classifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7550409893545527343L;

	/** number of classes */
	protected int numClasses;

	/** counts, means, standard deviations, priors..... */
	protected double[][] counts;
	protected double classNum[];
	protected double numOfValues[][];

	protected int learnSetSize = 0;
	
	protected Map<CountHelper, Double> attrValue = new HashMap<CountHelper, Double>();
	
	public NaiveBayesClassifier() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {

		numClasses = data.numClasses();
		learnSetSize = data.numInstances();
		classNum = new double[numClasses];
		numOfValues = new double[data.numAttributes()][];

		// remove instances with missing class
		data.deleteWithMissingClass();

		for (int i = 0; i < data.numInstances(); i++) {
			Instance instance = data.instance(i);
			int classValue = (int) instance.classValue();
			classNum[classValue]++;
			for (int j = 0; j < data.numAttributes() - 1; j++) {
				//polic
				if (data.attribute(j).isNominal()) {

					CountHelper ch = new CountHelper();
					ch.attributeIdx = j;
					ch.classValue = instance.classValue();
					ch.value = instance.value(j);
					//liczność wystąpienia pary (atrybut, klasa)
					attrValue.put(ch,
							attrValue.get(ch) == null ? 1
									: attrValue.get(ch) + 1);

				} else {
					// ...
				}
			}
			// ...
		}

		//obliczamy prawdopodobienstwo wystąpienia konkretnej pary (atrybut klasa)
		for (CountHelper c : attrValue.keySet()) {
				attrValue.put(c, (attrValue.get(c)/classNum[(int)c.classValue]));
			
		}
		
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] distribution = new double[numClasses];

		
		for(int i=0; i<numClasses; i++){
			//przenażamy obliczone prawdopodobieństwo przez prawdopodobieństwo wystąpienia konkretnej klasy
			distribution[i] = calculateProbability(instance,i) * classNum[i]/learnSetSize;
		}
		return distribution;
	}

	private double calculateProbability(Instance instance, double classValue){
		
		double p = 1;
		//obliczamy iloczyń prawdopodobieństw dla konkretnych atrybutów
		for (int j = 0; j < instance.numAttributes() - 1; j++) {
			if (instance.attribute(j).isNominal()) {

				CountHelper ch = new CountHelper();
				ch.attributeIdx = j;
				ch.classValue = classValue;
				ch.value = instance.value(j);
				
				p*= attrValue.get(ch);

			} else {
				// ...
			}
		}
		return p;
	}
	
	@Override
	public double classifyInstance(Instance instance) throws Exception {
		double classValue = 0.0;
		double max = Double.MIN_VALUE;
		double[] dist = distributionForInstance(instance);

		for (int i = 0; i < dist.length; i++) {
			if (dist[i] > max) {
				classValue = i;
				max = dist[i];
			}
		}

		return classValue;
	}

}
