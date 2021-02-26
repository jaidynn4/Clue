package experiment;

import java.util.*;


public class TestBoard {
	private TestBoardCell[][] grid;			//The game board
	private Set<TestBoardCell> targets;		//List of possible cells to move to
	private Set<TestBoardCell> visited;		//List of cells visited in movement path
	final static int COLS = 4; 				// sets max width for test board
	final static int ROWS = 4; 				// Sets max height for test board
	
	
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
		visited.clear();
		targets.clear();
		visited.add(startCell);
		//recursive call for going through roll size
		findAllTargets(startCell, pathlength);
	}
	
	private void findAllTargets(TestBoardCell startCell, int pathlength) {
		for(TestBoardCell cell: startCell.getAdjList()) {
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
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	//Getter for a specific cell within the board
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

}
