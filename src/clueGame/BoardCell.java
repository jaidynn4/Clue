package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell {
	private int row;							//Row of this cell on the board
	private int col;							//Column of this cell on the board
	private char initial;
	private char secretPassage;
	private boolean roomLabel;
	private boolean roomCenter;
	private DoorDirection doorDirection;
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
	
	public void addAdj (BoardCell adj) {
		//TODO method stub
	}
}
