package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public abstract class Player {
	protected String name;						//The player character's name
	protected Color color;						//The color used to represent the player
	protected int row;							//The current row number that the player is on
	protected int column;						//The current column number that the player is on
	protected Set<Card> hand;					//The player's hand of cards
	protected Set<Card> seen;					//Cards that the player has seen
	protected boolean wasPulledToRoom = false;	//Tracks if the player was moved to a room since their last turn
	protected Room lastRoomVisited;				//Tracks the last room that the player made a suggestion in so they will opt to move elsewhere
	protected boolean hasMovedThisTurn = false;	//Tracks if the player has moved yet on their turn
	
	//Default constructor takes in name, color, row, and column
	public Player(String name, Color color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		hand = new HashSet<Card>();
		seen = new HashSet<Card>();
		
		//Instantiate a dummy value for the last room visited that will never equal any of the room cells on the board
		//This will be overwritten and used later to improve computer movement AI logic
		this.lastRoomVisited = new Room("Null", true);
		this.lastRoomVisited.setCenterCell(new BoardCell(-1, -1));
	}
	
	
	//Checks if this player can disprove the current suggestion
	public Card disproveSuggestion(Solution suggestion) {
		Card[] cards = new Card[3];
		cards[0] = suggestion.getPerson();
		cards[1] = suggestion.getRoom();
		cards[2] = suggestion.getWeapon();
		
		ArrayList<Card> matches = new ArrayList<Card>();
		int matchIndex = 0;
		
		//Check each card in the player's hand for matches to the cards in the suggestion and add each matching card to an ArrayList
		for (Card card: hand) {
			for (int i = 0; i < 3; i++) {
				if (card == cards[i]) {
					matches.add(cards[i]);
					matchIndex++;
				}
			}
		}
		
		//If no matches return null
		if (matches.size() == 0) {
			return null;
		}
		
		//Otherwise return a random card from the ArrayList of matching cards
		Random rand = new Random();
		int randNum = rand.nextInt(matches.size());
		return matches.get(randNum);
	}
	
	
	//Draw the oval/circle for the player in the cell. Uses same params as draw for cell, so location is exact.
	public void draw(Graphics g, int cellWidth, int cellHeight, int offset) {
		int numOffset = 1;
		
		if(Board.getInstance().getCell(row, column).isRoomCenter()) {
			numOffset = Board.getInstance().getPlayerList().lastIndexOf(this);;
		}
		
		//Fill first so border goes over top
		g.setColor(color);
		g.fillOval(cellWidth*column+(offset*numOffset), cellHeight*row+offset, cellWidth, cellHeight);
		g.setColor(Color.BLACK);
		g.drawOval(cellWidth*column+(offset*numOffset), cellHeight*row+offset, cellWidth, cellHeight);
	}
	
	
	//Move the player to a new location on the board
	public void move(int row, int column) {
		//Mark the change in which cell is occupied
		Board.getInstance().getCell(this.row, this.column).setOccupied(false);
		setRow(row);
		setColumn(column);
		Board.getInstance().getCell(this.row, this.column).setOccupied(true);
		
		//If the player is human and moved into a room on their turn, display the suggestion menu
		if(this instanceof HumanPlayer && !wasPulledToRoom) {
			if (Board.getInstance().getCell(row, column).isRoomCenter()) {
				SuggestionOptionsFrame sFrame = new SuggestionOptionsFrame(Board.getInstance(), GameActionType.SUGGESTION);
			}
		}
	}
	
	//Abstract method used to update the player's hand
	public abstract void updateHand(Card card);
	
	//Abstract method used to update the player's seen cards
	public abstract void updateSeen(Card card);
	
	//Abstract method used to get the target cell for player movement
	public abstract BoardCell findTarget(int pathlength, ArrayList<Card> roomDeck);
	
	//Abstract method used to determine if the player can make an accusation
	public abstract Solution canAccuse(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck);
	
	//Abstract method used to create a computer player suggestion
	public abstract Solution createSuggestion(ArrayList<Card> playerDeck, ArrayList<Card> roomDeck, ArrayList<Card> weaponDeck);
	
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

	//Getter for wasPulledToRoom
	public boolean isPulledToRoom() {
		return wasPulledToRoom;
	}

	//Setter for wasPulledToRoom
	public void setPulledToRoom(boolean wasPulledToRoom) {
		this.wasPulledToRoom = wasPulledToRoom;
	}

	//Setter for lastRoomVisited
	public void setLastRoomVisited(Room lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

	//Getter for hasMovedThisTurn
	public boolean isHasMovedThisTurn() {
		return hasMovedThisTurn;
	}

	//Setter for hasMovedThisTurn
	public void setHasMovedThisTurn(boolean hasMovedThisTurn) {
		this.hasMovedThisTurn = hasMovedThisTurn;
	}
	
	//Getter for hand
	public Set<Card> getHand() {
		return hand;
	}
	
	//getter for seen
	public Set<Card> getSeen() {
		return seen;
	}
}
