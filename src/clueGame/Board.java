package clueGame;

import java.util.*;

public class Board {
	private BoardCell[][] grid;			//The game board
	private Set<BoardCell> targets;		//List of possible cells to move to
	private Set<BoardCell> visited;		//List of cells visited in movement path
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance;
	
	
	private Board() {
		super();
	}
	
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
	}
	
	//Create the set of target cells - blank method stub
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		//recursive call for going through roll size
		findAllTargets(startCell, pathlength);
	}
	
	private void findAllTargets(BoardCell startCell, int pathlength) {
		for(BoardCell cell: startCell.getAdjList()) {
			if(!visited.contains(cell)) {
				visited.add(cell);
				if(pathlength == 1) {
					//final check for if the cell is occupied or a room before adding it to the list
					if(!cell.isOccupied && !cell.isRoom) {
						targets.add(cell);
					}
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

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

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
}
