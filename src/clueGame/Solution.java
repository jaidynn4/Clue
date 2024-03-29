package clueGame;

public class Solution {
	private Card person;		//Person card
	private Card room;			//Room card
	private Card weapon;		//Weapon card
	
	//Default constructor takes in 3 cards
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	//Getter for toString returns a sentence
	@Override
	public String toString() {
		return person.getCardName() + " in the " + room.getCardName() + " with the " + weapon.getCardName() + "?";
	}
		
	//Checks if the cards of 2 solutions are equal
	public boolean equals(Solution solution2) {
		if (this.person != solution2.person) {
			return false;
		}
		if (this.room != solution2.room) {
			return false;
		}
		if (this.weapon != solution2.weapon) {
			return false;
		}
		return true;
	}
	
	//Getter for person card
	public Card getPerson() {
		return person;
	}

	//Getter for room card
	public Card getRoom() {
		return room;
	}

	//Getter for weapon card
	public Card getWeapon() {
		return weapon;
	}
		
}
