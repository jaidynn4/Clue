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
	private static ArrayList<Card> peopleDeck, roomDeck, weaponDeck;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		
		//People cards (6)
		peopleDeck = new ArrayList<Card>();
		thrawnCard = new Card(CardType.PERSON, "Admiral Thrawn");
		vaderCard = new Card(CardType.PERSON, "Darth Vader");
		tarkinCard = new Card(CardType.PERSON, "Grand Moff Tarkin");
		moffCard = new Card(CardType.PERSON, "Moff Jerjerodd");
		admiralCard = new Card(CardType.PERSON, "Rear Admiral Chiraneau");
		palpatineCard = new Card(CardType.PERSON, "Emperor Palpatine");
		peopleDeck.add(thrawnCard);
		peopleDeck.add(vaderCard);
		peopleDeck.add(tarkinCard);
		peopleDeck.add(moffCard);
		peopleDeck.add(admiralCard);
		peopleDeck.add(palpatineCard);
		
		//Room cards (9)
		roomDeck = new ArrayList<Card>();
		engineCard = new Card(CardType.ROOM, "Engine Room");
		bridgeCard = new Card(CardType.ROOM, "Bridge");
		dockingCard = new Card(CardType.ROOM, "Docking Bay");
		messCard = new Card(CardType.ROOM, "Mess Hall");
		maintenanceCard = new Card(CardType.ROOM, "Maintenance");
		trashCard = new Card(CardType.ROOM, "Trash Compactor");
		armoryCard = new Card(CardType.ROOM, "Armory");
		navCard = new Card(CardType.ROOM, "Navigation");
		quartersCard = new Card(CardType.ROOM, "Captain's Quarters");
		roomDeck.add(engineCard);
		roomDeck.add(bridgeCard);
		roomDeck.add(dockingCard);
		roomDeck.add(messCard);
		roomDeck.add(maintenanceCard);
		roomDeck.add(trashCard);
		roomDeck.add(armoryCard);
		roomDeck.add(navCard);
		roomDeck.add(quartersCard);
		
		
		//Weapon Cards (6)
		weaponDeck = new ArrayList<Card>();
		blasterCard = new Card(CardType.WEAPON, "Blaster");
		saberCard = new Card(CardType.WEAPON, "Lightsaber");
		spannerCard = new Card(CardType.WEAPON, "Hydrospanner");
		fusionCard = new Card(CardType.WEAPON, "Fusion Cutter");
		shockCard = new Card(CardType.WEAPON, "Shock Stick");
		detonatorCard = new Card(CardType.WEAPON, "Thermal Detonator");	
		weaponDeck.add(blasterCard);
		weaponDeck.add(saberCard);
		weaponDeck.add(spannerCard);
		weaponDeck.add(fusionCard);
		weaponDeck.add(shockCard);
		weaponDeck.add(detonatorCard);	
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
