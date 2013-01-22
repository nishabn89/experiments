package classification;

import java.io.File;
import java.util.*;


public class NaiveBayes {
	private ArrayList<HashMap<Integer,ArrayList<Integer>>> dataSet;
	public ArrayList<HashMap<Integer,HashMap<String,Double>>> classifierModel;
	private ArrayList<Integer> yesClassifier ;
	private ArrayList<Integer> noClassifier;
	public ArrayList<ArrayList<Integer>> testData = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> trainData = new ArrayList<ArrayList<Integer>>();
	public ArrayList<Integer> classifiedData = new ArrayList<Integer>();
	
	public NaiveBayes(){
		
	}
	
	//Constructs the data structure such that, the probabilities can be calculated easily while constructing the model
	public void buildDataModel(){
		dataSet = new ArrayList<HashMap<Integer,ArrayList<Integer>>>();
		yesClassifier = new ArrayList<Integer>();
		noClassifier = new ArrayList<Integer>();
		Iterator<ArrayList<Integer>> itr = trainData.iterator();
		ArrayList<Integer> currRecord = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<Integer>> currHashMap;
		ArrayList<Integer> currList;
		int recordIndex = 0;
		while(itr.hasNext()){
			currRecord = itr.next();
			if(currRecord.get(0) == 1)
				yesClassifier.add(recordIndex);
			else
				noClassifier.add(recordIndex);
			for(int i = 1; i < currRecord.size(); i++){
				if(recordIndex == 0 ){
					currHashMap = new HashMap<Integer, ArrayList<Integer>>();
					currList = new ArrayList<Integer>();
					currList.add(recordIndex);
					currHashMap.put(currRecord.get(i), currList);
					dataSet.add(currHashMap);
				} else {
					currHashMap = dataSet.get(i-1);
					if(currHashMap.get(currRecord.get(i)) == null){
						currList = new ArrayList<Integer>();
						currList.add(recordIndex);
						currHashMap.put(currRecord.get(i), currList);
						dataSet.set(i-1,currHashMap);
					} else {
						currList = currHashMap.get(currRecord.get(i));
						currList.add(recordIndex);
						currHashMap.put(currRecord.get(i), currList);
						dataSet.set(i-1,currHashMap);
					}
					
				}
			}
			recordIndex++;
		}
	}
	
	//Coverts data in train file to array
	public void buildTrainData(String train_file){
		String currRecord = "";
		String [] currAttributes;
		 trainData = new ArrayList<ArrayList<Integer>>();
		try {
			Scanner sc = new Scanner(new File(train_file));
			while(sc.hasNextLine()){
				ArrayList<Integer> currList = new ArrayList<Integer>();
				currRecord = sc.nextLine().toString();
				currAttributes = currRecord.split("\t");
				if(currAttributes[0].indexOf("+1") != -1)
					currList.add(1);
				else
					currList.add(0);
				for(int i = 1; i < currAttributes.length; i++){
					currList.add(Integer.parseInt(currAttributes[i]));
				}
				trainData.add(currList);
			}		
			sc.close();
		}catch(Exception e){}	
		
	}
	
