package experiment;

import java.util.*;


public class TestBoard {
	//private Map<TestBoardCell, Set<TestBoardCell>> board;	//The game board
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;						//List of possible cells to move to
	private Set<TestBoardCell> visited;						//List of cells visited in movement path
	final static int COLS = 4;
	final static int ROWS = 4;
	
	
	//Construct a blank 4x4 test board
	public TestBoard() {
		super();
		this.grid = new TestBoardCell[ROWS][COLS];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				TestBoardCell tempCell = new TestBoardCell(i, j, this);
				grid[i][j] = tempCell;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				grid[i][j].getAdjacencies();
			}
		}
		this.targets = new HashSet<TestBoardCell>();
		this.visited = new HashSet<TestBoardCell>();
	}
	
	//Create the set of target cells - blank method stub
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		//TODO algorithm with recursive function called inside to find all target cells
		return;
	}
	
	//Getter for targets
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	//Getter for a specific cell within the board
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

}
