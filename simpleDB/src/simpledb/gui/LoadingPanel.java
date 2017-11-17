package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTable;

public class LoadingPanel extends JPanel {
	private LoadData loadData;
	private JPanel internalPanel;
	private JLabel warningLabel_1;
	private JLabel warningLabel_2;
	private JTextField tableNameTextField;
	private JTextField recordCountTextField;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public LoadingPanel() {
		loadData = new LoadData();
		
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		internalPanel = new JPanel();
		internalPanel.setLayout(null);
		internalPanel.setBounds(10, 214, 1008, 310);
		add(internalPanel);
		
		JLabel tableNameLabel_1 = new JLabel("Table Name:");
		tableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_1.setBounds(63, 61, 107, 24);
		add(tableNameLabel_1);
		
		JLabel tableNameLabel_2 = new JLabel("Table Name");
		tableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_2.setBounds(117, 67, 238, 24);
		internalPanel.add(tableNameLabel_2);
		
		tableNameTextField = new JTextField();
		tableNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameTextField.setBounds(206, 58, 359, 24);
		add(tableNameTextField);
		tableNameTextField.setColumns(10);
		
		warningLabel_1 = new JLabel("Warning!");
		warningLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_1.setBounds(63, 96, 684, 24);
		add(warningLabel_1);
		
		//Ok buttonu tiklandikdan sonra kullanildigi icin daha once create edilmesi gerekir.
		DefaultTableModel loadTableModel = new DefaultTableModel();
		
		table = new JTable(loadTableModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(20);
		table.setBounds(0, 0, 1, 1);
		internalPanel.add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setBounds(117, 103, 867, 83);
		internalPanel.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tableName = tableNameTextField.getText();
				if(tableName.length() == 0){
					warningLabel_1.setText("Please enter table name.");
					warningLabel_1.setVisible(true);
				}
				else if(getColumnNames(tableName, loadTableModel) == false){
					warningLabel_1.setText("\""+tableName+"\" table is not exist in database.");
					warningLabel_1.setVisible(true);
				}
				else{
					createTableSketch(tableName, loadTableModel);					
					tableNameLabel_2.setText(tableName);
					warningLabel_1.setVisible(false);
					warningLabel_2.setVisible(false);
					internalPanel.setVisible(true);
				}
			}
		});
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okButton.setBounds(476, 96, 89, 24);
		add(okButton);
					
		JLabel recordCountLabel = new JLabel("Record Count:");
		recordCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel.setBounds(410, 67, 103, 24);
		internalPanel.add(recordCountLabel);
		
		recordCountTextField = new JTextField();
		recordCountTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountTextField.setColumns(10);
		recordCountTextField.setBounds(506, 68, 86, 24);
		internalPanel.add(recordCountTextField);
		
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		recordCountTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		warningLabel_2 = new JLabel("Warning!");
		warningLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_2.setBounds(117, 197, 709, 24);
		internalPanel.add(warningLabel_2);
		
		JLabel distinctValueLabel = new JLabel("Distinct Value");
		distinctValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel.setBounds(33, 167, 103, 24);
		internalPanel.add(distinctValueLabel);
		
		JLabel maxValueLabel = new JLabel("Max Value");
		maxValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel.setBounds(33, 143, 103, 24);
		internalPanel.add(maxValueLabel);
		
		JLabel minValueLabel = new JLabel("Min Value");
		minValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel.setBounds(33, 120, 103, 24);
		internalPanel.add(minValueLabel);
		
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = recordCountTextField.getText();
				if(recordCountText.length() == 0){
					warningLabel_2.setText("Record count is missed. Please enter record count.");
					warningLabel_2.setVisible(true);
				}
				else if(isThereEmptyCell(loadTableModel)){
					warningLabel_2.setText("Missed information. Please fill all cell.");
					warningLabel_2.setVisible(true);
				}
				else{
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_2.getText();
					warningLabel_2.setVisible(false);
					loadData.generateFieldValues(tableName, recordCount, loadTableModel);				
				}
			}
		});
		loadButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton.setBounds(895, 197, 89, 24);
		internalPanel.add(loadButton);
		
	}
	
	//Load table paneli acildiginda componetlerin durumunu duzenler.
	public void editLoadingPanelComponents(){
		tableNameTextField.setText("");
		recordCountTextField.setText("");
		warningLabel_1.setVisible(false);
		warningLabel_2.setVisible(false);
		internalPanel.setVisible(false);
	}
	
	//Doldurulacak olan tablonun field isimlerini getirir. Eger tablo VT'de yoksa "false" doner.
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
	
	//Doldurulacak olan tablonun bos satirlarini olusturur.
	private void createTableSketch(String tableName, DefaultTableModel tableModel){
		Transaction tx = new Transaction();
				
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
					
		if(sch.fields().size() != 0){
			Vector<Object> rowData0 = new Vector<Object>();
			Vector<Object> rowData1 = new Vector<Object>();
			Vector<Object> rowData2 = new Vector<Object>();
			for (String fld : sch.fields()) {
				rowData0.add("");
				rowData1.add("");
				rowData2.add(""); 
			}
			
			tableModel.addRow(rowData0);
			tableModel.addRow(rowData1);
			tableModel.addRow(rowData2);			
		}
				
		tx.commit();		
	}
	
	//Ekrandaki tablo doldurulurken butun hucrelerin doldurulup doldurulmadigini kontrol eder.
	private boolean isThereEmptyCell(DefaultTableModel tableModel){
		boolean emptyCell = false;
		
		for(int i=0; i<tableModel.getColumnCount();i++){
			String minValue = (String)tableModel.getValueAt(0, i);
			String maxValue = (String)tableModel.getValueAt(1, i);
			String distinctValue = (String)tableModel.getValueAt(2, i);
				
			if(minValue.length() == 0 || maxValue.length() == 0 || distinctValue.length() == 0){
				emptyCell = true;
				break;
			}	
		}
		return emptyCell;
	}
}
