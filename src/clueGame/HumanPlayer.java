package clueGame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

public class HumanPlayer extends Player {

	//Default constructor
	public HumanPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}

	//Add a card to the player's hand
	@Override
	public void updateHand(Card card) {
		hand.add(card);
	}

	//Add a card to the player's seen cards list
	@Override
	public void updateSeen(Card card) {
		seen.add(card);
	}

	
//Unused stubs to satisfy abstract method, this is only truly used by computer player
	@Override
	public BoardCell findTarget(int pathlength, ArrayList<Card> roomDeck) {
		return null;
	}

	@Override
	public Solution createSuggestion(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck) {
		return null;
	}
	
	@Override
	public Solution canAccuse(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck) {
		return null;
	}
	
}
