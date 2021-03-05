package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	@Test
	public void testAdjacenciesRooms() {
		//Starting with Navigation, as it has 1 door and a secret passage
		Set<BoardCell> testList = board.getAdjList(2, 22);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(20, 1)));
		assertTrue(testList.contains(board.getCell(5, 23)));
		
		//Now test Engine Room which has 3 doors, but no secret passage
		testList = board.getAdjList(30, 9);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(29, 18)));
		assertTrue(testList.contains(board.getCell(24, 0)));
		assertTrue(testList.contains(board.getCell(29, 11)));
		
		//One more room, the Docking Bay
		testList = board.getAdjList(16, 20);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 17)));
		assertTrue(testList.contains(board.getCell(21, 17)));
		assertTrue(testList.contains(board.getCell(25, 24)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	@Test
	public void testAdjacencyDoor() {
		//Check the door at the right of the Mess Hall
		Set<BoardCell> testList = board.getAdjList(11, 7);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(10, 7)));
		assertTrue(testList.contains(board.getCell(12, 7)));
		assertTrue(testList.contains(board.getCell(11, 8)));
		assertTrue(testList.contains(board.getCell(11, 2)));

		//Check the door at the bottom of Docking Bay
		testList = board.getAdjList(25, 24);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(26, 24)));
		assertTrue(testList.contains(board.getCell(25, 23)));
		assertTrue(testList.contains(board.getCell(16, 20)));
		
		//Check the door to Navigation
		testList = board.getAdjList(5, 23);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(2, 22)));
		assertTrue(testList.contains(board.getCell(5, 22)));
		assertTrue(testList.contains(board.getCell(4, 23)));
		assertTrue(testList.contains(board.getCell(6, 23)));
	}
	
	// Test a variety of walkway scenarios
	@Test
	public void testAdjacencyWalkways() {
		//Test on right edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(6, 24);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(6, 23)));
		
		//Test near a door but not adjacent
		testList = board.getAdjList(18, 2);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(18, 1)));
		assertTrue(testList.contains(board.getCell(17, 2)));

		//Test adjacent to walkways and a door
		testList = board.getAdjList(23, 3);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(23, 2)));
		assertTrue(testList.contains(board.getCell(23, 4)));
		assertTrue(testList.contains(board.getCell(22, 3)));
		assertTrue(testList.contains(board.getCell(24, 3)));

		//Test next to unused
		testList = board.getAdjList(13,13);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(13, 12)));
		assertTrue(testList.contains(board.getCell(13, 14)));
		assertTrue(testList.contains(board.getCell(12, 13)));
	}
	
	
	//Tests for targets out of room centers, rolling 1, 3 and 4
	@Test
	public void testTargetsInArmory() {
		// test a roll of 1
		board.calcTargets(board.getCell(20, 4), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(22, 3)));
		assertTrue(targets.contains(board.getCell(17, 3)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(20, 4), 3);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(17, 1)));
		assertTrue(targets.contains(board.getCell(18, 2)));	
		assertTrue(targets.contains(board.getCell(24, 3)));
		assertTrue(targets.contains(board.getCell(23, 4)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(20, 4), 4);
		targets= board.getTargets();
		assertEquals(20, targets.size());
		assertTrue(targets.contains(board.getCell(24, 4)));
		assertTrue(targets.contains(board.getCell(23, 5)));	
		assertTrue(targets.contains(board.getCell(18, 1)));
		assertTrue(targets.contains(board.getCell(16, 3)));	
	}
	
	@Test
	public void testTargetsInCaptainsQuarters() {
		// test a roll of 1
		board.calcTargets(board.getCell(1, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 5)));
		assertTrue(targets.contains(board.getCell(31, 24)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(1, 2), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(1, 5)));
		assertTrue(targets.contains(board.getCell(4, 4)));	
		assertTrue(targets.contains(board.getCell(4, 6)));
		assertTrue(targets.contains(board.getCell(31, 24)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(1, 2), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(0, 5)));	
		assertTrue(targets.contains(board.getCell(3, 6)));
		assertTrue(targets.contains(board.getCell(31, 24)));	
	}

	//Tests for targets out of walkways, rolling 1, 3 and 4
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(10, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(16, 20)));
		assertTrue(targets.contains(board.getCell(10, 16)));	
		assertTrue(targets.contains(board.getCell(9, 17)));	
			
		
		// test a roll of 3
		board.calcTargets(board.getCell(10, 17), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(16, 20)));
		assertTrue(targets.contains(board.getCell(7, 17)));
		assertTrue(targets.contains(board.getCell(13, 15)));	
		assertTrue(targets.contains(board.getCell(9, 17)));
		assertTrue(targets.contains(board.getCell(11, 15)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(10, 17), 4);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(16, 20)));
		assertTrue(targets.contains(board.getCell(11, 14)));
		assertTrue(targets.contains(board.getCell(8, 17)));	
		assertTrue(targets.contains(board.getCell(7, 16)));
		assertTrue(targets.contains(board.getCell(10, 13)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(31, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(31, 19)));
		assertTrue(targets.contains(board.getCell(30, 18)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(31, 18), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(30, 9)));
		assertTrue(targets.contains(board.getCell(29, 22)));
		assertTrue(targets.contains(board.getCell(30, 18)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(31, 18), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(30, 9)));
		assertTrue(targets.contains(board.getCell(29, 22)));
		assertTrue(targets.contains(board.getCell(28, 17)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(22, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(21, 6)));
		assertTrue(targets.contains(board.getCell(22, 7)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(22, 6), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(22, 3)));
		assertTrue(targets.contains(board.getCell(21, 8)));
		assertTrue(targets.contains(board.getCell(22, 7)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(22, 6), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(20, 4)));
		assertTrue(targets.contains(board.getCell(26, 6)));
		assertTrue(targets.contains(board.getCell(23, 5)));	
	}

	@Test
	//Test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 above
		board.getCell(24, 15).setOccupied(true);
		board.calcTargets(board.getCell(26, 15), 4);
		board.getCell(24, 15).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(29, 14)));
		assertTrue(targets.contains(board.getCell(25, 16)));
		assertTrue(targets.contains(board.getCell(26, 19)));	
		assertFalse( targets.contains( board.getCell(24, 15))) ;
		assertFalse( targets.contains( board.getCell(22, 15))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(26, 11).setOccupied(true);
		board.getCell(23, 12).setOccupied(true);
		board.calcTargets(board.getCell(23, 11), 1);
		board.getCell(26, 11).setOccupied(false);
		board.getCell(23, 12).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(22, 11)));	
		assertTrue(targets.contains(board.getCell(23, 10)));	
		assertTrue(targets.contains(board.getCell(26, 11)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(5, 1).setOccupied(true);
		board.calcTargets(board.getCell(11, 2), 3);
		board.getCell(5, 1).setOccupied(false);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(13, 7)));
		assertTrue(targets.contains(board.getCell(12, 8)));	
		assertFalse(targets.contains(board.getCell(5, 3)));

	}
}
