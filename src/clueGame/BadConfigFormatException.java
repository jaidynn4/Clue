package clueGame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super("Error with Config File Format.");
	}
	
	//Creates new exception with a custom message passed in where it is thrown and appends it to a log file
	public BadConfigFormatException(String message) {
		super(message);
		try {
			//Check if file already exists, so we can append vs make a new file
			File file = new File("exceptionlog.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file,true);
			//Opens writers for file that will be used
	    	BufferedWriter bw = new BufferedWriter(fw);
	    	PrintWriter out = new PrintWriter(bw);
	    	//Sends message to log file 
			out.println(message);
			out.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}