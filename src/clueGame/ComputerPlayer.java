package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	//Default constructor
	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}

	//Add a card to the player's hand
	@Override
	public void updateHand(Card card) {
		hand.add(card);
		updateSeen(card);
	}
	
	//Add a card to the player's seen cards list
	@Override
	public void updateSeen(Card card) {
		seen.add(card);
	}
	
	//Use AI logic to make the computer player generate a suggestion
	public Solution createSuggestion(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck) {
		//Get the room that the player is in
		Room myRoom = Board.getInstance().getRoom(Board.getInstance().getCell(row, column));
		Card playerCard = null;
		Card roomCard = null;
		Card weaponCard = null;
		
		//Get the room card of the room that the player is in
		for (Card card: roomDeck) {
			if (card.getCardName().equals(myRoom.getName())) {
				roomCard = card;
				break;
			}
		}
		
		//Choose a player card
		ArrayList<Card> playerCards = new ArrayList<Card>();
		for (Card card: playerDeck) {
			if (!seen.contains(card) || hand.contains(card)) {
				playerCards.add(card);
			}
		}
		
		//Return a random unseen player card
		Random rand = new Random();
		int randNum = rand.nextInt(playerCards.size());
		playerCard = playerCards.get(randNum);
		
		//Choose a weapon card
		ArrayList<Card> weaponCards = new ArrayList<Card>();
		for (Card card: weaponDeck) {
			if (!seen.contains(card) || hand.contains(card)) {
				weaponCards.add(card);
			}
		}
		//Return a random unseen weapon card
		randNum = rand.nextInt(weaponCards.size());
		weaponCard = weaponCards.get(randNum);
		
		//Return a solution using the three chosen cards
		return new Solution(playerCard, roomCard, weaponCard);
	}
	
	@Override
	public BoardCell findTarget(int pathlength, ArrayList<Card> roomDeck) {
		//grab the board and calculate targets from player location for roll amount
		Board board = Board.getInstance();
		board.calcTargets(board.getCell(row, column), pathlength);
		Set<BoardCell> targets = board.getTargets();
		
		//Add the current cell to the list of targets if the player was just moved to a room outside of their turn
		if(isJustMoved()) {
			targets.add(board.getCell(row, column));
			setJustMoved(false);
		}
		
		//if targets is empty, stay in the current location
		if(targets.size() == 0) {
			return board.getCell(row, column);
		}
		
		//Make a new array for choices AI can make and populate it with unseen rooms in range
		ArrayList<BoardCell> choices = new ArrayList<BoardCell>();
		for (BoardCell cell: targets) {
			if(cell.isRoomCenter() && cell != lastRoomVisited.getCenterCell()) {
				for(Card card: roomDeck) {
					if(card.getCardName().equals(board.getRoom(cell).getName()) && !seen.contains(card)) {
						choices.add(cell);
					}
				}
			}
		}
		
		//if no seen rooms are in range, populate choices array with all targets around
		if(choices.size() == 0) {
			for(BoardCell target: targets) {
				choices.add(target);
			}
		}
		//return random from choices
		Random rand = new Random();
		int randNum = rand.nextInt(choices.size());
		return choices.get(randNum);
	}
	
	public Solution canAccuse(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck) {
		ArrayList<Card> unseenPlayers = new ArrayList<Card>();
		ArrayList<Card> unseenRooms = new ArrayList<Card>();
		ArrayList<Card> unseenWeapons = new ArrayList<Card>();
		
		for(Card card: playerDeck) {
			if(!seen.contains(card)) {
				unseenPlayers.add(card);
			}
		}
		for(Card card: roomDeck) {
			if(!seen.contains(card)) {
				unseenRooms.add(card);
			}
		}
		for(Card card: weaponDeck) {
			if(!seen.contains(card)) {
				unseenWeapons.add(card);
			}
		}
		
		int totalNumberOfCardsUnseen = unseenPlayers.size() + unseenRooms.size() + unseenWeapons.size();
		if(totalNumberOfCardsUnseen == 3) {
			return new Solution(unseenPlayers.get(0), unseenRooms.get(0), unseenWeapons.get(0));
		}
		
		return null;
	}

}
