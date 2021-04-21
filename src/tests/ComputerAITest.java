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

public class ComputerAITest {

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
		board.setConfigFiles("ClueLayoutExecutor.csv", "ClueSetupExecutor.txt");
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
		quartersCard = new Card(CardType.ROOM, "Quarters");
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
	
	@Test
	public void testSuggestionCreation() {
		ComputerPlayer cpuPlayer = new ComputerPlayer("K2-SO", Color.black, 3, 12);
		//set player hand to every card except 1 of each type
		for (int i = 1; i < peopleDeck.size(); i++) {
			cpuPlayer.updateSeen(peopleDeck.get(i));
			cpuPlayer.updateSeen(weaponDeck.get(i));
		}
		for (int i = 1; i < roomDeck.size(); i++) {
			cpuPlayer.updateSeen(roomDeck.get(i));
		}
		
		//grab a solution from the cpu player with all cards seen, besides 3 and check it for top of deck cards
		Solution suggestion = cpuPlayer.createSuggestion(peopleDeck, roomDeck, weaponDeck);
		assertTrue(suggestion.equals(new Solution(peopleDeck.get(0), bridgeCard, weaponDeck.get(0))));
		
		//reset player, do all but 2 of each type of card seen
		cpuPlayer = new ComputerPlayer("K2-SO", Color.black, 3, 12);
		
		for (int i = 2; i < peopleDeck.size(); i++) {
			cpuPlayer.updateSeen(peopleDeck.get(i));
			cpuPlayer.updateSeen(weaponDeck.get(i));
		}
		for (int i = 2; i < roomDeck.size(); i++) {
			cpuPlayer.updateSeen(roomDeck.get(i));
		}
		
		//loop many times and make sure we see the solution is randomly chosen at least once
		boolean foundSolution = false;
		for (int i = 0; i < 500; i++) {
			suggestion = cpuPlayer.createSuggestion(peopleDeck, roomDeck, weaponDeck);
			if (suggestion.equals(new Solution(peopleDeck.get(0), bridgeCard, weaponDeck.get(0)))) {
				foundSolution = true;
			}
		}
		assertTrue(foundSolution);
	}	
	
	//Testing for Computer AI target selection
	@Test
	public void testComputerTarget() {
		//new player placed just outside bridge next to door
		ComputerPlayer cpuPlayer = new ComputerPlayer("K2-SO", Color.black, 6, 10);
		//player rolls a 1 for pathlength
		int roll = 2;
		//test that bridge is selected as an unseen room and not other spots
		assertTrue(board.getCell(3, 12).equals(cpuPlayer.findTarget(roll, roomDeck)));
		
		//move player a bit above the engine room
		cpuPlayer = new ComputerPlayer("K2-SO", Color.black, 20, 0);
		
		cpuPlayer.updateSeen(armoryCard);
		cpuPlayer.updateSeen(messCard);
		cpuPlayer.updateSeen(maintenanceCard);
		
		//impossible huge roll so we get multiple rooms as options
		roll = 16;
		
		//testing that only the unseen room of various options is picked
		assertTrue(board.getCell(30, 9).equals(cpuPlayer.findTarget(roll, roomDeck)));
		
		//move player a bit above the engine room
		cpuPlayer = new ComputerPlayer("K2-SO", Color.black, 24, 0);
		
		cpuPlayer.updateSeen(engineCard);
		
		//impossible huge roll so we get multiple rooms as options
		roll = 1;
		board.getCell(24, 1).setOccupied(true);
		boolean targetSelected = false;
		for (int i = 0; i < 100; i++) {
			if(board.getCell(23, 0).equals(cpuPlayer.findTarget(roll, roomDeck))) {
				targetSelected = true;
			}
		}
		//testing that no room is picked of various options when all rooms have been seen
		assertTrue(targetSelected);
		
	}

}
