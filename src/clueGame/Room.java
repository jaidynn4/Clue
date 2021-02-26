package clueGame;

public class Room {
	private String name;			//The name of the room
	private BoardCell centerCell;	//The center cell of the room
	private BoardCell labelCell;	//The label cell of the room
	
	//Constructor for the Room class
	public Room() {
		super();
	}
	
	//Getter for the name of the room
	public String getName() {
		return name;
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


