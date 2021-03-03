package clueGame;

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super("Error with Config File Format.");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}

//Throw:
//If the board layout refers to a room that is not in your setup file
//If the board layout file does not have the same number of columns in every row.
//If an entry in either file does not have the proper format.

//EXCRED: when thrown, write a message to a log file