	//Converts data in test file to array
	public void buildTestData(String test_file) {
	    testData = new ArrayList<ArrayList<Integer>>();
		String currRecord = "";
		String [] currAttributes;
		try {
			Scanner sc = new Scanner(new File(test_file));
			while(sc.hasNextLine()){
				ArrayList<Integer> currList = new ArrayList<Integer>();
				currRecord = sc.nextLine().toString();
				currAttributes = currRecord.split("\t");
				if(currAttributes[0].indexOf("+1") != -1)
					currList.add(1);
				else
					currList.add(0);
				for(int i = 1; i < currAttributes.length; i++){
					currList.add(Integer.parseInt(currAttributes[i]));
				}
				testData.add(currList);
			}		
			sc.close();
		}catch(Exception e){}
	}
	//Builds the classifier model
	public void buildClassifier(){
		classifierModel = new  ArrayList<HashMap<Integer,HashMap<String,Double>>>();
		double classProbYes = 0; 
		double classProbNo = 0; 
		for(int i = 0; i < dataSet.size(); i++){
			HashMap<Integer,HashMap<String,Double>> currAttrClassifier = new HashMap<Integer,HashMap<String,Double>>();
			Set<Integer> s = dataSet.get(i).keySet();
			Iterator<Integer> itr = s.iterator();
			int laplacianAdj  = dataSet.get(i).keySet().size();
			//int laplacianAdj  = 0;
			while(itr.hasNext()) {
				int key = itr.next();
				HashMap<String,Double> currClassifier = new HashMap<String,Double>();
				classProbYes = (double)countXGivenY(dataSet.get(i).get(key),yesClassifier)/(yesClassifier.size()+laplacianAdj);
				classProbNo = (double)countXGivenY(dataSet.get(i).get(key),noClassifier)/(noClassifier.size()+laplacianAdj);
				currClassifier.put("yes", classProbYes);
				currClassifier.put("no", classProbNo);
				currAttrClassifier.put(key, currClassifier);
			}
			classifierModel.add(currAttrClassifier);
		}
	}
	
	//Uses the classifier model to classify the test data
	public void classifyTestData(){
	   classifiedData = new ArrayList<Integer>();
	   for(int i = 0; i < testData.size();i++){
			double classifierYesProb = 1;
			double classifierNoProb = 1;
			//Ignoring classifier information
			for(int j = 0; j < classifierModel.size(); j++){
				if(classifierModel.get(j).get(testData.get(i).get(j+1)) != null)
					classifierYesProb = classifierYesProb*(classifierModel.get(j).get(testData.get(i).get(j+1)).get("yes"));
				if(classifierModel.get(j).get(testData.get(i).get(j+1)) != null)
					classifierNoProb = classifierNoProb*(classifierModel.get(j).get(testData.get(i).get(j+1)).get("no"));
			}
			if (classifierYesProb >= classifierNoProb) {
				classifiedData.add(1);
			} else {
				classifiedData.add(0);
			}
		}
	}

	//Measures the quality of classifier by counting four parameters
	public void measureClassifierQuality(){
		int falsePositive = 0;
		int falseNegative = 0;
		int truePositive = 0;
		int trueNegative = 0;
		for(int i = 0; i < classifiedData.size(); i++){
			if(classifiedData.get(i) == testData.get(i).get(0) &&
					classifiedData.get(i) == 1){
				truePositive++;
			} else if(classifiedData.get(i) == testData.get(i).get(0) &&
					classifiedData.get(i) == 0){
				trueNegative++;
			} else if(classifiedData.get(i) != testData.get(i).get(0) &&
					classifiedData.get(i) == 1){
				falsePositive++;
			} else if(classifiedData.get(i) != testData.get(i).get(0) &&
					classifiedData.get(i) == 0){
				falseNegative++;
			} 
		}
		System.out.println(truePositive);
		System.out.println(falseNegative);	
		System.out.println(falsePositive);
		System.out.println(trueNegative);
	}
	
	//Calculates P(A/B)
	private int countXGivenY(ArrayList<Integer> x, ArrayList<Integer> y){
		//Laplacian adjustment so that probability is not zero
		int count = 1;
		
		for(int j = 0; j < y.size(); j++){
			for(int i = 0; i < x.size(); i++){
			if(x.get(i).equals(y.get(j)))
				count++;
			}
		}
		return count;
		
	}
	
	public static void main (String[] args) throws Exception{
		NaiveBayes nb = new NaiveBayes();
		nb.buildTrainData(args[0]);
		nb.buildDataModel();
		nb.buildClassifier();
		nb.buildTestData(args[1]);
		nb.classifyTestData();
		nb.measureClassifierQuality();
		
	}
}

