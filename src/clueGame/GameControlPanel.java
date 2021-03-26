package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class GameControlPanel extends JPanel {
	private JTextField theName;
	private JTextField theRoll;
	private JTextField theGuess;	//Text field displaying guess
	private JTextField theGuessResult;	//Text field displaying guess result
	
	
	public GameControlPanel() {
		//TODO constructor
		setLayout(new GridLayout(2,0));
		JPanel panel = createTopPanel();
		add(panel);
		
		
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0,2));
		
		//Guess panel
		bottomPanel.add(createGuessPanel());
		
		//Guess result panel
		bottomPanel.add(createGuessResultPanel());
		
		//Be able to set/update info in fields with setters
		add(bottomPanel);
	}
	
	
	//Create the top panel that will hold several subpanels
	public JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,4));
		topPanel.add(createTurnPanel());
		topPanel.add(createDiePanel());
		
		JButton accusation = new JButton("Make Accusation");
		topPanel.add(accusation);
		JButton next = new JButton("Next!");
		topPanel.add(next);
		
		return topPanel;
	}
	
	//Create a panel to display the turn information
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		JLabel nameLabel = new JLabel("Whose turn?");
		panel.add(nameLabel);
		theName = new JTextField();
		theName.setEditable(false);
		panel.add(theName);
		
		return panel;
	}
	
	//Create a panel to display the die result
	private JPanel createDiePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		
		JLabel dieLabel = new JLabel("Roll:");
		panel.add(dieLabel);
		
		theRoll = new JTextField(1);
		theRoll.setEditable(false);
		panel.add(theRoll);
		
		//Add two blank panels to the grid layout to ensure that the roll panels are in the top half
		panel.add(new JPanel());
		panel.add(new JPanel());
		return panel;
	}
	
	//Create a panel to display the guess
	private JPanel createGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		
		theGuess = new JTextField();
		theGuess.setEditable(false);
		panel.add(theGuess);
		
		return panel;
	}
	
	//Create a panel to display the guess result
	private JPanel createGuessResultPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		
		theGuessResult = new JTextField();
		theGuessResult.setEditable(false);
		panel.add(theGuessResult);
		
		return panel;
	}
	
	//Set the player and die roll information in the respective text fields
	public void setTurn(Player player, int dieRoll) {
		theName.setText(player.getName());
		theName.setBackground(player.getColor());
		theRoll.setText(String.valueOf(dieRoll));
	}
	
	//Set a guess in the text field
	public void setGuess(String guess) {
		theGuess.setText(guess);
	}
	
	//Set a guess result in the text field
	public void setGuessResult(String guessResult) {
		theGuessResult.setText(guessResult);
	}
	
	
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();		//Create the panel
		JFrame frame = new JFrame();							//Create the frame
		frame.setContentPane(panel);							//Put the panel in the frame
		frame.setSize(750, 180); 								//Size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	//Allow the panel to close
		frame.setVisible(true); 								//Make it visible
		
		
		//Test filling in the data
		panel.setTurn(new ComputerPlayer("Col. Mustard", Color.orange, 0, 0), 5);
		panel.setGuess("I have no guess!");
		panel.setGuessResult("So you have nothing?");
	}
}
