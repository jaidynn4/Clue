package experiment;

import java.util.*;


public class TestBoard {
	private Map<TestBoardCell, Set<TestBoardCell>> board;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;

	public TestBoard() {
		//TODO set up a board for testing
		this.board = new HashMap<TestBoardCell, Set<TestBoardCell>>();
		
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		//TODO algorithm with recursive function called inside to find all target cells
		return;
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col) {
		//TODO uncomment loop below to allow search of cells in a board, check if valid?
		TestBoardCell toReturn = null;
//		for(Map.Entry<TestBoardCell, Set<TestBoardCell>> cell: board.entrySet()) {
//			if(cell.getKey().getRow() == row && cell.getKey().getColumn() == col) {
//				toReturn = cell.getKey();
//			}
//		}
		return toReturn;
	}

}
