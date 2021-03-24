package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

public class gameSetupTests {

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;
	private static Card thrawnCard, vaderCard, tarkinCard, moffCard, admiralCard, palpatineCard, 
	engineCard, bridgeCard, dockingCard, messCard, maintenanceCard, trashCard, armoryCard, navCard, quartersCard,
	blasterCard, saberCard, spannerCard, fusionCard, shockCard, detonatorCard;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		//People cards (6)
		thrawnCard = new Card(CardType.PERSON, "Admiral Thrawn");
		vaderCard = new Card(CardType.PERSON, "Darth Vader");
		tarkinCard = new Card(CardType.PERSON, "Grand Moff Tarkin");
		moffCard = new Card(CardType.PERSON, "Moff Jerjerodd");
		admiralCard = new Card(CardType.PERSON, "Rear Admiral Chiraneau");
		palpatineCard = new Card(CardType.PERSON, "Emperor Palpatine");
		//Room cards (9)
		engineCard = new Card(CardType.ROOM, "Engine Room");
		bridgeCard = new Card(CardType.ROOM, "Bridge");
		dockingCard = new Card(CardType.ROOM, "Docking Bay");
		messCard = new Card(CardType.ROOM, "Mess Hall");
		maintenanceCard = new Card(CardType.ROOM, "Maintenance Room");
		trashCard = new Card(CardType.ROOM, "Trash Compactor");
		armoryCard = new Card(CardType.ROOM, "Armory");
		navCard = new Card(CardType.ROOM, "Navigation");
		quartersCard = new Card(CardType.ROOM, "Captain's Quarters");
		//Weapon Cards (6)
		blasterCard = new Card(CardType.WEAPON, "Blaster");
		saberCard = new Card(CardType.WEAPON, "Lightsaber");
		spannerCard = new Card(CardType.WEAPON, "Hydrospanner");
		fusionCard = new Card(CardType.WEAPON, "Fusion Cutter");
		shockCard = new Card(CardType.WEAPON, "Shock Stick");
		detonatorCard = new Card(CardType.WEAPON, "Thermal Detonator");	
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
	
	//Test for accusations
	@Test
	public void testAccusation() {
		board.setTheAnswer(thrawnCard, bridgeCard, blasterCard);
		//check correct answer is returning true
		assertTrue(board.checkAccusation(new Solution(thrawnCard, bridgeCard, blasterCard)));
		//solutions with 1 false entry all return false
		assertTrue(!board.checkAccusation(new Solution(vaderCard, bridgeCard, blasterCard)));
		assertTrue(!board.checkAccusation(new Solution(thrawnCard, engineCard, blasterCard)));
		assertTrue(!board.checkAccusation(new Solution(thrawnCard, bridgeCard, saberCard)));
		//all wrong solution returns false
		assertTrue(!board.checkAccusation(new Solution(palpatineCard, armoryCard, detonatorCard)));
	}
	
	//Test for a Player disproving suggestions
	@Test
	public void testDisproval() {
		Player player = new ComputerPlayer("Playername", Color.black, 1, 1);
		player.updateHand(thrawnCard);
		player.updateHand(armoryCard);
		player.updateHand(detonatorCard);
		
		//Test when a player has multiple cards from the suggestion
		//Must randomly return each card at least once
		Set<Card> seen = new HashSet<Card>();
		for (int i = 0; i < 500; i++) {
			seen.add(player.disproveSuggestion(new Solution(thrawnCard, armoryCard, detonatorCard)));
		}
		assertEquals(seen.size(), 3);
		
		//Tests when a player has one of each card type matching the suggestion
		assertEquals(detonatorCard, player.disproveSuggestion(new Solution(palpatineCard, messCard, detonatorCard)));
		assertEquals(thrawnCard, player.disproveSuggestion(new Solution(thrawnCard, messCard, saberCard)));
		assertEquals(armoryCard, player.disproveSuggestion(new Solution(vaderCard, armoryCard, spannerCard)));
		
		//Test when a player has none of the cards from the suggestion
		assertEquals(null, player.disproveSuggestion(new Solution(vaderCard, navCard, spannerCard)));
	}
	
	
	
}
