package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

public class ClueGame extends JFrame {
	private GameControlPanel gcPanel;
	private CardsDisplayPanel cdPanel;
	private Board bPanel;
	
	public ClueGame() {
		gcPanel = new GameControlPanel();				//Create the panel
		cdPanel = new CardsDisplayPanel();
		bPanel = Board.getInstance();
		bPanel.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		bPanel.initialize();
		
		add(gcPanel, BorderLayout.SOUTH);
		add(cdPanel, BorderLayout.EAST);
		add(bPanel, BorderLayout.CENTER);
		
		setSize(800, 800); 								//Size the frame
		setTitle("Clue Game - Star Wars Edition");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Allow the panel to close
		setVisible(true); 								//Make it visible
	}
	
	public static void main(String[] args) {
		
		ClueGame frame = new ClueGame();						//Create the frame
		
		
		
		//Test filling in the data
		frame.gcPanel.setTurn(new ComputerPlayer("Col. Mustard", Color.orange, 0, 0), 5);
		frame.gcPanel.setGuess("I have no guess!");
		frame.gcPanel.setGuessResult("So you have nothing?");
		
		//Testing functionality of updatePanel() method in CardStatusPanel class
		frame.cdPanel.getWeaponsPanel().getHandPanel().updatePanel("Blaster", Color.white);
		frame.cdPanel.getWeaponsPanel().getSeenPanel().updatePanel("Lightsaber", Color.red);
		frame.cdPanel.getWeaponsPanel().getHandPanel().updatePanel("Hydrospanner", Color.white);
		frame.cdPanel.getWeaponsPanel().getSeenPanel().updatePanel("Thermal Detonator", Color.gray);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Admiral Thrawn", Color.yellow);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Grand Moff Tarkin", Color.blue);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Emperor Palpatine", Color.yellow);
	}
}
