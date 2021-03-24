package clueGame;

import java.awt.Color;
import java.util.*;

public abstract class Player {
	
	private String name;			//The player character's name
	private Color color;			//The color used to represent the player
	protected int row;				//The current row number that the player is on
	protected int column;			//The current column number that the player is on
	protected Set<Card> hand;		//The player's hand of cards
	protected Set<Card> seen;		//Cards that the player has seen

	//Default constructor takes in name, color, row, and column
	public Player(String name, Color color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		hand = new HashSet<Card>();
		seen = new HashSet<Card>();
	}
	
	//Abstract method used to update the player's hand
	public abstract void updateHand(Card card);
	
	//Abstract method used to update the player's seen cards
	public abstract void updateSeen(Card card);
		
	
	//Getter for hand
	public Set<Card> getHand() {
		return hand;
	}

	public Card disproveSuggestion(Solution suggestion) {
		Card[] cards = new Card[3];
		cards[0] = suggestion.getPerson();
		cards[1] = suggestion.getRoom();
		cards[2] = suggestion.getWeapon();
		
		ArrayList<Card> matches = new ArrayList<Card>();
		int matchIndex = 0;
		
		for (Card card: hand) {
			for (int i = 0; i < 3; i++) {
				if (card == cards[i]) {
					matches.add(cards[i]);
					matchIndex++;
				}
			}
		}
		if (matches.size() == 0) {
			return null;
		}
		Random rand = new Random();
		int randNum = rand.nextInt(matches.size());
		return matches.get(randNum);
	}
	
	//Getter for name
	public String getName() {
		return name;
	}

	//Setter for name
	public void setName(String name) {
		this.name = name;
	}

	//Getter for color
	public Color getColor() {
		return color;
	}

	//Setter for color
	public void setColor(Color color) {
		this.color = color;
	}

	//Getter for row
	public int getRow() {
		return row;
	}

	//Setter for row
	public void setRow(int row) {
		this.row = row;
	}

	//Getter for column
	public int getColumn() {
		return column;
	}

	//Setter for column
	public void setColumn(int column) {
		this.column = column;
	}

}
