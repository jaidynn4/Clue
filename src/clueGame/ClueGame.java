package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

public class ClueGame extends JFrame {
	private GameControlPanel gcPanel;		//Game Control Panel GUI
	private CardsDisplayPanel cdPanel;		//Cards Display Panel GUI
	private Board bPanel;					//Board GUI
	
	public ClueGame() {
		//Create the panels, ensuring that the Singleton board panel is initialized
		gcPanel = new GameControlPanel();
		cdPanel = new CardsDisplayPanel();
		bPanel = Board.getInstance();
		bPanel.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		bPanel.initialize();
		
		//Add the panels to the JFrame
		add(gcPanel, BorderLayout.SOUTH);
		add(cdPanel, BorderLayout.EAST);
		add(bPanel, BorderLayout.CENTER);
		
		setSize(1200, 900); 							//Size the frame
		setTitle("Clue Game - Star Wars Edition");		//Name the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Allow the panel to close
		setExtendedState(JFrame.MAXIMIZED_BOTH); 		//Set the frame to fullscreen
		setUndecorated(false);							//Ensure that the window controls appear
		setVisible(true); 								//Make it visible
	}
	
	public static void main(String[] args) {
		
		ClueGame frame = new ClueGame();						//Create the frame
		
		//Test filling in the data for GameControlPanel class
		frame.gcPanel.setTurn(new ComputerPlayer("Emperor Palpatine", Color.magenta, 0, 0), 5);
		frame.gcPanel.setGuess("I have no guess!");
		frame.gcPanel.setGuessResult("So you have nothing?");
		
		//Testing functionality of updatePanel() method in CardStatusPanel class
		frame.cdPanel.getWeaponsPanel().getHandPanel().updatePanel("Blaster", Color.white);
		frame.cdPanel.getWeaponsPanel().getSeenPanel().updatePanel("Lightsaber", Color.red);
		frame.cdPanel.getWeaponsPanel().getHandPanel().updatePanel("Hydrospanner", Color.white);
		frame.cdPanel.getWeaponsPanel().getSeenPanel().updatePanel("Thermal Detonator", Color.magenta);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Admiral Thrawn", Color.yellow);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Grand Moff Tarkin", Color.blue);
		frame.cdPanel.getPeoplePanel().getSeenPanel().updatePanel("Emperor Palpatine", Color.yellow);
	}
}
