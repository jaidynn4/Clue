package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import clueGame.GameControlPanel.NextListener;

public class SuggestionOptionsFrame extends JFrame {
	private JComboBox<Card> room;   		//A combo box for the room(s)
	private JComboBox<Card> weapon;			//A combo box for the weapons
	private JComboBox<Card> person; 		//A combo box for the people
	private Board bpanel;  					//A reference to the board
	private GameActionType gameActionType;  //Helps us know whether the current box is a suggestion or not
	private Card solutionPerson; 			//Return for person in the suggestion/accusation
	private Card solutionRoom;   			//Return for room in the suggestion/accusation
	private Card solutionWeapon; 			//Return for weapon in the suggestion/accusation 
	
	//Sets up a new popup box for making suggestions and accusations, depending on enum passed.
	public SuggestionOptionsFrame(Board bpanel, GameActionType paneType) {
		this.bpanel = bpanel;
		this.gameActionType = paneType;
		this.setSize(400, 150);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(4,2));
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		
		if(this.gameActionType == GameActionType.ACCUSATION) {
			this.setTitle("Make an Accusation");
		} else {
			this.setTitle("Make a Suggestion");
		}
		//person box
		JLabel personLabel = new JLabel("Person");
		add(personLabel);
		person = createCardList(CardType.PERSON);
		add(person);
		
		JLabel roomLabel;
		//Use a different room label for suggestions and accusations
		//do full room dropdown for accusations but only current room for suggestions
		if(paneType == GameActionType.SUGGESTION) {
			roomLabel = new JLabel("Current Room");
			add(roomLabel);
			room = getCurrentRoomCombo();
			add(room);
		} else {
			roomLabel = new JLabel("Room");
			add(roomLabel);
			room = createCardList(CardType.ROOM);
			add(room);
		}
		//weapons box
		JLabel weaponLabel = new JLabel("Weapon");
		add(weaponLabel);
		weapon = createCardList(CardType.WEAPON);
		add(weapon);
		
		//add cancel and submit buttons
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelListener());
		add(cancel);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		add(submit);
	}
	
	//Populates a single option dropdown for rooms when suggestion is needed, using only current room
	private JComboBox<Card> getCurrentRoomCombo() {
		Room currentRoom = bpanel.getRoom(bpanel.getCell(bpanel.getCurrentPlayer().getRow(), bpanel.getCurrentPlayer().getColumn()));
		for(Card card: bpanel.getRoomDeck()) {
			if(card.getCardName().equals(currentRoom.getName())) {
				JComboBox<Card> combo = new JComboBox<Card>();
				combo.addItem(card);
				add(combo);
				return combo;
			}
		}
		return null;
	}
	
	//Populates the decks of cards per card type for drop down selection.
	public JComboBox<Card> createCardList(CardType type) {
		JComboBox<Card> combo = new JComboBox<Card>();
		ArrayList<Card> deck = new ArrayList<Card>();
		switch(type) {
			case PERSON:
				deck.addAll(bpanel.getPlayerDeck());
				break;
			case ROOM:
				deck.addAll(bpanel.getRoomDeck());
				break;
			case WEAPON:
				deck.addAll(bpanel.getWeaponDeck());
				break;
		}
		for(Card card: deck) {
			combo.addItem(card);
		}
		add(combo);
		return combo;
	}
	
	//creates a popup error for those trying to cancel a suggestion
	private void displayPopup() {
		JOptionPane popup = new JOptionPane();
		popup.showMessageDialog(this, "You cannot cancel making a suggestion.");
	}
	
	//Checks for submit button pressed, calls either endGame check for accusation or handles a human suggestion
	private class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			solutionPerson = (Card) person.getSelectedItem();
			solutionRoom = (Card) room.getSelectedItem();
			solutionWeapon = (Card) weapon.getSelectedItem();
			Solution suggestion = new Solution(solutionPerson, solutionRoom, solutionWeapon);
			if(gameActionType == GameActionType.SUGGESTION) {
				bpanel.handleHumanPlayerSuggestion(suggestion);
			} else {
				CloseFrame();
				bpanel.endGame(bpanel.checkAccusation(suggestion));
			}
			CloseFrame();
		}
		
	}
	
	//Checks for the cancel button and disallows any action if box is a suggestion rather than accusation
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(gameActionType == GameActionType.SUGGESTION) {
				displayPopup();
			} else {
				CloseFrame();
			}
		}
		
	}
	
	//Closes the frame
	public void CloseFrame() {
		super.dispose();
	}
}
