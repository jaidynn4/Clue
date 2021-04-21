package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class CardTypePanel extends JPanel {
	private CardType type;					//Enum to represent type of cards this panel will hold
	private JTextField defaultHandText;		//The "None" text field used for the player's hand cards
	private JTextField defaultSeenText;		//The "None" text field used for the player's seen cards
	private boolean isSeenEmpty = true;		//Shows whether there are any cards in the list
	
	//Constructor
	public CardTypePanel(CardType type){
		super();
		this.type = type;
		setLayout(new GridLayout(0,1));
		
		//Set a string for the border label depending on the CardType enum
		String typeString = "ERROR";
		switch(type) {
		case PERSON:
			typeString = "People";
			break;
		case ROOM:
			typeString = "Rooms";
			break;
		case WEAPON:
			typeString = "Weapons";
		}
		
		//Set the border for this type panel
		setBorder(new TitledBorder (new EtchedBorder(), typeString));

		JLabel handLabel = new JLabel();
		handLabel.setText("In Hand:");
		add(handLabel);
		
		//Add a text box stating "None" as a placeholder until cards are added
		defaultHandText = new JTextField("None");
		defaultHandText.setEditable(false);
		add(defaultHandText);
		
		//add hand cards if any
		Board board = Board.getInstance();
		Player player = board.getPlayerList().get(0);
		Set<Card> hand = player.getHand();
		ArrayList<Card> cards = new ArrayList<Card>();
		for(Card card: hand) {
			if(card.getCardType() == type) {
				cards.add(card);
				JTextField cardName = new JTextField(card.getCardName());
				cardName.setEditable(false);
				add(cardName);
			}
		}
		if(cards.size() > 0) {
			remove(defaultHandText);
		}
		
		
		JLabel seenLabel = new JLabel();
		seenLabel.setText("Seen:");
		add(seenLabel);	
		
		//Add a text box stating "None" as a placeholder until cards are added
		defaultSeenText = new JTextField("None");
		defaultSeenText.setEditable(false);
		add(defaultSeenText);
		updateUI();
		
	}
		
	//Passes in card name and color (default is white) to add a card to the panel
	//Color represents the color of the player who showed a card to the human player
	public void updatePanel(Card card) {
		Player human = Board.getInstance().getPlayerList().get(0);
		if(!human.getSeen().contains(card)) {
			//If there are no cards in the panel, remove the none panel and update the boolean
			if(isSeenEmpty) {
				remove(defaultSeenText);
				isSeenEmpty = false;
			}
			
			//Add the new card to the panel
			JTextField cardText = new JTextField(card.getCardName());
			Border border = BorderFactory.createLineBorder(card.getCardHolder().getColor(), 5);
			cardText.setBorder(border);
			cardText.setEditable(false);
			add(cardText);
			updateUI();
		}
	}

	//Getter for type
	public CardType getType() {
		return type;
	}

	
}
