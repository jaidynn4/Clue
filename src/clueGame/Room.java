package clueGame;

public class Room {
	private String name;			//The name of the room
	private BoardCell centerCell;	//The center cell of the room
	private BoardCell labelCell;	//The label cell of the room
	private Boolean isSpace;
	
	//Constructor for the Room class
	public Room(String name, Boolean isSpace) {
		super();
		this.name = name;
		this.isSpace = isSpace;
	}
	
	//Getter for the name of the room
	public String getName() {
		return name;
	}
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	//Getter for the center cell of the room
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public Boolean getIsSpace() {
		return isSpace;
	}

	public void setIsSpace(Boolean isSpace) {
		this.isSpace = isSpace;
	}

	//Getter for the label cell of the room
	public BoardCell getLabelCell() {
		return labelCell;
	}
}


