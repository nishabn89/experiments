
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Robot that will make use of word list to guess the word based on the frequency of 
 * the letter
 * 
 */

public class HangmanRobo {

	
	StringBuilder consumedString = new StringBuilder();
	HashMap<Integer,ArrayList<String>> preprocessedText = new HashMap<Integer,ArrayList<String>>(); 
	/**
	 * The EOWL word list from Internet has been processed based on the length of 
	 * the words.
	 * 
	 */
	public void preProcessWords(){
		String path = "resources"; 
		ArrayList<String> list = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		   if (listOfFiles[i].isFile()){
			 try {
					Scanner sc = new Scanner(listOfFiles[i]);
					while(sc.hasNextLine()){
						String currWord = sc.nextLine().toString();
						if(preprocessedText.get(currWord.length()) == null){
							list = new ArrayList<String>();
						}else {
							list = preprocessedText.get(currWord.length());
						}
						list.add(currWord);
						preprocessedText.put(currWord.length(), list);
						
					}		
					sc.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		    }
		 }
	}
	
	/**
	 * Adds character that have been guessed so far.
	 * 
	 */
	public void addCharacter(char c){
		consumedString.append(c);
	}
	
	/**
	 * The toughest part !!! The intuition here is that chose the letter that has the highest frequency
	 * from the list of candidates. The candidates are those which satisfy the regular expression of 
	 * current state of the object. The candidates are first filtered based on the length
	 * 
	 */
	public char  nextGuess(String puzzle){
		List<String> words = new ArrayList<String>();
		words = Arrays.asList(puzzle.split("[^a-z_']+"));
		Iterator<String> itr = words.iterator();
		List<String> possibleString = new ArrayList<String>();
		double[] globalFreq = new double[26];
		
		//For every word, calculate the letter frequency and accumulate it with global frequency
		while(itr.hasNext()) {
			possibleString = new ArrayList<String>();
			String wordToMatch = itr.next();
			if(!wordToMatch.contains("_")) continue;
			ArrayList<String> possibleWords = preprocessedText.get(wordToMatch.length());
			Iterator<String> inneritr = possibleWords.iterator();
			Pattern regex = Pattern.compile(wordToMatch.replace("_", 
					(consumedString.length() > 0) ?
							String.format("[a-z&&[^%s]]", consumedString)
							: "[a-z]"));
			while(inneritr.hasNext()){
				String currWordToCheck = inneritr.next();
				Matcher m = regex.matcher(currWordToCheck);
	    		if(m.find())
	    			possibleString.add(currWordToCheck);
			}
			
			double[] thisfreq = new double[26];
			
			for(int i = 0; i < possibleString.size(); i++){
				String str = possibleString.get(i);
				int idx;
				for(int j = 0 ; j < str.length(); j++ ){
					idx = str.charAt(j) - 'a' ;
					if(idx < 0 || idx >= thisfreq.length) 
						continue;
					thisfreq[idx]++;
				}	
			}

			for(int i = 0; i < thisfreq.length; ++i) {
		        thisfreq[i] /= possibleString.size();
	        	globalFreq[i] += thisfreq[i];
	        }
		}
		
		//If by chance the consumed characters gets counted make it zero
		ArrayList<Character> consumedCharlist = new ArrayList<Character>();
		for(int i = 0; i < consumedString.length(); i++){
			consumedCharlist.add(consumedString.charAt(i));
			int idx = consumedString.charAt(i) - 'a' ;
			if(idx < 0 || idx >= globalFreq.length) 
				continue;
			globalFreq[idx] = 0;
				
		}
		
		char maxFrequencyChar = 'a';
		double maxFrequency = 0;
		//Calculate the maximum global frequency		
		for(int i = 0; i < globalFreq.length; ++i) {			
			if(globalFreq[i] > maxFrequency) {
				char c = (char)((int)'a' + i);
				if(consumedCharlist.contains(c)) 
					continue;
				maxFrequency = globalFreq[i];
				maxFrequencyChar = c;
			}
		}
		
		//If no potential candidate found return a random character.
		if(maxFrequency == 0){
			for(char c = 'a'; c <= 'z'; c++) {
				if(!consumedCharlist.contains(c)) {
					return c;
				}
			}
		}
		return maxFrequencyChar;
		
	}
	
	
	public static void main(String[] args) {
		HangmanRequest request = new HangmanRequest();
		HangmanRobo robo = new HangmanRobo();
		robo.preProcessWords();
		HangmanResponse responseObj = request.getNewGame();
		HangmanResponse currentResponse;
		while(responseObj.status == HangmanResponse.gameStatus.ALIVE){
			char guessChar = robo.nextGuess(responseObj.state);
			currentResponse = request.guessLetter(responseObj.token,guessChar);
			if(responseObj.state.equals(currentResponse.state)){
				System.out.println(currentResponse.state);
				System.out.println("Oops ! I thought it would be right. Huh.. Lemme try again");
			} else {
				System.out.println(currentResponse.state);
				System.out.println("That worked !!");
			}
			robo.addCharacter(guessChar);
			
			responseObj = currentResponse;
		}
		switch(responseObj.status) {
			case DEAD:
				System.out.println(responseObj.state);
				System.out.println("Sorry !! I tried my best. The prisoner is killed");
				break;
			case FREE:
				System.out.println(responseObj.state);
				System.out.println("Yaayy !! I freed the prisoner.");
				break;
			default:
				break;
		}
		
		
	}

}
