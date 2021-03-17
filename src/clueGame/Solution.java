package clueGame;

public class Solution {
	private Card person;
	private Card room;
	private Card weapon;
	
	//Default constructor takes in 3 cards
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
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
