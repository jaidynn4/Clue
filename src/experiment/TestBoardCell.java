package experiment;

import java.util.*;

public class TestBoardCell {
	private int row;							//Row of this cell on the board
	private int column;							//Column of this cell on the board
	private Set<TestBoardCell> adjacencyList;	//Set of cells adjacent to this cell
	boolean isRoom;								//Holds whether this cell is a room
	boolean occupied;							//Holds whether this cell is occupied
	
	//TestBoardCell constructor
	public TestBoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
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
		//TODO update the set of adjacencies with this method
		return;
	}

	//Getter for row
	public int getRow() {
		return row;
	}

	//Getter for column
	public int getColumn() {
		return column;
	}
	
	
}