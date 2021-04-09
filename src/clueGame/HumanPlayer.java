package clueGame;

import java.awt.Color;
import java.util.ArrayList;

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
	//TODO add more functionality
	@Override
	public void updateSeen(Card card) {
		seen.add(card);
	}
	
	@Override
	public BoardCell findTarget(int pathlength, ArrayList<Card> roomDeck) {
		//TODO method stub
		return null;
	}
}
