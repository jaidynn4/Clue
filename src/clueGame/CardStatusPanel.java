package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class CardStatusPanel extends JPanel {
	private StatusCategory category;	//Stores whether this is a Hand list or a Seen list
	private JTextField defaultText;		//Stores the initial "None" text field while no cards are in the hand
	private boolean isEmpty = true;		//Shows whether there are any cards in the list
	
	//Constructor
	public CardStatusPanel(StatusCategory category) {
		super();
		this.category = category;
		
		//Set the number of layout rows equal to 0 so that it can expand as needed
		setLayout(new GridLayout(0,1));
		
		//Determine the text of the panel's label based on StatusCategory enum
		JLabel nameLabel = new JLabel();
		if(category == StatusCategory.HAND){
			nameLabel.setText("In Hand:");
		}
		else {
			nameLabel.setText("Seen:");
		}
		
		add(nameLabel);	
		
		//Add a text box stating "None" as a placeholder until cards are added
		defaultText = new JTextField("None");
		defaultText.setEditable(false);
		add(defaultText);
	}
	
	
	//Passes in card name and color (default is white) to add a card to the panel
	//Color represents the color of the player who showed a card to the human player
	public void updatePanel(String name, Color color) {
		//If there are no cards in the panel, remove the none panel and update the boolean
		if(isEmpty) {
			remove(defaultText);
			isEmpty = false;
		}
		//Add the new card to the panel
		JTextField card = new JTextField(name);
		Border border = BorderFactory.createLineBorder(color, 5);
		card.setBorder(border);
		card.setEditable(false);
		add(card);
		updateUI();
	}
	
	public void updateHuman(Player player, CardType type) {
		if(!isEmpty) {
			this.removeAll();
			JLabel nameLabel = new JLabel();
			nameLabel.setText("Seen:");
			add(nameLabel);
		}
		for(Card card: player.getSeen()) {
			if(card.getCardType() == type) {
				if(isEmpty) {
					remove(defaultText);
					isEmpty = false;
				}
				
				//Add the new card to the panel
				JTextField cardName = new JTextField(card.getCardName());
				Border border = BorderFactory.createLineBorder(card.getCardHolder().getColor(), 5);
				cardName.setBorder(border);
				cardName.setEditable(false);
				add(cardName);
				updateUI();
			}
		}
	}
}
