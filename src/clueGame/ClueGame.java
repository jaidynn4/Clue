package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class ClueGame extends JFrame {
	private GameControlPanel gcPanel;		//Game Control Panel GUI
	private CardsDisplayPanel cdPanel;		//Cards Display Panel GUI
	private Board bPanel;					//Board GUI
	private String setupConfigFile;			//
	private String layoutConfigFile;
	
	public ClueGame(String layoutConfigFile, String setupConfigFile) {
		//Create the panels, ensuring that the Singleton board panel is initialized
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
		
		gcPanel = new GameControlPanel();
		cdPanel = new CardsDisplayPanel();
		bPanel = Board.getInstance();
		
		bPanel.setConfigFiles(layoutConfigFile, setupConfigFile);
		bPanel.initialize();
		bPanel.setCdPanel(cdPanel);
		bPanel.setGcPanel(gcPanel);
		
		gcPanel.setGuess("No guess made this turn.");
		gcPanel.setGuessResult("None.", Color.white);
		
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
	
	public void run() {
		//Get the list of players
		ArrayList<Player> playerList = bPanel.getPlayerList();
		
		//Get the name of the first human player in the player map
		String humanPlayerName = playerList.get(0).getName();
		
		//Make a pop-up pane for a welcome message
		JOptionPane popup = new JOptionPane();
		
		//Display pop-up with a welcome message corresponding to the chosen map
		if(layoutConfigFile.equals("ClueLayoutEbonHawk.csv") && setupConfigFile.equals("ClueSetupEbonHawk.txt")) {
			popup.showMessageDialog(this, "You are playing as " + humanPlayerName + ". \nThere has been a murder aboard the Ebon Hawk! \n"
										+ "Kreia has tragically (maybe) been found dead. \nWhich of your fellow crew members could be responsible? "
										+ "\nYou must solve this mystery before it's TOO LATE!");
		} else {
			popup.showMessageDialog(this,"You are playing as " + humanPlayerName + ". \nThere has been a murder aboard the ISD Executor! \n"
										+ "Random Stormtrooper #6953 has tragically been found dead. \nYou and your fellow officials "
										+ "are all potentially capable of murder. . . and you yourself could be the next victim."
										+ " \nYou must solve this mystery before it's TOO LATE!");
		}
		//Start first turn with player 0, and set isHumanPlayer to true
		Board.getInstance().setCurrentPlayer(playerList.get(0));
		cdPanel.populateHand(playerList.get(0));
		Board.getInstance().doNextTurn(true);
		
		gcPanel.setTurn(playerList.get(Board.getInstance().getCurrentPlayerIndex()), Board.getInstance().getCurrentRoll());
	}
	
	public GameControlPanel getGcPanel() {
		return gcPanel;
	}
	
}