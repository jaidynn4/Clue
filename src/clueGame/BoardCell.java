package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;


public class BoardCell {
	private int row;												//Row of this cell on the board
	private int col;												//Column of this cell on the board
	private char initial;											//The character representing the type of room that this cell belongs to
	private char secretPassage;										//The destination of the secret passage, if one exists
	private boolean roomLabel;										//Holds whether this cell is a room label
	private boolean roomCenter;										//Holds whether this cell is a room center
	private DoorDirection doorDirection = DoorDirection.NONE;		//Holds the direction of a door from the enum class DoorDirection
	private Set<BoardCell> adjacencyList;							//Set of cells adjacent to this cell
	boolean isPartOfRoom;											//Holds whether this cell is a room
	boolean isOccupied;												//Holds whether this cell is occupied
	boolean	isTarget = false;										//Stores whether this cell is currently a target cell
	private int x, y;												//Stores the top left coordinates of this cell on the JPanel for the board
	
	//TestBoardCell constructor
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.col = column;
		this.secretPassage = '!';
		this.adjacencyList = new HashSet<BoardCell>();
	}
	
	
	//Create the adjacency list for this cell
	public void getAdjacencies() {
		Board theBoard = Board.getInstance();	//Reference to the instance of the game board
		
		//Check whether to add the cell to the west
		if (this.col > 0) {
			if (shouldBeAdjacent(this, theBoard.getCell(row, col - 1), DoorDirection.LEFT)) {
				adjacencyList.add(theBoard.getCell(row, col - 1));
			}
		}
		//Check whether to add the cell to the east
		if (this.col < theBoard.getNumColumns() - 1){
			if( shouldBeAdjacent(this, theBoard.getCell(row, col + 1), DoorDirection.RIGHT)) {
				adjacencyList.add(theBoard.getCell(row, col + 1));
			}
		}
		//Check whether to add the cell to the north
		if (this.row > 0) {
			if(shouldBeAdjacent(this, theBoard.getCell(row - 1, col), DoorDirection.UP)) {
				adjacencyList.add(theBoard.getCell(row - 1, col));
			}
		}
		//Check whether to add the cell to the south
		if (this.row < theBoard.getNumRows() - 1) {
			if(shouldBeAdjacent(this, theBoard.getCell(row + 1, col), DoorDirection.DOWN)) {
				adjacencyList.add(theBoard.getCell(row + 1, col));
			}
		}
		return;
	}
	
	
	
	//A helper method used for getAdjacencies, decides if the cell in question should be added to list
	public boolean shouldBeAdjacent(BoardCell currentCell, BoardCell cellToCheck, DoorDirection door) {
		Board theBoard = Board.getInstance(); //Reference to the instance of the game board
		
		//false if unused
		if (cellToCheck.getInitial() == 'X') {
			return false;
		}
		
		//if the cell's door direction matches the specified door direction, link it to the room center
		if (currentCell.doorDirection == door) {
			Room adjacentRoom = theBoard.getRoom(cellToCheck.getInitial());	//The initial of the room of the cell we are checking
			adjacencyList.add(adjacentRoom.getCenterCell());
			adjacentRoom.getCenterCell().addAdjacency(currentCell);
			return false;
		}
		
		//if the room is a secret passage indicator, add the secret passage room's center cell to the
		//adjacency list of the current room's center cell
		if (currentCell.secretPassage != '!') {
			Room currentRoom = theBoard.getRoom(currentCell.getInitial());		//The initial of the room of the current cell
			Room passageRoom = theBoard.getRoom(currentCell.secretPassage);		//The initial of the secret passage room
			currentRoom.getCenterCell().addAdjacency(passageRoom.getCenterCell());
			return false;
		}
		//false if cell is a room
		if (currentCell.isPartOfRoom()) {
			return false;
		}
		//false if cell to check is a room
		if (cellToCheck.isPartOfRoom()) {
			return false;
		}
		return true;
	}

	//draws all cells in greyscale to meet theme of Imperial Starship
	public void draw(Graphics g, int cellWidth, int cellHeight, int offset) {
		x = cellWidth*col+offset+1;
		y = cellHeight*row+offset+1;
		
		//First make all cells and for rooms push out with a slight offset for borders
		if(isPartOfRoom) {
			if(Board.getInstance().getRoom(initial).getCenterCell().isTarget) {
				g.setColor(Color.cyan);
			}else {
				g.setColor(Color.LIGHT_GRAY);
			}
			g.fillRect(x, y, cellWidth, cellHeight);
		} else if (initial =='X') {
			//unused spaces are just black
			g.setColor(Color.BLACK);
			g.fillRect(x, y, cellWidth-1, cellHeight-1);
		} else {
			if(isTarget) {
				g.setColor(Color.cyan);
			}else {
				g.setColor(Color.GRAY);
			}
			g.fillRect(cellWidth*col+1+offset, cellHeight*row+1+offset, cellWidth-1, cellHeight-1);
			g.setColor(Color.BLACK);
			g.drawRect(cellWidth*col+offset,  cellHeight*row+offset, cellWidth,  cellHeight);
		}
		
	}
	
	//Loop through again here and draw all the doors after the rooms are created
	//Set to 1/5th the size of the cell sitting right on the borders so it feels like a energy field blue door
	public void drawDoors(Graphics g, int cellWidth, int cellHeight, int offset) {
		g.setColor(Color.BLUE);
		final int DOOR_SIZE = 5;
		switch(doorDirection) {
			case UP:
				g.fillRect(cellWidth*col+offset, cellHeight*(row)+offset-2, cellWidth, DOOR_SIZE);
				break;
			case DOWN:
				g.fillRect(cellWidth*col+offset, cellHeight*(row+1)+offset-1, cellWidth, DOOR_SIZE);
				break;
			case LEFT:
				g.fillRect(cellWidth*(col)+offset-2, cellHeight*row+offset, DOOR_SIZE, cellHeight);
				break;
			case RIGHT:
				g.fillRect(cellWidth*(col+1)+offset-1, cellHeight*row+offset, DOOR_SIZE, cellHeight);
				break;
		}
	}
	
	//Check if click is within box
	public boolean containsClick(int mouseX, int mouseY) {
		Rectangle rect = new Rectangle(x, y, Board.getInstance().getCellWidth(), Board.getInstance().getCellHeight());
		if (rect.contains(new Point(mouseX, mouseY))) {
			return true;
		}
		return false;
	}
	
	
	public void setIsTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
	
	//Update the adjacencyList with a cell
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}
		
	//Getter for row
	public int getRow() {
		return row;
	}

	//Getter for column
	public int getColumn() {
		return col;
	}
	
	//Check if the cell is a doorway
	public boolean isDoorway() {
		if(doorDirection == DoorDirection.NONE) {
			return false;
		}
		return true;
	}
	
	//Check if the cell is a label
	public boolean isLabel() {
		return roomLabel;
	}
	
	//Check if the cell is a room center
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	//Check the direction of the door in a cell
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	//Check if the room is a secret passage
	public Character getSecretPassage() {
		return secretPassage;
	}

	//Getter for the initial of this cell
	public char getInitial() {
		return initial;
	}

	//Setter for the initial of this cell
	public void setInitial(char initial) {
		this.initial = initial;
	}

	//Setter for secret passage 
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	//Tests if this cell is a room label
	public boolean isRoomLabel() {
		return roomLabel;
	}

	//Setter for roomLabel
	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	//Setter for roomCenter
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	//Setter for DoorDirection
	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	//Getter for  adjacencyList
	public Set<BoardCell> getAdjList() {
		return adjacencyList;
	}

	//Getter for isRoom
	public boolean isPartOfRoom() {
		return isPartOfRoom;
	}

	//Setter for isRoom
	public void setIsPartOfRoom(boolean isRoom) {
		this.isPartOfRoom = isRoom;
	}

	//Getter for isOccupied
	public boolean isOccupied() {
		return isOccupied;
	}

	//Setter for isOccupied
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
}
