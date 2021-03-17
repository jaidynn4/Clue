package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	//Default constructor takes a name for the card
	public Card(CardType cardType, String cardName) {
		super();
		this.cardType = cardType;
		this.cardName = cardName;
	}

	//
	public boolean equals(Card target) {
		//TODO method stub
		return true;
	}

	//Getter for cardType
	public CardType getCardType() {
		return cardType;
	}

	//Getter for CardName
	public String getCardName() {
		return cardName;
	}
}
