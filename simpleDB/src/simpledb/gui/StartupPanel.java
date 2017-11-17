package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JTextPane;

import simpledb.server.Startup;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class StartupPanel extends JPanel {
	private JTextField dbNameTextField;

	/**
	 * Create the panel.
	 */
	public StartupPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(SystemColor.menu);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPane.setEditable(false);
		textPane.setText("You are now in the process of starting the system. After entering the database name, the system will start. Please, enter database name.");
		textPane.setBounds(364, 240, 615, 38);
		add(textPane);
		
		JLabel dbNameLabel = new JLabel("DB Name:");
		dbNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		dbNameLabel.setBounds(364, 289, 62, 24);
		add(dbNameLabel);
		
		dbNameTextField = new JTextField();
		dbNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		dbNameTextField.setBounds(436, 289, 318, 24);
		add(dbNameTextField);
		dbNameTextField.setColumns(10);
		
		JLabel warningLabel = new JLabel("Please, enter database name.");
		warningLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel.setBounds(364, 324, 244, 24);
		add(warningLabel);
		warningLabel.setVisible(false);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dbName = dbNameTextField.getText();
				if(dbName.length() == 0)
					warningLabel.setVisible(true);
				else{
					try {
						new Startup(dbName);
						GUI.getInstance().initializeTableCatalogPanel();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					warningLabel.setVisible(false);
				}

			}
		});
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okButton.setBounds(665, 324, 89, 24);
		add(okButton);

	}
}
