
public class HangmanResponse {
		public enum gameStatus { ALIVE, DEAD, FREE };
		public gameStatus status;
		public String token;
		public int remainingGuess;
		public String state;
}
