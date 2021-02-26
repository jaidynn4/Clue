package tests;

import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import experiment.*;


public class BoardTestsExp {
	TestBoard boardToTest;
	
	@BeforeEach
	public void setUp() {
		boardToTest = new TestBoard();
		
	}
	
	
	//Starting top left, checks if adjacent cells is correct
	@Test
	public void testTopLeftAdjacency() {
		TestBoardCell cell = boardToTest.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(boardToTest.getCell(1, 0)));
		Assert.assertTrue(testList.contains(boardToTest.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
	}
	
	//starting top left, checks if targets for a 3 roll is correct
	@Test
	public void testTargetsNormalThreeSteps() {
		TestBoardCell cell = boardToTest.getCell(0, 0);
		boardToTest.calcTargets(cell,  3);
		Set<TestBoardCell> targets = boardToTest.getTargets();
		Assert.assertEquals(6,  targets.size());
		Assert.assertTrue(targets.contains(boardToTest.getCell(3, 0)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(2, 1)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(0, 1)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(1, 2)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(0, 3)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(1, 0)));
	}
	
	//Starting bottom left, checks targets correct with an occupied spot nearby and a room nearby
	@Test
	public void testTargetsMixed() {
		boardToTest.getCell(0, 2).setIsOccupied(true);
		boardToTest.getCell(2, 2).setIsRoom(true);
		TestBoardCell cell = boardToTest.getCell(0, 3);
		boardToTest.calcTargets(cell, 3);
		Set<TestBoardCell> targets = boardToTest.getTargets();
		Assert.assertEquals(4,  targets.size());
		Assert.assertTrue(targets.contains(boardToTest.getCell(0, 0)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(1, 1)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(3, 3)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(1, 3)));
		
	}
	
	//starting top left, checks if targets correct for a roll of 1
	@Test
	public void testTargetsNormalOneStep() {
		TestBoardCell cell = boardToTest.getCell(0, 0);
		boardToTest.calcTargets(cell,  1);
		Set<TestBoardCell> targets = boardToTest.getTargets();
		Assert.assertEquals(2,  targets.size());
		Assert.assertTrue(targets.contains(boardToTest.getCell(0, 1)));
		Assert.assertTrue(targets.contains(boardToTest.getCell(1, 0)));
	}
	
	//starting top left, checks if targets correct for a roll of 1
		@Test
		public void testTargetsFourStepNormal() {
			TestBoardCell cell = boardToTest.getCell(1, 1);
			boardToTest.calcTargets(cell,  4);
			Set<TestBoardCell> targets = boardToTest.getTargets();
			Assert.assertEquals(7,  targets.size());
			Assert.assertTrue(targets.contains(boardToTest.getCell(0, 0)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(2, 0)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(3, 1)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(0, 2)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(2, 2)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(1, 3)));
			Assert.assertTrue(targets.contains(boardToTest.getCell(3, 3)));
		}
	
	//starting bottom right, makes sure adjacent cells are noted properly.
	@Test
	public void testBottomRightAdjacency() {
		TestBoardCell cell = boardToTest.getCell(3, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(boardToTest.getCell(3, 2)));
		Assert.assertTrue(testList.contains(boardToTest.getCell(2, 3)));
		Assert.assertEquals(2, testList.size());
	}
	

	//starting central at 1,1 , makes sure adjacent cells are noted properly.
	@Test
	public void testMiddleAdjacency() {
		TestBoardCell cell = boardToTest.getCell(1, 1);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(boardToTest.getCell(0, 1)));
		Assert.assertTrue(testList.contains(boardToTest.getCell(1, 0)));
		Assert.assertTrue(testList.contains(boardToTest.getCell(2, 1)));
		Assert.assertTrue(testList.contains(boardToTest.getCell(1, 2)));
		Assert.assertEquals(4, testList.size());
	}
}
