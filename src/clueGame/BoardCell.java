package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell {
	private int row;												//Row of this cell on the board
	private int col;												//Column of this cell on the board
	private char initial;											//The character representing the type of room that this cell belongs to
	private char secretPassage;										//The destination of the secret passage, if one exists
	private boolean roomLabel;										//Holds whether this cell is a room label
	private boolean roomCenter;										//Holds whether this cell is a room center
	private DoorDirection doorDirection = DoorDirection.NONE;		//Holds the direction of a door from the enum class DoorDirection
	private Set<BoardCell> adjacencyList;							//Set of cells adjacent to this cell
	boolean isPartOfRoom;											//Holds whether this cell is a room
	boolean isOccupied;												//Holds whether this cell is occupied
	
	//TestBoardCell constructor
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.col = column;
		this.secretPassage = '!';
		this.adjacencyList = new HashSet<BoardCell>();
	}
	
	
	//Create the adjacency list for this cell
	public void getAdjacencies() {
		//Check whether to add the cell to the west
		Board theBoard = Board.getInstance();
		if (this.col > 0) {
			if (getAdjacent(this, theBoard.getCell(row, col - 1), DoorDirection.LEFT)) {
				adjacencyList.add(theBoard.getCell(row, col - 1));
			}
		}
		//Check whether to add the cell to the east
		if (this.col < theBoard.getNumColumns() - 1){
			if( getAdjacent(this, theBoard.getCell(row, col + 1), DoorDirection.RIGHT)) {
				adjacencyList.add(theBoard.getCell(row, col + 1));
			}
		}
		//Check whether to add the cell to the north
		if (this.row > 0) {
			if(getAdjacent(this, theBoard.getCell(row - 1, col), DoorDirection.UP)) {
				adjacencyList.add(theBoard.getCell(row - 1, col));
			}
		}
		//Check whether to add the cell to the south
		if (this.row < theBoard.getNumRows() - 1) {
			if(getAdjacent(this, theBoard.getCell(row + 1, col), DoorDirection.DOWN)) {
				adjacencyList.add(theBoard.getCell(row + 1, col));
			}
		}
		return;
	}
	
	
	
	//A helper method used for getAdjacencies
	public boolean getAdjacent(BoardCell currentCell, BoardCell cellToCheck, DoorDirection door) {
		Board theBoard = Board.getInstance();
		
		//false if unused
		if (cellToCheck.getInitial() == 'X') {
			return false;
		}
		
		//if the cell's door direction matches the specified door direction, link it to the room center
		if (currentCell.doorDirection == door) {
			Room currentRoom = theBoard.getRoom(cellToCheck.getInitial());
			adjacencyList.add(currentRoom.getCenterCell());
			currentRoom.getCenterCell().addAdjacency(currentCell);
			return false;
		}
		
		//if the room is a secret passage indicator, add the secret passage room's center cell to the
		//adjacency list of the current room's center cell
		if (currentCell.secretPassage != '!') {
			Room currentRoom = theBoard.getRoom(currentCell.getInitial());
			Room passageRoom = theBoard.getRoom(currentCell.secretPassage);
			currentRoom.getCenterCell().addAdjacency(passageRoom.getCenterCell());
			return false;
		}
		//false if cell is a room
		if (currentCell.isPartOfRoom()) {
			return false;
		}

		if (cellToCheck.isPartOfRoom()) {
			return false;
		}
		return true;
	}

	//Update the adjacencyList with a cell
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}
		
	//Getter for row
	public int getRow() {
		return row;
	}

	//Getter for column
	public int getColumn() {
		return col;
	}
	
	//Check if the cell is a doorway
	public boolean isDoorway() {
		if(doorDirection == DoorDirection.NONE) {
			return false;
		}
		return true;
	}
	
	//Check if the cell is a label
	public boolean isLabel() {
		return roomLabel;
	}
	
	//Check if the cell is a room center
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	//Check the direction of the door in a cell
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	//Check if the room is a secret passage
	public Character getSecretPassage() {
		return secretPassage;
	}

	//Getter for the initial of this cell
	public char getInitial() {
		return initial;
	}

	//Setter for the initial of this cell
	public void setInitial(char initial) {
		this.initial = initial;
	}

	//Setter for secret passage 
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	//Tests if this cell is a room label
	public boolean isRoomLabel() {
		return roomLabel;
	}

	//Setter for roomLabel
	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	//Setter for roomCenter
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	//Setter for DoorDirection
	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	//Getter for  adjacencyList
	public Set<BoardCell> getAdjList() {
		return adjacencyList;
	}

	//Getter for isRoom
	public boolean isPartOfRoom() {
		return isPartOfRoom;
	}

	//Setter for isRoom
	public void setIsPartOfRoom(boolean isRoom) {
		this.isPartOfRoom = isRoom;
	}

	//Getter for isOccupied
	public boolean isOccupied() {
		return isOccupied;
	}

	//Setter for isOccupied
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
}
