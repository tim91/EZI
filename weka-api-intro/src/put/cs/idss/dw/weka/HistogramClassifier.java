package put.cs.idss.dw.weka;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.api.hyperdrive.NArrayObject;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Author
 */
public class HistogramClassifier extends Classifier {

	private static final long serialVersionUID = 7897533291024540981L;

	private int m_numClasses = 0;

	private double histogramSplits;
	
	public HistogramClassifier(int splits) {
		
		histogramSplits = splits;
	}

	/**
	 * Key - attribute id
	 * Value - AttributeMinMax
	 */
	private Map<Integer,AttributeMinMax> minMaxForAttributes;
	
	private NArrayObject<HistogramInterval> histogram;
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO build classifier here and use it to classify new instances
		data.deleteWithMissingClass();
		m_numClasses = data.numClasses();
		
		System.out.println("Classes: " + m_numClasses);
		System.out.println();
		
		minMaxForAttributes = new HashMap<Integer,AttributeMinMax>();
		
		for (int i = 0; i < data.numInstances(); ++i) {
			Instance instance = data.instance(i);
			
			int classIndex = instance.classIndex();
			
			int atrNum = instance.numAttributes();
			String desc = "\n----------------\nInstance: " + i + "\n";
			for(int a = 0; a < atrNum; a++){
			
				Attribute attr = instance.attribute(a);
				
				//ignore class attribute
				if(a == classIndex){
					continue;
				}
				
				AttributeMinMax minMax = minMaxForAttributes.get(a);
				
				if(minMax == null) {
					minMax = new AttributeMinMax();
				}
				
				double attrVal = instance.value(a);
				if(minMax.max < attrVal){
					minMax.max = attrVal;
				}
				
				if(minMax.min > attrVal){
					minMax.min = attrVal;
				}
				
				minMaxForAttributes.put(a, minMax);
				
				desc += attr.name() + " " + attrVal + " ";
			}
			System.out.println(desc);
		}
		System.out.println(minMaxForAttributes);
		//Create histogram
		histogram = createHistogramStructure();
		
		//store data in histogram structure
		
		for (int i = 0; i < data.numInstances(); ++i) {
			Instance instance = data.instance(i);
		
			storeInstanceInHistogram(instance);
			
		}
	}

	private NArrayObject<HistogramInterval> createHistogramStructure(){
		
		int[] dimensions = new int[minMaxForAttributes.keySet().size()];
		
		for (int i=0; i< dimensions.length; i++) {
			dimensions[i] = (int)histogramSplits;
		}
		
		return new NArrayObject<HistogramInterval>(dimensions);
	}
	
	private int[] getLocationOfInstanceInHistogram(Instance in){
		
		int[] indexInEveryDimension = new int[minMaxForAttributes.keySet().size()];
		int i = 0;
		
		for (int attrIdx : minMaxForAttributes.keySet()) {
			
			double av = in.value(attrIdx);
			AttributeMinMax amm = minMaxForAttributes.get(attrIdx);
			
			//calculate distance for this attribute from begining of histogram
			
			//make sure that given values are between min and max
			if(av < amm.min) av = amm.min;
			if(av > amm.max) av = amm.max;
			
			double distance = av - amm.min;
			//calculate interval in histogram
			int intervalIdx = (int)(distance / amm.getIntervalSize());
			
			if(intervalIdx == this.histogramSplits){
				intervalIdx--;
			}
			
			indexInEveryDimension[i] = intervalIdx;
			i++;
		}
		return indexInEveryDimension;
	}
	
	private void storeInstanceInHistogram(Instance in){
		
		int[] indexInHistogram = getLocationOfInstanceInHistogram(in);
		
		double classValue = in.classValue();
		HistogramInterval hi = this.histogram.get(indexInHistogram);
		
		if(hi == null) hi = new HistogramInterval();
		hi.addClass(classValue);
		this.histogram.set(indexInHistogram, hi);
		
	}
	
	@Override
	public double classifyInstance(Instance instance) throws Exception {
		
		int [] indexInHistogram = getLocationOfInstanceInHistogram(instance);
		
		HistogramInterval hi = this.histogram.get(indexInHistogram);
		if(hi == null){
			System.out.println("????????");
			return 0.0;
		}else{
			System.out.println("found");
		}
		
		return hi.getMostFrequentClass(); // class number; you can use distributionForInstance method
//		return 0.0;
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] dist = new double[m_numClasses];
		// dist[0] = 1.0; or something like this :)
		return dist;
	}
	
	/**
	 * Class store information about specific attribute
	 * @author Tomek
	 *
	 */
	private class AttributeMinMax
	{
		/**
		 * Min value of attribute in set
		 */
		public double min = Double.MAX_VALUE;
		/**
		 * Max value of attribute in set
		 */
		public double max = Double.MIN_VALUE;
		
		private double getRangeLength(){
			return Math.abs(min) + Math.abs(max);
		}
		
		public double getIntervalSize(){
			return getRangeLength() / histogramSplits;
		}
	}
	
	private class HistogramInterval{
		
		Map<Double,Integer> classesFrequency = new HashMap<Double, Integer>();
		
		private double mostFrequentClass = 0.0;
		
		public void addClass(double classNumber){
			
			Integer classVal = classesFrequency.get(classNumber);
			if(classVal == null) classVal = new Integer(1);
			else classVal ++;
			
			classesFrequency.put(classNumber, classVal);
			
			if(classNumber != mostFrequentClass){
				
				Integer mostFreq = classesFrequency.get(mostFrequentClass);
				if(mostFreq == null || mostFreq < classVal){
					mostFrequentClass = classNumber;
				}
			}
		}
		
		public double getMostFrequentClass(){
			return this.mostFrequentClass;
		}
		
	}
}
