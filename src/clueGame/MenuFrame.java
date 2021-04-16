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
	private JComboBox<String> map;
	private JComboBox<String> character;
	private ClueGame gameFrame;
	private String layoutConfigFile;
	private String setupConfigFile;
	
	public MenuFrame() {
		setSize(300, 150);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Main Menu");
		
		JPanel choicesPanel = createChoicesPanel();
		add(choicesPanel, BorderLayout.CENTER);
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		add(submit, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public JPanel createChoicesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		
		JLabel mapLabel = new JLabel("Choose a map:");
		panel.add(mapLabel);
		map = createMapMenu();
		panel.add(map);
		
		JLabel characterLabel = new JLabel("Choose your character:");
		panel.add(characterLabel);
		character = createCharacterMenu();
		panel.add(character);

		return panel;
	}
	
	public JComboBox<String> createMapMenu() {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("EbonHawk");
		combo.addItem("Executor");
		return combo;
	}
	
	public JComboBox<String> createCharacterMenu() {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem("//TODO");
		return combo;
	}
	
	
	
	private class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			layoutConfigFile = "ClueLayout" + (String) map.getSelectedItem() + ".csv";
			setupConfigFile = "ClueSetup" + (String) map.getSelectedItem() + ".txt";
			setVisible(false);
			ClueGame frame = new ClueGame(layoutConfigFile, setupConfigFile);
			frame.run();
		}
		
	}
	
	
	public static void main(String[] args) {
		MenuFrame menuFrame = new MenuFrame();
	}
	
	
}
