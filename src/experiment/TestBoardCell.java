package experiment;

import java.util.*;

public class TestBoardCell {
	private int row;							//Row of this cell on the board
	private int col;							//Column of this cell on the board
	private Set<TestBoardCell> adjacencyList;	//Set of cells adjacent to this cell
	boolean isRoom;								//Holds whether this cell is a room
	boolean occupied;							//Holds whether this cell is occupied
	private TestBoard board;
	
	//TestBoardCell constructor
	public TestBoardCell(int row, int column, TestBoard board) {
		super();
		this.row = row;
		this.col = column;
		this.board = board;
		this.adjacencyList = new HashSet<TestBoardCell>();
	}
	
	//Update the adjacencyList with a cell
	public void addAdjacency(TestBoardCell cell) {
		adjacencyList.add(cell);
	}
	
	//Getter for  adjacencyList
	public Set<TestBoardCell> getAdjList() {
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
		return occupied;
	}

	//Setter for isOccupied
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	//Create the adjacency list for this cell - a blank method stub
	public void getAdjacencies() {
		//West
		if (this.col > 0) {
			adjacencyList.add(board.getCell(this.row, this.col - 1));
		}
		//East
		if (this.col < TestBoard.COLS - 1) {
			adjacencyList.add(board.getCell(this.row, this.col + 1));
		}
		//North
		if (this.row > 0) {
			adjacencyList.add(board.getCell(this.row - 1, this.col));
		}
		//South
		if (this.row < TestBoard.ROWS - 1) {
			adjacencyList.add(board.getCell(this.row + 1, this.col));
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
	
	
}