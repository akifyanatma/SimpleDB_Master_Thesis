package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import simpledb.record.RecordFile;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTable;

public class ViewPanel extends JPanel {
	private JLabel blockCountLabel;
	private JLabel recordCountLabel;

	/**
	 * Create the panel.
	 */
	public ViewPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		JPanel internalPanel = new JPanel();
		internalPanel.setLayout(null);
		internalPanel.setBounds(29, 119, 1008, 608);
		add(internalPanel);
			
		JLabel tableNameLabel_1 = new JLabel("Table Name:");
		tableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_1.setBounds(29, 28, 87, 24);
		add(tableNameLabel_1);
		
		JTextField textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setColumns(10);
		textField.setBounds(116, 28, 230, 24);
		add(textField);
		
		JLabel warningLabel = new JLabel("Warning!");
		warningLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel.setBounds(368, 28, 625, 24);
		add(warningLabel);
		
		DefaultTableModel viewTableModel = new DefaultTableModel();
		
		JTable table = new JTable(viewTableModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(20);
		table.setBounds(0, 0, 1, 1);
		internalPanel.add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(37, 50, 942, 518);
		internalPanel.add(scrollPane);
		
		JLabel tableNameLabel_2 = new JLabel("Table Name");
		tableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_2.setBounds(37, 15, 247, 24);
		internalPanel.add(tableNameLabel_2);
		
		blockCountLabel = new JLabel("Block Count:");
		blockCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		blockCountLabel.setBounds(294, 15, 132, 24);
		internalPanel.add(blockCountLabel);
		
		recordCountLabel = new JLabel("Record Count:");
		recordCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel.setBounds(504, 15, 193, 24);
		internalPanel.add(recordCountLabel);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tableName = textField.getText();
				if(tableName.length() == 0){
					warningLabel.setText("Please enter table name.");
					warningLabel.setVisible(true);
				}
				else {
					boolean thereIsTable = getColumnNames(tableName, viewTableModel);
					if(thereIsTable == false){
						warningLabel.setText("\""+tableName+"\" table is not exist in database.");
						warningLabel.setVisible(true);
					}
					else{
						internalPanel.setVisible(true);
						tableNameLabel_2.setText(tableName);
						warningLabel.setVisible(false);					
						getTableRecords(tableName, viewTableModel);
					}
				}
			}
		});
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okButton.setBounds(257, 63, 89, 24);
		add(okButton);
				
	}
	
	//Doldurulacak olan tablonun field isimlerini getirir, eger tablo VT'de yoksa "false" doner.
	private boolean getColumnNames(String tableName, DefaultTableModel tableModel){
		tableModel.setColumnCount(0);
		tableModel.setRowCount(0);
		
		Transaction tx = new Transaction();
				
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
			
		boolean thereIsTable = false;
		
		if(sch.fields().size() != 0){
			for (String fld : sch.fields()) {
				tableModel.addColumn(fld);
			}
			
			thereIsTable = true;
		}
				
		tx.commit();
		return thereIsTable;
		
	}
	
	private void getTableRecords(String tableName, DefaultTableModel tableModel){
		Transaction tx = new Transaction();
		
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		int B = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).blocksAccessed();
		int R = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).recordsOutput();
		
		blockCountLabel.setText("B: "+String.valueOf(B));
		recordCountLabel.setText("R: "+String.valueOf(R));
				
		RecordFile recordFile = new RecordFile(ti, tx);
		
		while(recordFile.next()){
			Vector<Object> rowData = new Vector<Object>();
			for (String fld : sch.fields()) {
				if(sch.type(fld) == 4)
					rowData.add(recordFile.getInt(fld));
	//			else if(sch.type(fld) == 8)
	//				rowData.add(recordFile.getDouble(fld));
				else
					rowData.add(recordFile.getString(fld));
			}
			
			tableModel.addRow(rowData);
		}	
		tx.commit();
}

}
