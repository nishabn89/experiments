package classification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class NBAdaboost {
	int noOfIterations;
	private ArrayList<ArrayList<Integer>> sampledDataForNextItr;
	private ArrayList<ArrayList<HashMap<Integer,HashMap<String,Double>>>> classifierModels;
	private ArrayList<Double> weights;
	private ArrayList<Double> errorOfClassifiers;
	ArrayList<Integer> classifiedData = new ArrayList<Integer>();
	private NaiveBayes nb ;
	
	//Gets an instance of naiveBayes to construct the weak classifiers
	public NBAdaboost(NaiveBayes nb){
		noOfIterations = 5;
		weights = new ArrayList<Double>();
		classifierModels = new ArrayList<ArrayList<HashMap<Integer,HashMap<String,Double>>>>();
		errorOfClassifiers = new ArrayList<Double>();
		this.nb =nb;
	}
	
	//Forms 5 classifierModel based on the error and updatedWeights
	public void boost(){
		//Initialise weights
		for(int i = 0; i < nb.trainData.size();i++)
			weights.add(((double)1/nb.trainData.size()));
		//Generate classifier
		double error;
		double currWeight;
		double updatedWeight;
		for(int i = 0; i < noOfIterations ; i ++){
			do {
				nb.trainData = sampleDataBasedOnWeight(nb.trainData);
				nb.buildDataModel();
				nb.buildClassifier();
				error = calculateError(nb.classifierModel);
				if(error > 0.5){
					for(int idx = 0; idx < nb.trainData.size();idx++)
						weights.set(idx,((double)1/nb.trainData.size()));
				}	
			} while (error > 0.5);
			classifierModels.add(nb.classifierModel);
			errorOfClassifiers.add(error);
			if(error == 0.0)
				break;
			//double oldweights = 0.0;
			double newweights = 0.0;
			double normalizedWeight = 0.0;
			
			for(int idx = 0; idx < nb.classifiedData.size(); idx++){
				if(nb.classifiedData.get(idx).equals(nb.testData.get(idx).get(0))){
					currWeight = weights.get(idx);
					updatedWeight = currWeight * (error/(1-error));
					weights.set(idx, updatedWeight);
				} 
				newweights = newweights + weights.get(idx);
			}
			
			for(int idx = 0; idx < nb.classifiedData.size(); idx++){
				currWeight = weights.get(idx);
				normalizedWeight = currWeight/newweights;
				weights.set(idx, normalizedWeight);
			}
		}
	}
	
	//Calculates the error of the new classifier model. The train data is made the test data to calculate the error
	private double calculateError(ArrayList<HashMap<Integer,HashMap<String,Double>>> classifierModel){
		BigDecimal error = new BigDecimal(0.0);
		nb.testData = nb.trainData;
		nb.classifyTestData();
		for(int i = 0; i < nb.classifiedData.size(); i++){
			if(!nb.classifiedData.get(i).equals(nb.testData.get(i).get(0))){
				error = error.add(new BigDecimal(weights.get(i))) ;
			} 
		}
		return error.doubleValue();
	}
	
	//Implements roulette-wheel selection to sample based on weight and updates the weights array accordingly
	private ArrayList<ArrayList<Integer>> sampleDataBasedOnWeight(ArrayList<ArrayList<Integer>> sampledData ){
		sampledDataForNextItr = new ArrayList<ArrayList<Integer>>();
		ArrayList<Double> cumulativeWeights = new ArrayList<Double>();
		Random randomWeight = new Random();
		double candidateWeight = 0.0;
		//roulette-wheel selection algorithm
		cumulativeWeights.add(weights.get(0));
		for(int i = 1; i < sampledData.size(); i++){
			cumulativeWeights.add(cumulativeWeights.get(i-1) + weights.get(i));
		}
		for(int i = 0; i < sampledData.size(); i++){
			candidateWeight = randomWeight.nextDouble() * cumulativeWeights.get(weights.size() - 1) ;
			int candidate = Arrays.binarySearch(cumulativeWeights.toArray(),candidateWeight);
			if(candidate < 0)
				candidate = Math.abs(candidate+1);
			//Find the item
			sampledDataForNextItr.add(sampledData.get(candidate));
		}
		
		return sampledDataForNextItr;
	}
	
	//Used the 5 classifierModels and its weight to classify the test data
	public void ensemble(){
		double currClassifierWeight;
		ArrayList<HashMap<String,Double>> classifierWeightYesNo = new ArrayList<HashMap<String,Double>>();
		HashMap<String,Double> currRecord;
		for(int idx = 0;idx < nb.classifiedData.size(); idx++){
			currRecord = new HashMap<String,Double>();
			currRecord.put("yes", 0.0);
			currRecord.put("no", 0.0);
			classifierWeightYesNo.add(currRecord);
		}
		for(int i =0; i < classifierModels.size();i++){
			BigDecimal num = new BigDecimal(1-errorOfClassifiers.get(i));
			BigDecimal den = new BigDecimal(errorOfClassifiers.get(i));
			currClassifierWeight = Math.log(num.doubleValue()/den.doubleValue());
			nb.classifierModel = classifierModels.get(i);
			nb.classifyTestData();
			//nb.measureClassifierQuality();
			for(int idx = 0;idx < nb.classifiedData.size(); idx++){
				currRecord = classifierWeightYesNo.get(idx);
				if(nb.classifiedData.get(idx).equals(1)){
					double currRecordWeight = currRecord.get("yes");
					currRecord.put("yes", currClassifierWeight + currRecordWeight);
				} else {
					double currRecordWeight = currRecord.get("no");
					currRecord.put("no", currClassifierWeight + currRecordWeight);
				}
				classifierWeightYesNo.set(idx,currRecord);
				if(i == classifierModels.size()-1){
					currRecord = classifierWeightYesNo.get(idx);
					if(currRecord.get("yes") >= currRecord.get("no"))
						classifiedData.add(1);
					else
						classifiedData.add(0);
				}
			}
		}	
	}
	
	//Measures the quality of the ensemble classifier by counting four parameters
	public void measureClassifierQuality(){
		int falsePositive = 0;
		int falseNegative = 0;
		int truePositive = 0;
		int trueNegative = 0;
		for(int i = 0; i < classifiedData.size(); i++){
			if(classifiedData.get(i).equals(nb.testData.get(i).get(0)) &&
					classifiedData.get(i) == 1){
				truePositive++;
			} else if(classifiedData.get(i).equals(nb.testData.get(i).get(0)) &&
					classifiedData.get(i) == 0){
				trueNegative++;
			} else if(!classifiedData.get(i).equals(nb.testData.get(i).get(0)) &&
					classifiedData.get(i) == 1){
				falsePositive++;
			} else if(!classifiedData.get(i).equals(nb.testData.get(i).get(0)) &&
					classifiedData.get(i) == 0){
				falseNegative++;
			} 
		}
		System.out.println(truePositive);
		System.out.println(falseNegative);	
		System.out.println(falsePositive);
		System.out.println(trueNegative);
	}
	
	public static void main (String[] args) throws Exception{
		NBAdaboost ab = new NBAdaboost(new NaiveBayes());
		ab.nb.buildTrainData(args[0]);
		ab.boost();
		ab.nb.buildTestData(args[1]);
		ab.ensemble();
		ab.measureClassifierQuality();
	}
}
