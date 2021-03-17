package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

public class gameSetupTests {

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	//Test players created properly
	@Test
	public void testPlayers() {
		//Test that we have the right number of players
		assertEquals(board.getPlayerMap().size(), 6);
		
		//Test that there are a human player and a computer player created
		assertTrue(board.getPlayer(Color.white) instanceof HumanPlayer);
		assertTrue(board.getPlayer(Color.blue) instanceof ComputerPlayer);
		
		//Check start locations are proper
		Color checkColor = Color.white;
		BoardCell playerCell = board.getCell(board.getPlayer(checkColor).getRow(), board.getPlayer(checkColor).getColumn());
		assertEquals(board.getCell(0,5), playerCell);
		checkColor = Color.gray;
		playerCell = board.getCell(board.getPlayer(checkColor).getRow(), board.getPlayer(checkColor).getColumn());
		assertEquals(board.getCell(0,19), playerCell);
		checkColor = Color.blue;
		playerCell = board.getCell(board.getPlayer(checkColor).getRow(), board.getPlayer(checkColor).getColumn());
		assertEquals(board.getCell(6,24), playerCell);
	}
	
	//Test that proper amount of cards are made
	@Test
	public void testCards() {
		//Test that each deck has the right size
		//Note that theDeck will initially contain all 21 cards but will be depleted as cards are dealt
		assertEquals(board.getTheDeck().size(), 0);
		assertEquals(board.getPlayerDeck().size(), 6);
		assertEquals(board.getWeaponDeck().size(), 6);
		assertEquals(board.getRoomDeck().size(), 9);
		
		//Makes sure the solution/answer has the proper types of cards set up
		assertEquals(board.getTheAnswer().getPerson().getCardType(), CardType.PERSON);
		assertEquals(board.getTheAnswer().getWeapon().getCardType(), CardType.WEAPON);
		assertEquals(board.getTheAnswer().getRoom().getCardType(), CardType.ROOM);
		
		ArrayList<Card> allPlayerCards = new ArrayList<Card>();
		
		//Test that 3 cards were dealt to each player
		for(Player player: board.getPlayerMap().values()) {
			assertEquals(player.getHand().size(), 3);
			
			//For each card that each player has, ensure that it is not equal to cards in the solution/answer
			for(Card card: player.getHand()) {
				assertTrue(card != board.getTheAnswer().getPerson());
				assertTrue(card != board.getTheAnswer().getWeapon());
				assertTrue(card != board.getTheAnswer().getRoom());
				
				//The current card should not be a card that has already been dealt to any player
				assertTrue(!allPlayerCards.contains(card));
				allPlayerCards.add(card);
			}
		}
		
		
	}
}
