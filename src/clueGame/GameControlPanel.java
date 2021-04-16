package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class GameControlPanel extends JPanel {
	private JTextField theName;
	private JTextField theRoll;
	private JTextField theGuess;		//Text field displaying guess
	private JTextField theGuessResult;	//Text field displaying guess result
	
	
	public GameControlPanel() {
		setSize(800, 180);
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
		accusation.addActionListener(new AccusationListener());
		topPanel.add(accusation);
		JButton next = new JButton("Next!");
		next.addActionListener(new NextListener());
		topPanel.add(next);
		
		return topPanel;
	}
	
	class NextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Board theBoard = Board.getInstance();
			theBoard.handleNext();
			setTurn(theBoard.getCurrentPlayer(), theBoard.getCurrentRoll());
			if(theBoard.getLatestGuess() == null) {
				setGuess("No guess made this turn.");
				setGuessResult("None.", Color.white);
			} else {
				setGuess(theBoard.getLatestGuess().toString());
				setGuessResult("Not Disproven. . .", Color.white);
				if(theBoard.getLatestDisprove() != null) {
					Color guessDisproverColor = theBoard.getLatestDisprove().getCardHolder().getColor();
					String playerName = theBoard.getLatestDisprove().getCardHolder().getName();
					setGuessResult("Disproved by " + playerName, guessDisproverColor);
				}
			}
			repaint();
		}
	}
	
	
	class AccusationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Board theBoard = Board.getInstance();
			theBoard.handleHumanPlayerAccusation();
		}
	}
	
	
	//Create a panel to display the turn information
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		
		JPanel namePanel = new JPanel();
		JLabel nameLabel = new JLabel("Whose turn?");
		panel.add(nameLabel);
		panel.add(namePanel);
		
		JPanel textPanel = new JPanel();
		theName = new JTextField();
		theName.setEditable(false);
		panel.add(theName);
		panel.add(textPanel);
		
		return panel;
	}
	
	//Create a panel to display the die result
	private JPanel createDiePanel() {
		JPanel panel = new JPanel();
		
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
		theName.setEditable(true);
		theName.setText(player.getName());
		theName.setEditable(false);
		Border border = BorderFactory.createLineBorder(player.getColor(), 5);
		theName.setBorder(border);
		theRoll.setText(String.valueOf(dieRoll));
		repaint();
	}
	
	//Set a guess in the text field
	public void setGuess(String guess) {
		theGuess.setText(guess);
		repaint();
	}
	
	//Set a guess result in the text field
	public void setGuessResult(String guessResult, Color color) {
		theGuessResult.setText(guessResult);
		//theGuessResult.setBackground(color);
		Border border = BorderFactory.createLineBorder(color, 5);
		theGuessResult.setBorder(border);
		repaint();
	}
	
}
