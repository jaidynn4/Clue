package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JPanel {
	private BoardCell[][] grid;				//The game board
	private Set<BoardCell> targets;			//List of possible cells to move to
	private Set<BoardCell> visited;			//List of cells visited in movement path
	private int numRows;					//The total number of rows in the game board
	private int numColumns;					//The total number of columns in the game board
	private String layoutConfigFile;		//Stores the filename of the layoutConfigFile
	private String setupConfigFile;			//Stores the filename of the setupConfigFile
	private Map<Character, Room> roomMap;	//The map of all rooms that exist on the board
	private ArrayList<Player> playerList;	//The map of all players that exist in the game
	private ArrayList<Card> theDeck;		//Set representing all cards that exist in the game
	private ArrayList<Card> playerDeck;		//Set of only player cards
	private ArrayList<Card> weaponDeck;		//Set of only weapon cards
	private ArrayList<Card> roomDeck;		//Set of only room cards
	private Solution theAnswer;				//The Solution to the game
	private boolean isTurnFinished = false;	//Flags whether the current player turn is finished, default to false
	private static Board theInstance;		//The Singleton Pattern instance of the board class
	private Player currentPlayer;
	private int currentPlayerIndex = 0;
	private int currentRoll;
	private int offset;
	private int cellWidth;
	private int cellHeight;
	private Solution latestGuess;
	private Card latestDisprove;
	private CardsDisplayPanel cdPanel;
	private GameControlPanel gcPanel;


	//Board constructor
	private Board() {
		super();
		setSize(620,620);
		addMouseListener(new BoardListener());
	}
	
	public CardsDisplayPanel getCdPanel() {
		return cdPanel;
	}

	public void setCdPanel(CardsDisplayPanel cdPanel) {
		this.cdPanel = cdPanel;
	}

	//Return the Singleton Pattern instance of the game board and create it if it does not already exist
	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}
	
	public Solution getLatestGuess() {
		return latestGuess;
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
		this.playerList = new ArrayList<Player>();
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
	
	
	public int getOffset() {
		return offset;
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
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
			if(playerList.size() != 0) {
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
				playerList.add(player);
				humanPlayerExists = true;
			} else {
				Player player = new ComputerPlayer(data[2], playerColor, row, col);
				playerList.add(player);
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
		
		//While there are still cards in theDeck, loop through the players and deal one card to the current player
		int i = 0;
		while(theDeck.size() > 0) {
			if(i == playerList.size()) {
				i = 0;
			} else {
				randNum = rand.nextInt(theDeck.size());
				Card card = theDeck.get(randNum);
				theDeck.remove(card);
				playerList.get(i).updateHand(card);
				card.setCardHolder(playerList.get(i));
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
		for (Player player: playerList) {
			if (player.getColor() == color) {
				return player;
			}
		}
		return null;
	}
	
	//Getter for the playerMap
	public ArrayList<Player> getPlayerList() {
		return playerList;
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
	public Card processSuggestion(ArrayList<Player> players, int accuserIndex, Solution suggestion) {
		int index = accuserIndex;
		while(true) {
			index++;
			//if end of players go to start of list, as we may start later into it
			if(index == players.size()) {
				index = 0;
			}
			//If we loop back through all players without returning, return null
			if (index == accuserIndex) {
				return null;
			}
			//have each player try to disprove, and return the first one we get
			Card card = players.get(index).disproveSuggestion(suggestion);
			if (card != null) {
				return card;
			}
		}
	}

	//runs all the graphics commands for the board to make cells, rooms, doors, and players
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		offset = 10;
		cellWidth = (getWidth() - 2*offset) / numColumns;
		cellHeight = (getHeight() - 2*offset) / numRows;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		//start will all cells, including rooms
		for(BoardCell[] cells: grid) {
			for(BoardCell cell: cells) {
				cell.draw(g, cellWidth, cellHeight, offset);
			}
		}
		
		//draw all the doors by looping through and calling the boardcell's drawDoors
		for(BoardCell[] cells: grid) {
			for(BoardCell cell: cells) {
				cell.drawDoors(g, cellWidth, cellHeight, offset);
			}
		}
		
		//this draws the room labels all over the board
		for(Room room: roomMap.values()) {
			room.draw(g, cellWidth, cellHeight, offset);
		}
		
		//places player ovals at their start locations
		for(Player player: playerList){
			player.draw(g, cellWidth, cellHeight, offset);
		}
	}
	
	
	public void handleNext() {
		if (!isTurnFinished) {
			JOptionPane popup = new JOptionPane();
			popup.showMessageDialog(this, "Wait for the current turn to be finished. . .");
		} else {
			//pushes to next player in rotation and starts next turn functions
			isTurnFinished = false;
			currentPlayer.setHasMovedThisTurn(false);
			updatePlayer();
			doNextTurn(currentPlayer instanceof HumanPlayer); //passes boolean on whether or not current player is a human player
		}
	}
	
	public void handleHumanPlayerAccusation() {
		if (currentPlayer instanceof HumanPlayer && !currentPlayer.isHasMovedThisTurn()) {
			SuggestionOptionsFrame accusation = new SuggestionOptionsFrame(Board.getInstance(), GameActionType.ACCUSATION);
		} else {
			JOptionPane popup = new JOptionPane();
			popup.showMessageDialog(this, "You may only make an accusation at the start of your turn.");
		}
	}
	
	public void updatePlayer() {
		//grab index of current player and increase it by 1
		currentPlayerIndex++;
		
		//reset index to 0 if we hit end of list
		if(currentPlayerIndex >= playerList.size()) {
			currentPlayerIndex = 0;
		}
		//set player as next in rotation
		currentPlayer = playerList.get(currentPlayerIndex);
	}
	
	public int rollDie() {
		//randomly roll a single die 1-6
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}
	
	public void doNextTurn(boolean isHuman) {
		currentRoll = rollDie();
		BoardCell playerCell = getCell(currentPlayer.row, currentPlayer.column);
		//If player is human, run human turn
		if(isHuman) {
			//Make a pop-up pane with a roll display message
			JOptionPane rollPopup = new JOptionPane();
			rollPopup.showMessageDialog(this,"You rolled a " + currentRoll + ". Please select a target.");
			
			//Set target cells
			calcTargets(playerCell, currentRoll);
			for(BoardCell cell: targets) {
				cell.setIsTarget(true);
			}
			
			//Add the current cell to the list of targets if the player was just moved to a room outside of their turn
			if(currentPlayer.isJustMoved()) {
				targets.add(getCell(currentPlayer.getRow(), currentPlayer.getColumn()));
				currentPlayer.setJustMoved(false);
			}
			
			if(targets.size() == 0) {
				//Make a pop-up pane with a display message stating that there are no targets
				JOptionPane noTargetsPopup = new JOptionPane();
				noTargetsPopup.showMessageDialog(this,"You have no available movement targets this turn. Please press next.");
				isTurnFinished = true;
			}
			
		//Else run computer turn
		} else {
			//Make accusation here if the computer player has found the solution
			Solution possibleAccusation = currentPlayer.canAccuse(playerDeck, roomDeck, weaponDeck);
			if(possibleAccusation != null) {
				endGame(checkAccusation(possibleAccusation));
			}
			
			//Have the computer player automatically select a target and move there.
			BoardCell target = currentPlayer.findTarget(currentRoll, roomDeck);
			currentPlayer.move(target.getRow(), target.getColumn());
			
			//Only return a latest guess and disprove if the computer player is able to make a suggestion.
			latestGuess = null;
			latestDisprove = null;
			//Check if the current player is in a room
			if(getCell(currentPlayer.getRow(), currentPlayer.getColumn()).isRoomCenter()) {
				//Mark that the player has just visited this room so that they will move elsewhere
				currentPlayer.setLastRoomVisited(getRoom(getCell(currentPlayer.getRow(), currentPlayer.getColumn())));
				
				latestGuess = currentPlayer.createSuggestion(playerDeck, roomDeck, weaponDeck);
				//Find the player that made the suggestion and move them to the room of the suggestion
				for (Player player: playerList) {
					if (latestGuess.getPerson().getCardName().equals(player.getName())) {
						player.setJustMoved(true);
						player.move(currentPlayer.getRow(), currentPlayer.getColumn());
						
					}
				}
				latestDisprove = processSuggestion(playerList, currentPlayerIndex, latestGuess);
			}
			if(latestDisprove != null) {
				currentPlayer.updateSeen(latestDisprove);
				if(currentPlayer instanceof HumanPlayer) {
					cdPanel.updateSeen(latestDisprove);
				}
			}
			
			isTurnFinished = true;
		}
		repaint();
	}
	
	public void handleHumanPlayerSuggestion(Solution suggestion) {
		repaint();
		Card result = processSuggestion(playerList, currentPlayerIndex, suggestion);
		gcPanel.setGuess(suggestion.toString());
		for (Player player: playerList) {
			if (player == currentPlayer) continue;
			if (suggestion.getPerson().getCardName().equals(player.getName())) {
				player.move(currentPlayer.getRow(), currentPlayer.getColumn());
				player.setJustMoved(true);
			}
		}
		if(result != null) {
			currentPlayer.updateSeen(result);
			cdPanel.updateHumanSeenLists(currentPlayer);
			gcPanel.setGuessResult(result.getCardHolder().getName() + " showed you \"" + result.getCardName() + "\"", result.getCardHolder().getColor());
		} else {
			gcPanel.setGuessResult("No one could disprove you. . .", Color.white);
		}
		isTurnFinished = true;
	}

	private class BoardListener implements MouseListener {
		public void mouseClicked(MouseEvent event) {
			for(BoardCell[] cells: grid) {
				for(BoardCell cell: cells) {
					if (cell.containsClick(event.getX(), event.getY())) {
						if(cell.isTarget) {
							currentPlayer.move(cell.getRow(), cell.getColumn());
							currentPlayer.setHasMovedThisTurn(true);
							isTurnFinished = true;
						} else if (cell.isPartOfRoom && getRoom(cell).getCenterCell().isTarget){
							currentPlayer.move(getRoom(cell).getCenterCell().getRow(), getRoom(cell).getCenterCell().getColumn());
							currentPlayer.setHasMovedThisTurn(true);
						} else {
							//Make a pop-up pane with an error display message depending on whether the player has moved or not yet
							JOptionPane popup = new JOptionPane();
							if(!currentPlayer.isHasMovedThisTurn() && currentPlayer instanceof HumanPlayer) {
								popup.showMessageDialog(Board.getInstance(),"Invalid click, please select a cyan target.");
							} else {
								popup.showMessageDialog(Board.getInstance(),"Invalid click, please wait for your next movement turn.");
							}
						}
					}
				}
			}
			if(currentPlayer.isHasMovedThisTurn()) {
				clearAllTargets();
			}
		}
		
		//Don't care about these unused methods
		public void mousePressed(MouseEvent event) {}
		public void mouseReleased(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}
	}
	
	public void clearAllTargets() {
		for(BoardCell[] cells: grid) {
			for(BoardCell cell: cells) {
				cell.setIsTarget(false);
			}
		}
		repaint();
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getCurrentRoll() {
		return currentRoll;
	}
	
	public Card getLatestDisprove() {
		return latestDisprove;
	}
	
	//Getter for the room object from char
	public Room getRoom(char icon) {
		return roomMap.get(icon);
	}
	
	//Getter for the room object from cell
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	//Getter for the list of adjacencies for a BoardCell
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
	
	public GameControlPanel getGcPanel() {
		return gcPanel;
	}

	public void setGcPanel(GameControlPanel gcPanel) {
		this.gcPanel = gcPanel;
	}
	
	public void endGame(boolean isCorrectAccusation) {
		JOptionPane popup = new JOptionPane();
		if(isCorrectAccusation) {
			popup.showMessageDialog(this, currentPlayer.getName() + " has accused \"" + theAnswer.toString() + "\" \n " + currentPlayer.getName() + " Wins!");
		} else {
			popup.showMessageDialog(this, "Correct answer was: " + theAnswer.toString() + " \n " + currentPlayer.getName() + " guessed wrong! \n Game Over!");
		}
		System.exit(0);
	}

	public String getLayoutConfigFile() {
		return this.layoutConfigFile;
	}
	
	public String getSetupConfigFile() {
		return this.setupConfigFile;
	}
}
