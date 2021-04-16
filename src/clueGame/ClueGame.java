package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

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
		bPanel.setCdPanel(cdPanel);
		bPanel.setGcPanel(gcPanel);
		
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
		
		frame.gcPanel.setGuess("No guess made this turn.");
		frame.gcPanel.setGuessResult("None.", Color.white);
		
		//Get the list of players
		ArrayList<Player> playerList = frame.bPanel.getPlayerList();
		
		//Get the name of the first human player in the player map
		String humanPlayerName = playerList.get(0).getName();
		
		//Make a pop-up pane with a welcome message
		JOptionPane popup = new JOptionPane();
		
		//Display pop-up
		popup.showMessageDialog(frame,"You are playing as " + humanPlayerName + " and you must solve this mystery before it's TOO LATE!");
		
		//Start first turn with player 0, and set isHumanPlayer to true
		Board.getInstance().setCurrentPlayer(playerList.get(0));
		frame.cdPanel.populateHand(playerList.get(0));
		Board.getInstance().doNextTurn(true);
		
		frame.gcPanel.setTurn(playerList.get(Board.getInstance().getCurrentPlayerIndex()), Board.getInstance().getCurrentRoll());
	}

	public GameControlPanel getGcPanel() {
		return gcPanel;
	}
	
}
