package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuFrame extends JFrame{
	private JComboBox<String> map;				//The player's choice of map
	private JComboBox<String> character;		//The player's choice of character
	private ClueGame gameFrame;					//An instance of ClueGame
	private String layoutConfigFile;			//The name of the layout file
	private String setupConfigFile;				//The name of the setup file
	
	public MenuFrame() {
		//Create the JFrame with standard setup options
		setSize(300, 150);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Main Menu");
		
		//Create a panel on the JFrame to store the menu selector
		JPanel choicesPanel = createChoicesPanel();
		add(choicesPanel, BorderLayout.CENTER);
		
		//Create a button for the user to submit their choices
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		add(submit, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	//Creates the menu selector JComboBoxes
	public JPanel createChoicesPanel() {
		JPanel panel = new JPanel();
		//Create it with the capacity to expand rows for if we ever implement the character selector
		panel.setLayout(new GridLayout(0,2));
		
		//Create the map selector panel
		JLabel mapLabel = new JLabel("Choose a map:");
		panel.add(mapLabel);
		map = createMapMenu();
		panel.add(map);
		
//Unused code to add a menu selector for which character the human player would like to be
//We may add this for fun at a later date but did not have time currently
		
//		JLabel characterLabel = new JLabel("Choose your character:");
//		panel.add(characterLabel);
//		character = createCharacterMenu();
//		panel.add(character);

		return panel;
	}
	
	//Create the map combo box
	public JComboBox<String> createMapMenu() {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("Executor");
		combo.addItem("EbonHawk");
		return combo;
	}
	
	//Create the character combo box (which is not added to the menu)
	public JComboBox<String> createCharacterMenu() {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("//TODO");
		return combo;
	}
	
	//When the user submits their choices, start the game by creating the ClueGame JFrame and hiding the menu frame
	private class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Pass in the selected map files to ClueGame
			layoutConfigFile = "ClueLayout" + (String) map.getSelectedItem() + ".csv";
			setupConfigFile = "ClueSetup" + (String) map.getSelectedItem() + ".txt";
			setVisible(false);
			ClueGame frame = new ClueGame(layoutConfigFile, setupConfigFile);
			frame.run();
		}
		
	}
	
	
	//All that needs to be done in the main method is the creation of a MenuFrame.
	public static void main(String[] args) {
		MenuFrame menuFrame = new MenuFrame();
	}	
}