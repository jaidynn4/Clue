package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.awt.*;

public class Board {
	private BoardCell[][] grid;				//The game board
	private Set<BoardCell> targets;			//List of possible cells to move to
	private Set<BoardCell> visited;			//List of cells visited in movement path
	private int numRows;					//The total number of rows in the game board
	private int numColumns;					//The total number of columns in the game board
	private String layoutConfigFile;		//Stores the filename of the layoutConfigFile
	private String setupConfigFile;			//Stores the filename of the setupConfigFile
	private Map<Character, Room> roomMap;	//The map of all rooms that exist on the board
	private Map<Color, Player>	playerMap;	//The map of all players that exist in the game
	private ArrayList<Card> theDeck;		//Set representing all cards that exist in the game
	private ArrayList<Card> playerDeck;		//Set of only player cards
	private ArrayList<Card> weaponDeck;		//Set of only weapon cards
	private ArrayList<Card> roomDeck;		//Set of only room cards
	private Solution theAnswer;				//The Solution to the game
	private static Board theInstance;		//The Singleton Pattern instance of the board class
	
	
	//Board constructor
	private Board() {
		super();
	}
	
	//Return the Singleton Pattern instance of the game board and create it if it does not already exist
	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}
	
	//Initialize the game board
	public void initialize() {
		//Create new data structures for the instance variables
		this.targets = new HashSet<BoardCell>();
		this.visited = new HashSet<BoardCell>();
		this.theDeck = new ArrayList<Card>();
		this.playerDeck = new ArrayList<Card>();
		this.weaponDeck = new ArrayList<Card>();
		this.roomDeck = new ArrayList<Card>();
		this.roomMap = new HashMap<Character, Room>();
		this.playerMap = new HashMap<Color, Player>();
		//Load the files with the game board information and handle any exceptions thrown
		try {
			loadSetupConfig();
			loadLayoutConfig();
			for(BoardCell[] cells: grid) {
				for(BoardCell cell: cells) {
					cell.getAdjacencies();
				}
			}
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	//Load the Setup file
	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(setupConfigFile);
			Scanner scan = new Scanner(reader);
			boolean humanPlayerExists = false;	//Track whether a human player has been created
			//While the file has another line, read in the line
			while(scan.hasNext()) {
				String in = scan.nextLine();
				//If the line is a comment, ignore it and move on to the next line
				if(in.contains("//")) {
					continue;
				}
				//Create an array for each entry in the line
				String[] data = in.split(", ");
				ArrayList<String> allowedDataTypes = new ArrayList<String>(List.of("Room", "Space", "Player", "Weapon"));
				//Throw an exception if the first entry is invalid
				String type = data[0]; //Saves the type of object to setup
				if(!allowedDataTypes.contains(type)) {
					throw new BadConfigFormatException("Invalid type of cell - Needs to be Room or Space");
				}	
				if(type.equals("Room") || type.equals("Space")) {
					makeRoom(data, type); //Handles setup of room types if that is the data being sent by file
				}
				if(type.equals("Player")) {	
					//Creates a new player and adds it to the map of players
					humanPlayerExists = makePlayer(humanPlayerExists, data);
				}
				
				//Makes a card for each valid type of object
				makeCard(data, type);
				
			}
			if(playerMap.size() != 0) {
				deal();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	//Helper method for loadSetupConfig() that handles creation of a player object
	private boolean makePlayer(boolean humanPlayerExists, String[] data) {
		try {
			//convert string to color
			Color playerColor = (Color)Color.class.getField(data[1].toUpperCase()).get(null);
			
			//parse row and col info from file to int
			int row = Integer.parseInt(data[3]);
			int col = Integer.parseInt(data[4]);
			
			//Default setup makes the first player to appear in the setup file become the human player character
			if(!humanPlayerExists) {
				Player player = new HumanPlayer(data[2], playerColor, row, col);
				playerMap.put(playerColor, player);
				humanPlayerExists = true;
			} else {
				Player player = new ComputerPlayer(data[2], playerColor, row, col);
				playerMap.put(playerColor, player);
			}
		//Catch the many possible exceptions that the color class wants handled here
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			System.out.println(e.getMessage());
		}
		return humanPlayerExists;
	}

	
	
	
	
	//Helper method for loadSetupConfig() that handles setup of room types
	private void makeRoom(String[] data, String type) throws BadConfigFormatException {
		//Throw an exception if there are more than 3 entries in the line
		if(data.length != 3) {
			throw new BadConfigFormatException("Invalid setup, each room needs a type, a name, and an icon");
		}
		//Throw an exception if the room icon is not a single character
		if(data[2].length() != 1) {
			throw new BadConfigFormatException("Invalid room icon - Needs to be a single character");
		}
		//If we survived all that rigorous error testing, create a new room matching the data line.
		Room temp = new Room(data[1], type.equals("Room"));
		this.roomMap.put(data[2].charAt(0), temp);
	}
	
	
	
	//Helper method for loadSetupConfig() that handles setup of card types
	private void makeCard(String[] data, String type) {
		//Create a different card type depending on the data read from the setup config file
		Card card;
		switch(type) {
			case "Room":
				card = new Card(CardType.ROOM,data[1]);
				roomDeck.add(card);
				break;
			case "Player":
				card = new Card(CardType.PERSON, data[2]);
				playerDeck.add(card);
				break;
			case "Weapon":
				card = new Card(CardType.WEAPON, data[1]);
				weaponDeck.add(card);
				break;
			//If the data does not match a card type (e.g. the data is "Space") then ignore it
			default:
				return;
		}
		//Add the new card to theDeck
		theDeck.add(card);
	}

	
	
	//Deal out the cards
	public void deal() {
		Random rand = new Random();
		
		//Generate a random card of each type and deal them to the Solution
		int randNum = rand.nextInt(playerDeck.size());
		Card playerCard = playerDeck.get(randNum);
		randNum = rand.nextInt(roomDeck.size());
		Card roomCard = roomDeck.get(randNum);
		randNum = rand.nextInt(weaponDeck.size());
		Card weaponCard = weaponDeck.get(randNum);
		theAnswer = new Solution(playerCard, roomCard, weaponCard);
		
		//Remove the Solution cards from theDeck so that they are not dealt to players
		theDeck.remove(playerCard);
		theDeck.remove(roomCard);
		theDeck.remove(weaponCard);

		//Populate an Array of Player objects for ease of indexing
		Player[] players = new Player[playerMap.values().size()];
		int index = 0;
		for(Player player: playerMap.values()) {
			players[index] = player;
			index++;
		}
		
		//While there are still cards in theDeck, loop through the players and deal one card to the current player
		int i = 0;
		while(theDeck.size() > 0) {
			if(i == players.length) {
				i = 0;
			} else {
				randNum = rand.nextInt(theDeck.size());
				Card card = theDeck.get(randNum);
				theDeck.remove(card);
				players[i].updateHand(card);
				i++;
			}
		}
	}
	
	
	
	
	//Load the layout file
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(layoutConfigFile);
			Scanner scan = new Scanner(reader);
			String layout = getLayoutInfo(scan);	//Calls a helper method to return a String representation of the board layout
			
			//turn the scan into separated data we can read into our board with a split
			String[] data = layout.split(",");
			for (String entry: data) {
				//if an entry in the data has weird info for a cell, throw exception
				if (entry.length() > 2) {
					throw new BadConfigFormatException("Improper entry in file - ensure that each cell contains no more than 2 characters");
				}
			}
			
			int index = 0;	//Tracks the current index within the String array 'data'
			grid = new BoardCell[numRows][numColumns];	//A 2D array storing the cells on the game board by indices
			
			// loop through our grid that was just allocated and make a cell for each spot based on our data, setting flags and such as needed.
			for(int i = 0; i < numRows; i++) {
				for(int j =0; j < numColumns; j++) {
					grid[i][j] = new BoardCell(i,j);
					char icon = data[index].charAt(0);
					//If the room icon for a data set is not in our room map from setup, throw an exception
					if(!roomMap.keySet().contains(icon)) {
						throw new BadConfigFormatException("Mismatched Icon for Room passed, does not match setup file.");
					}
					//If the cell contains more than 1 character, use a switch statement to determine what to do with the 2nd character
					if (data[index].length() > 1) {
						char special = data[index].charAt(1);
						readSpecialCharacter(grid[i][j], special, icon);
					}
					//Set the icon and status of the cell
					grid[i][j].setInitial(icon);
					grid[i][j].setIsPartOfRoom(roomMap.get(icon).getIsRoom());
					index++;
				}
			}
		} catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}
	}

	

	//This helper method for loadLayoutConfig() checks the size of the map and returns a string for the board layout
	private String getLayoutInfo(Scanner scan) throws BadConfigFormatException {
		String layout = "";
		numRows = 0;
		numColumns = 0;
		// While we have lines in the file, keep reading in and collect row/column sizes
		while(scan.hasNext()) {
			if (numRows != 0) {
				layout += ",";
			}
			numRows++;
			String in = scan.nextLine();
			String[] tempData = in.split(",");
			if (numColumns == 0) {
				numColumns = tempData.length;
			}
			else {
				//if column size is not consistent throughout, throw exception for config file
				if (numColumns != tempData.length) {
					throw new BadConfigFormatException("Improper column sizes - check your file entries");
				}
			}
			layout += in;
		}
		return layout;
	}
	
	
	//This helper method for loadLayoutConfig() has a switch statement grabs the second character in the map data and compares it to a list of options to set various types of flags
	private void readSpecialCharacter(BoardCell cell, char special, char icon) throws BadConfigFormatException {
		switch(special) {
			case '#':
				cell.setRoomLabel(true);
				roomMap.get(icon).setLabelCell(cell);
				break;
			case '*':
				cell.setRoomCenter(true);
				roomMap.get(icon).setCenterCell(cell);
				break;
			case '^':
				cell.setDoorDirection(DoorDirection.UP);
				break;
			case '>':
				cell.setDoorDirection(DoorDirection.RIGHT);
				break;
			case 'v':
				cell.setDoorDirection(DoorDirection.DOWN);
				break;
			case '<':
				cell.setDoorDirection(DoorDirection.LEFT);
				break;
			default:
				//If none of the above cases are true, the character represents a secret passage.
				if(roomMap.keySet().contains(special)) {
					cell.setSecretPassage(special);
				}
				//Throw an exception if the character for the secret passage does not correspond to a room
				else {
					throw new BadConfigFormatException("Unrecognized special character, see room keys and review setup file.");
				}
				
				break;
		}
	}
	
	
	
	//Create the set of target cells - blank method stub
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		//recursive call for going through die roll size
		findAllTargets(startCell, pathlength);
	}
	
	//Recursive function called by calcTargets
	private void findAllTargets(BoardCell startCell, int pathlength) {
		for(BoardCell cell: startCell.getAdjList()) {
			if(!visited.contains(cell)) {
				//do not enter cell or pass through if it is occupied, unless it is the center of a room
				if(cell.isOccupied && !cell.isRoomCenter()) {
					continue;
				}
				visited.add(cell);
				if(pathlength == 1 || cell.isPartOfRoom()) {
					//if at a room or end of die roll length, add to targets
						targets.add(cell);
				} else {
					//keep going until die roll number is met
					findAllTargets(cell, pathlength - 1);
				}
				//clear visited spot for next run
				visited.remove(cell);
			}
		}
	}
	
	
	//Sets the chosen files for the project in the instance variables.
	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = "./data/" + layout;
		this.setupConfigFile = "./data/" + setup;
	}
	
	//Gets a player based on color
	public Player getPlayer(Color color) {
		return playerMap.get(color);
	}
	
	//Getter for the playerMap
	public Map<Color, Player> getPlayerMap() {
		return playerMap;
	}
	
	//Getter for theDeck
	public ArrayList<Card> getTheDeck() {
		return theDeck;
	}
	
	//Getter for playerDeck
	public ArrayList<Card> getPlayerDeck() {
		return playerDeck;
	}

	//Getter for weaponDeck
	public ArrayList<Card> getWeaponDeck() {
		return weaponDeck;
	}

	//Getter for roomDeck
	public ArrayList<Card> getRoomDeck() {
		return roomDeck;
	}

	public Solution getTheAnswer() {
		return theAnswer;
	}
	
	public void setTheAnswer(Card person, Card room, Card weapon) {
		this.theAnswer = new Solution(person, room, weapon);
	}
	
	public boolean checkAccusation(Solution accusation) {
		return theAnswer.equals(accusation);
	}

	//check a suggestion and have players disprove it
	public Card processSuggestion(Player accuser, Solution suggestion) {
		return null;
	}

	//Getter for the room object from char
	public Room getRoom(char icon) {
		return roomMap.get(icon);
	}
	
	//Getter for the room object from cell
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public Set<BoardCell> getAdjList(int x, int y) {
		return this.getCell(x,y).getAdjList();
	}
	
	//Getter for targets
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	//Getter for a specific cell within the board
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	//Getter for the number of rows
	public int getNumRows() {
		return numRows;
	}

	//Setter for the number of rows
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	//Getter for the number of columns
	public int getNumColumns() {
		return numColumns;
	}

	//Setter for the number of columns
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}
}
