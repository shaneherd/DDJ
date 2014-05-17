import java.util.ArrayList;


public class DotComBust {
	private GameHelper helper = new GameHelper();
	private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
	private int numOfGuesses = 0;
	private ArrayList<String> guessesRemaining = new ArrayList<String>();
	
	private void setUpGame(){
		DotCom one = new DotCom();
		one.setName("Pets.com");
		DotCom two = new DotCom();
		two .setName("eToys.com");
		DotCom three = new DotCom();
		three.setName("Go2.com");
		
		dotComsList.add(one);
		dotComsList.add(two);
		dotComsList.add(three);
		
		guessesRemaining.add("a1");
		guessesRemaining.add("a2");
		guessesRemaining.add("a3");
		guessesRemaining.add("a4");
		guessesRemaining.add("a5");
		guessesRemaining.add("a6");
		guessesRemaining.add("a7");
		
		guessesRemaining.add("b1");
		guessesRemaining.add("b2");
		guessesRemaining.add("b3");
		guessesRemaining.add("b4");
		guessesRemaining.add("b5");
		guessesRemaining.add("b6");
		guessesRemaining.add("b7");
		
		guessesRemaining.add("c1");
		guessesRemaining.add("c2");
		guessesRemaining.add("c3");
		guessesRemaining.add("c4");
		guessesRemaining.add("c5");
		guessesRemaining.add("c6");
		guessesRemaining.add("c7");
		
		guessesRemaining.add("d1");
		guessesRemaining.add("d2");
		guessesRemaining.add("d3");
		guessesRemaining.add("d4");
		guessesRemaining.add("d5");
		guessesRemaining.add("d6");
		guessesRemaining.add("d7");
		
		guessesRemaining.add("e1");
		guessesRemaining.add("e2");
		guessesRemaining.add("e3");
		guessesRemaining.add("e4");
		guessesRemaining.add("e5");
		guessesRemaining.add("e6");
		guessesRemaining.add("e7");
		
		guessesRemaining.add("f1");
		guessesRemaining.add("f2");
		guessesRemaining.add("f3");
		guessesRemaining.add("f4");
		guessesRemaining.add("f5");
		guessesRemaining.add("f6");
		guessesRemaining.add("f7");
		
		guessesRemaining.add("g1");
		guessesRemaining.add("g2");
		guessesRemaining.add("g3");
		guessesRemaining.add("g4");
		guessesRemaining.add("g5");
		guessesRemaining.add("g6");
		guessesRemaining.add("g7");
		
		System.out.println("Your goal is to sink three dot coms.");
		System.out.println("Pets.com, eToys.com, Go2.com");
		System.out.println("Try to sink them all in the fewest number of guesses");
		
		for (DotCom dotComToSet : dotComsList){
			ArrayList<String> newLocation = helper.placeDotCom(3);
			dotComToSet.setLocationCells(newLocation);
		}
	}
	
	private void startPlaying(){
		while (!dotComsList.isEmpty()){
			String userGuess = helper.getUserInput("Enter a guess");
			checkUserGuess(userGuess);
		}
		finishGame();
	}
	
	private void checkUserGuess(String userGuess){
		numOfGuesses++;
		String result = "miss";
		for (int x = 0; x < dotComsList.size(); x++){
			result = dotComsList.get(x).checkYourself(userGuess);
			if (result.equals("hit")){
				break;
			}
			if (result.equals("kill")){
				dotComsList.remove(x);
				break;
			}
		}
		System.out.println(result);
		
		guessesRemaining.remove(userGuess);
		System.out.println("Options Remaining");
		for (int i = 0; i < guessesRemaining.size(); i++){
			System.out.print(guessesRemaining.get(i) + " ");
		}
		System.out.print("\n");
		
	}
	
	private void finishGame(){
		System.out.println("All Dot Coms are dead! Your stock is now worthless.");
		if (numOfGuesses <= 18){
			System.out.println("It only took you " + numOfGuesses + " guesses.");
			System.out.println("You got out before your options sank");
		}
		else {
			System.out.println("Took you long enough. " + numOfGuesses + " guesses.");
			System.out.println("Fish are dancing with your options");
		}
	}
	
	public static void main(String[] args){
		DotComBust game = new DotComBust();
		game.setUpGame();
		game.startPlaying();
	}
}
