package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class CardTypePanel extends JPanel {
	private CardStatusPanel handPanel;		//Panel for hand cards
	private CardStatusPanel seenPanel;		//Panel for seen cards
	private CardType type;					//Enum to represent type of cards this panel will hold
	
	//Constructor
	public CardTypePanel(CardType type){
		super();
		this.type = type;
		setLayout(new GridLayout(2,1));
		
		//Set a string for the border label depending on the CardType enum
		String typeString = "ERROR";
		switch(type) {
		case PERSON:
			typeString = "People";
			break;
		case ROOM:
			typeString = "Rooms";
			break;
		case WEAPON:
			typeString = "Weapons";
		}
		
		setBorder(new TitledBorder (new EtchedBorder(), typeString));
		createCardStatusPanels();
	}
	
	//Helper method to create two panels for hand cards and seen cards
	public void createCardStatusPanels() {
		handPanel = new CardStatusPanel(StatusCategory.HAND);
		seenPanel = new CardStatusPanel(StatusCategory.SEEN);
		add(handPanel);
		add(seenPanel);
	}
	
	//Getter for handPanel
	public CardStatusPanel getHandPanel() {
		return handPanel;
	}

	//Getter for seenPanel
	public CardStatusPanel getSeenPanel() {
		return seenPanel;
	}

	//Getter for type
	public CardType getType() {
		return type;
	}

	
}
