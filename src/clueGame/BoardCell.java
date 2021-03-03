package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell {
	private int row;							//Row of this cell on the board
	private int col;							//Column of this cell on the board
	private char initial;						//The character representing the type of room that this cell belongs to
	private char secretPassage;					//The destination of the secret passage, if one exists
	private boolean roomLabel;					//Holds whether this cell is a room label
	private boolean roomCenter;					//Holds whether this cell is a room center
	private DoorDirection doorDirection;		//Holds the direction of a door from the enum class DoorDirection
	private Set<BoardCell> adjacencyList;		//Set of cells adjacent to this cell
	boolean isRoom;								//Holds whether this cell is a room
	boolean isOccupied;							//Holds whether this cell is occupied
	
	//TestBoardCell constructor
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.col = column;
		this.adjacencyList = new HashSet<BoardCell>();
	}
	
	//Update the adjacencyList with a cell
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}
	
	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public boolean isRoomLabel() {
		return roomLabel;
	}

	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	//Getter for  adjacencyList
	public Set<BoardCell> getAdjList() {
		return adjacencyList;
	}

	//Getter for isRoom
	public boolean isRoom() {
		return isRoom;
	}

	//Setter for isRoom
	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	//Getter for isOccupied
	public boolean isOccupied() {
		return isOccupied;
	}

	//Setter for isOccupied
	public void setIsOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
	
	//Create the adjacency list for this cell
	public void getAdjacencies() {
		//Check whether to add the cell to the west
		if (this.col > 0) {
			adjacencyList.add(Board.getInstance().getCell(this.row, this.col - 1));
		}
		//Check whether to add the cell to the east
		if (this.col < Board.getInstance().getNumColumns() - 1) {
			adjacencyList.add(Board.getInstance().getCell(this.row, this.col + 1));
		}
		//Check whether to add the cell to the north
		if (this.row > 0) {
			adjacencyList.add(Board.getInstance().getCell(this.row - 1, this.col));
		}
		//Check whether to add the cell to the south
		if (this.row < Board.getInstance().getNumRows() - 1) {
			adjacencyList.add(Board.getInstance().getCell(this.row + 1, this.col));
		}
		return;
	}

	//Getter for row
	public int getRow() {
		return row;
	}

	//Getter for column
	public int getColumn() {
		return col;
	}
	
	//Add an additional adjacent space - used for secret passage linking
	public void addAdj (BoardCell adj) {
		//TODO method stub
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
	
}
