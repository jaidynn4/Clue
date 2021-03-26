package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

public class GameControlPanelTestCode extends JPanel {
	private JTextField name;
	
	public GameControlPanelTestCode() {
		setLayout(new GridLayout(2,0));
		JPanel panel = createNamePanel();
		add(panel);
		panel = createButtonPanel();
		add(panel);
	}
	
	private JPanel createNamePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		JLabel nameLabel = new JLabel("Name");
		name = new JTextField(20);
		panel.add(nameLabel);
		panel.add(name);
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Who are you?"));
		return panel;
	}
	
	private JPanel createButtonPanel() {
		// no layout specified, so this is flow
		JButton agree = new JButton("I agree");
		JButton disagree = new JButton("I disagree");
		JPanel panel = new JPanel();
		panel.add(agree);
		panel.add(disagree);
		return panel;
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(250, 150);
		GameControlPanelTestCode gui = new GameControlPanelTestCode();
		frame.add(gui, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
}

