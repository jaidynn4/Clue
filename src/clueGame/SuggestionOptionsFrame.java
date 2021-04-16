package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import clueGame.GameControlPanel.NextListener;

public class SuggestionOptionsFrame extends JFrame {
	private JComboBox<Card> room;
	private JComboBox<Card> weapon;	//A combo box is the box with dropdown option menu
	private JComboBox<Card> person;
	private Board bpanel;
	private GameActionType gameActionType;
	private Card solutionPerson;
	private Card solutionRoom;
	private Card solutionWeapon;
	
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
		
		JLabel personLabel = new JLabel("Person");
		add(personLabel);
		person = createCardList(CardType.PERSON);
		add(person);
		
		JLabel roomLabel;
		//Use a different label for suggestions and accusations
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
		
		JLabel weaponLabel = new JLabel("Weapon");
		add(weaponLabel);
		weapon = createCardList(CardType.WEAPON);
		add(weapon);
		
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelListener());
		add(cancel);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		add(submit);
	}

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
	
	
	private void displayPopup() {
		JOptionPane popup = new JOptionPane();
		popup.showMessageDialog(this, "You cannot cancel making a suggestion.");
	}
	
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
	
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(gameActionType == GameActionType.SUGGESTION) {
				displayPopup();
			} else {
				CloseFrame();
			}
		}
		
	}
	
	public void CloseFrame() {
		super.dispose();
	}
}
