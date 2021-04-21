package clueGame;

public class Card {
	private String cardName;		//The name of the card
	private CardType cardType;		//Enum for the type of card: person, room, or weapon
	private Player cardHolder;		//The player whose hand contains this card
	
	//Default constructor takes a name for the card
	public Card(CardType cardType, String cardName) {
		super();
		this.cardType = cardType;
		this.cardName = cardName;
	}

	//A toString function returns the name of the card
	@Override
	public String toString() {
		return cardName;
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

}
