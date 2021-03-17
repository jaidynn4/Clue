package clueGame;

import java.awt.Color;

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

}
