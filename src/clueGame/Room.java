package clueGame;

public class Room {
	private String name;			//The name of the room
	private BoardCell centerCell;	//The center cell of the room
	private BoardCell labelCell;	//The label cell of the room
	private Boolean isRoom;			//Stores whether this room is a room rather than a space
	
	//Constructor for the Room class
	public Room(String name, Boolean isRoom) {
		super();
		this.name = name;
		this.isRoom = isRoom;
	}
	
	//Getter for isRoom
	public Boolean getIsRoom() {
		return isRoom;
	}

	//Setter for isRoom
	public void setIsRoom(Boolean isRoom) {
		this.isRoom = isRoom;
	}

	//Getter for the name of the room
	public String getName() {
		return name;
	}
	
	//Setter for centerCell
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	//Setter for labelCell
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	//Getter for the center cell of the room
	public BoardCell getCenterCell() {
		return centerCell;
	}

	//Getter for the label cell of the room
	public BoardCell getLabelCell() {
		return labelCell;
	}
}


