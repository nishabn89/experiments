
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;


public class HangmanRequest {
	private static String urlName = "http://gallows.hulu.com/play?code=nisha.b.89@gmail.com";
	HangmanRequest(){}
	
	public HangmanResponse sendRequest(String urlParams){
		StringBuilder sb = new StringBuilder();
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlName + urlParams).openStream()));
			for (String str; ((str = in.readLine()) != null); sb.append(str))
					;
			in.close();
		} catch(Exception e) {}
		HangmanResponse responseObj = new Gson().fromJson(sb.toString(), HangmanResponse.class);
		responseObj.state = responseObj.state.toLowerCase();
		return responseObj;	
	}
	
	public HangmanResponse getNewGame(){
		return sendRequest("");
	}

	public HangmanResponse guessLetter(String token, char guessChar){
		return sendRequest(String.format("&token=%s&guess=%s",token, guessChar));
	}
}
