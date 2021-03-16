package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {
	private BoardCell[][] grid;				//The game board
	private Set<BoardCell> targets;			//List of possible cells to move to
	private Set<BoardCell> visited;			//List of cells visited in movement path
	private int numRows;					//The total number of rows in the game board
	private int numColumns;					//The total number of columns in the game board
	private String layoutConfigFile;		//Stores the filename of the layoutConfigFile
	private String setupConfigFile;			//Stores the filename of the setupConfigFile
	private Map<Character, Room> roomMap;	//The map of all rooms that exist on the board
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
		this.roomMap = new HashMap<Character, Room>();
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
	
	
	//Sets the chosen files for the project in the instance variables.
	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = "./data/" + layout;
		this.setupConfigFile = "./data/" + setup;
	}
	
	//Load the Setup file
	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(setupConfigFile);
			Scanner scan = new Scanner(reader);
			//While the file has another line, read in the line
			while(scan.hasNext()) {
				String in = scan.nextLine();
				//If the line is a comment, ignore it and move on to the next line
				if(in.contains("//")) {
					continue;
				}
				//Create an array for each entry in the line
				String[] data = in.split(", ");
				//Throw an exception if there are more than 3 entries in the line
				if(data.length != 3) {
					throw new BadConfigFormatException("Invalid setup, each room needs a type, a name, and an icon");
				}
				//Throw an exception if the first entry is invalid
				if(!data[0].equals("Space") && !data[0].equals("Room")) {
					throw new BadConfigFormatException("Invalid type of cell - Needs to be Room or Space");
				}
				//Throw an exception if the room icon is not a single character
				if(data[2].length() != 1) {
					throw new BadConfigFormatException("Invalid room icon - Needs to be a single character");
				}
				//If we survived all that rigorous error testing, create a new room matching the data line.
				Room temp = new Room(data[1], data[0].equals("Room"));
				this.roomMap.put(data[2].charAt(0), temp);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Load the layout file
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(layoutConfigFile);
			Scanner scan = new Scanner(reader);
			String layout = getLayoutInfo(scan);
			//turn the scan into separated data we can read into our board with a split
			String[] data = layout.split(",");
			for (String entry: data) {
				//if an entry in the data has weird info for a cell, throw exception
				if (entry.length() > 2) {
					throw new BadConfigFormatException("Improper entry in file - ensure that each cell contains no more than 2 characters");
				}
			}
			int index = 0;
			grid = new BoardCell[numRows][numColumns];
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
					readSpecialCharacter(data, index, i, j, icon);
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

	private void readSpecialCharacter(String[] data, int index, int i, int j, char icon)
			throws BadConfigFormatException {
		if (data[index].length() > 1) {
			char special = data[index].charAt(1);
			//This switch statement grabs the second character in the map data and compares it to a list of options to set various types of flags
			switch(special) {
				case '#':
					grid[i][j].setRoomLabel(true);
					roomMap.get(icon).setLabelCell(grid[i][j]);
					break;
				case '*':
					grid[i][j].setRoomCenter(true);
					roomMap.get(icon).setCenterCell(grid[i][j]);
					break;
				case '^':
					grid[i][j].setDoorDirection(DoorDirection.UP);
					break;
				case '>':
					grid[i][j].setDoorDirection(DoorDirection.RIGHT);
					break;
				case 'v':
					grid[i][j].setDoorDirection(DoorDirection.DOWN);
					break;
				case '<':
					grid[i][j].setDoorDirection(DoorDirection.LEFT);
					break;
				default:
					//If none of the above cases are true, the character represents a secret passage.
					if(roomMap.keySet().contains(special)) {
						grid[i][j].setSecretPassage(special);
					}
					//Throw an exception if the character for the secret passage does not correspond to a room
					else {
						throw new BadConfigFormatException("Unrecognized special character, see room keys and review setup file.");
					}
					
					break;
			}
		}
	}

	/**
	 * @param scan
	 * @return
	 * @throws BadConfigFormatException
	 */
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
	
	
	
	//Getter for the room character
	public Room getRoom(Character icon) {
		return roomMap.get(icon);
	}
	
	//Getter for the full room object
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
