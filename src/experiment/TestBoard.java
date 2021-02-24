package experiment;

import java.util.*;


public class TestBoard {
	private Map<TestBoardCell, Set<TestBoardCell>> board;	//The game board
	private Set<TestBoardCell> targets;						//List of possible cells to move to
	private Set<TestBoardCell> visited;						//List of cells visited in movement path

	//Construct a blank 4x4 test board
	public TestBoard() {
		super();
		this.board = new HashMap<TestBoardCell, Set<TestBoardCell>>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				TestBoardCell tempCell = new TestBoardCell(i, j);
				this.board.put(tempCell, tempCell.getAdjList());
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
		TestBoardCell toReturn = null;
		for(Map.Entry<TestBoardCell, Set<TestBoardCell>> cell: board.entrySet()) {
			if(cell.getKey().getRow() == row && cell.getKey().getColumn() == col) {
				toReturn = cell.getKey();
			}
		}
		return toReturn;
	}

}
