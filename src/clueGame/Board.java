package clueGame;

import java.util.*;

public class Board {
	private BoardCell[][] grid;				//The game board
	private Set<BoardCell> targets;			//List of possible cells to move to
	private Set<BoardCell> visited;			//List of cells visited in movement path
	private int numRows;					//The total number of rows in the game board
	private int numColumns;					//The total number of columns in the game board
	private String layoutConfigFile;		//Stores the filename of the layoutConfigFile
	private String setupConfigFile;			//Stores the filename of the setupConfigFile
	private Map<Character, Room> roomMap;	//The map of all rooms that exist on the board
	private static Board theInstance;		//The Singleton Pattern instance of the board class
	
	//Board constructor
	private Board() {
		super();
	}
	
	//Return the Singleton Pattern instance of the game board and create it if it does not already exist
	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}
	
	//Initialize the board
	public void initialize() {
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		
		//stub board made to make the tests fail without error
		grid = new BoardCell[32][25];
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 25; j++) {
				BoardCell temp = new BoardCell(i,j);
				grid[i][j] = temp;
			}
		}
	}
	
	//Create the set of target cells - blank method stub
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		//recursive call for going through roll size
		findAllTargets(startCell, pathlength);
	}
	
	//Recursive function called by calcTargets
	private void findAllTargets(BoardCell startCell, int pathlength) {
		for(BoardCell cell: startCell.getAdjList()) {
			if(!visited.contains(cell)) {
				//do not enter cell or pass through if occupied
				if(cell.isOccupied) {
					continue;
				}
				visited.add(cell);
				if(pathlength == 1 || cell.isRoom()) {
					//if at a room or end of roll length, add to targets
						targets.add(cell);
				} else {
					//keep going until roll number is met
					findAllTargets(cell, pathlength - 1);
				}
				//clear visited spot for next run
				visited.remove(cell);
			}
		}
	}
	
	//Getter for targets
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	//Getter for a specific cell within the board
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	//Getter for the number of rows
	public int getNumRows() {
		return numRows;
	}

	//Setter for the number of rows
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	//Getter for the number of columns
	public int getNumColumns() {
		return numColumns;
	}

	//Setter for the number of columns
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public void loadConfigFiles() {
		//TODO method stub
	}
	
	public void loadSetupConfig() {
		//TODO method stub
	}
	
	public void loadLayoutConfig() {
		//TODO method stub
	}
	
	public void setConfigFiles(String layout, String setup) {
		//TODO method stub
	}
	
	//Getter for the room character
	public Room getRoom(Character icon) {
		//TODO method stub
		return new Room();
	}
	
	//Getter for the full room object
	public Room getRoom(BoardCell cell) {
		//TODO method stub
		return new Room();
	}
}
