
public class SimpleDotComTestDrive {
	public static void main (String[] args){
		int numOfGuesses = 0;
		GameHelper helper = new GameHelper();		
		SimpleDotCom dot = new SimpleDotCom();
		
		int randomNum = (int)(Math.random()*5);
		int[] locations = {randomNum, randomNum+1, randomNum+2};
		dot.setLocationCells(locations);
		boolean isAlive = true;
		
		while (isAlive){
			String guess = helper.getUserInput("Enter a Number");
			String result = dot.checkYourself(guess);
			numOfGuesses++;
			
			if (result.equals("kill")){
				isAlive = false;
				System.out.println("You took " + numOfGuesses + " guesses");
			}
		}	
	}
}
