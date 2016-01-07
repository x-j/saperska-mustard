package saperskaMustard;

public class Player {

	String username;
	
	//i'm guessing here goes the weird server connection stuff	
	
	
	public Player(String username) {

		this.username = username;
		
	}
	
	@Override
	public String toString() {
		return username;
	}
}
