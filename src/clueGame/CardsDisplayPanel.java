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
		setSize(180, 620); 	
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
}
