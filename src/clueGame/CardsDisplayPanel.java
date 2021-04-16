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
	
	public void populateHand(Player player) {
		for(Card card: player.getHand()) {
			switch(card.getCardType()) {
				case PERSON:
					peoplePanel.getHandPanel().updatePanel(card.getCardName(), Color.white);
					break;
				case ROOM:
					roomsPanel.getHandPanel().updatePanel(card.getCardName(), Color.white);
					break;
				case WEAPON:
					weaponsPanel.getHandPanel().updatePanel(card.getCardName(), Color.white);
					break;
			}
		}
	}
	
	public void updateSeen(Card card) {
		switch(card.getCardType()) {
		case PERSON:
			peoplePanel.getSeenPanel().updatePanel(card.getCardName(), card.getCardHolder().getColor());
			break;
		case ROOM:
			roomsPanel.getSeenPanel().updatePanel(card.getCardName(), card.getCardHolder().getColor());
			break;
		case WEAPON:
			weaponsPanel.getSeenPanel().updatePanel(card.getCardName(), card.getCardHolder().getColor());
			break;
		}
	}
	
	public void updateHumanSeenLists(Player player) {
		peoplePanel.getSeenPanel().updateHuman(player, CardType.PERSON);
		roomsPanel.getSeenPanel().updateHuman(player, CardType.ROOM);
		weaponsPanel.getSeenPanel().updateHuman(player, CardType.WEAPON);
		updateUI();
	}
	
}
