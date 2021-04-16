package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	private Player cardHolder;
	
	//Default constructor takes a name for the card
	public Card(CardType cardType, String cardName) {
		super();
		this.cardType = cardType;
		this.cardName = cardName;
	}

	//We aren't sure why this method is needed yet. . . but it was in the UML diagram for the assignment so here it is.
	public boolean equals(Card target) {
		//TODO method stub
		return true;
	}

	//Getter for cardType
	public CardType getCardType() {
		return cardType;
	}

	//Getter for cardHolder
	public Player getCardHolder() {
		return cardHolder;
	}

	//Setter for cardHolder
	public void setCardHolder(Player cardHolder) {
		this.cardHolder = cardHolder;
	}

	//Getter for CardName
	public String getCardName() {
		return cardName;
	}

	@Override
	public String toString() {
		return cardName;
	}
	
	
}
