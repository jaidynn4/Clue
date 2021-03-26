package clueGame;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class GameControlPanel extends JPanel {
	private JTextField name;
	private JTextField roll;
	private JTextField theGuess;	//Text field displaying guess
	private JTextField theGuessResult;	//Text field displaying guess result
	
	
	public GameControlPanel() {
		//TODO constructor
		setLayout(new GridLayout(2,0));
		JPanel panel = setTurn(new ComputerPlayer("Col. Mustard", Color.orange, 0, 0), 5);
		add(panel);
		
		//Add turn panel
		
		//Add roll panel
		
		//Add accusation button
		
		//Add next button
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0,2));
		
		//Guess panel
		
		//Guess result panel
		
		
		
		//Be able to set/update info in fields with setters
		
	}
	
	
	
	public JPanel setTurn(Player player, int dieRoll) {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,4));
		topPanel.add(createTurnPanel());
		topPanel.add(createDiePanel());
		topPanel.add(createButtonPanel());
		return topPanel;
	}
	
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		JLabel nameLabel = new JLabel("Whose turn?");
		name = new JTextField(20);
		panel.add(nameLabel);
		panel.add(name);
		return panel;
	}
	
	private JPanel createDiePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		JLabel nameLabel = new JLabel("Roll:");
		roll = new JTextField(5);
		panel.add(nameLabel);
		panel.add(roll);
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JButton agree = new JButton("Make Accusation");
		JButton disagree = new JButton("Next ->");
		JPanel panel = new JPanel();
		panel.add(agree);
		panel.add(disagree);
		return panel;
	}
	
	public void setGuess(String guess) {
		theGuess.setText(guess);
	}
	
	
	public void setGuessResult(String guessResult) {
		theGuessResult.setText(guessResult);
	}
	
	
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();	//Create the panel
		JFrame frame = new JFrame();	//Create the frame
		frame.setContentPane(panel);		//Put the panel in the frame
		frame.setSize(750, 180); 	//Size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	//Allow the panel to close
		frame.setVisible(true); 	//Make it visible
		
		//Test filling in the data
		panel.setTurn(new ComputerPlayer("Col. Mustard", Color.orange, 0, 0), 5);
		//panel.setGuess("I have no guess!");
		//panel.setGuessResult("So you have nothing?");
	}
}
