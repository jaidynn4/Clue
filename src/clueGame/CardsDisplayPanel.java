package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class CardsDisplayPanel extends JPanel {
	private CardTypePanel peoplePanel;		//Panel for people cards
	private CardTypePanel roomsPanel;		//Panel for room cards
	private CardTypePanel weaponsPanel;		//Panel for weapon cards
	
	//Constructor
	public CardsDisplayPanel() {
		super();
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		createCategoryPanels();
	}
	
	//Make a CardTypePanel for each of the three card types
	public void createCategoryPanels() {
		peoplePanel = new CardTypePanel(CardType.PERSON);
		roomsPanel = new CardTypePanel(CardType.ROOM);
		weaponsPanel = new CardTypePanel(CardType.WEAPON);
		
		add(peoplePanel);
		add(roomsPanel);
		add(weaponsPanel);
	
	}
	
	//Getter for peoplePanel
	public CardTypePanel getPeoplePanel() {
		return peoplePanel;
	}

	//Getter for roomsPanel
	public CardTypePanel getRoomsPanel() {
		return roomsPanel;
	}

	//Getter for weaponsPanel
	public CardTypePanel getWeaponsPanel() {
		return weaponsPanel;
	}
		
	//Main method to run the test code
	public static void main(String[] args) {
		JFrame frame = new JFrame();							//Create the frame
		CardsDisplayPanel panel = new CardsDisplayPanel();		//Create the panel
		
		//Testing functionality of updatePanel() method in CardStatusPanel class
		panel.getWeaponsPanel().getHandPanel().updatePanel("Blaster", Color.white);
		panel.getWeaponsPanel().getSeenPanel().updatePanel("Lightsaber", Color.red);
		panel.getWeaponsPanel().getHandPanel().updatePanel("Hydrospanner", Color.white);
		panel.getWeaponsPanel().getSeenPanel().updatePanel("Thermal Detonator", Color.gray);
		panel.getPeoplePanel().getSeenPanel().updatePanel("Admiral Thrawn", Color.yellow);
		panel.getPeoplePanel().getSeenPanel().updatePanel("Grand Moff Tarkin", Color.blue);
		panel.getPeoplePanel().getSeenPanel().updatePanel("Emperor Palpatine", Color.yellow);

		frame.setContentPane(panel);							//Put the panel in the frame
		frame.setSize(180, 750); 								//Size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	//Allow the panel to close
		frame.setVisible(true); 								//Make it visible
		
	}
}
