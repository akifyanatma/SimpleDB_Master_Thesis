package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class ExDbPanel extends JPanel {
	private LoadData loadData;
	private JLabel warningLabel_1;
	private JLabel warningLabel_2;
	private JLabel warningLabel_3;
	private JLabel warningLabel_4;
	private JLabel warningLabel_5;
	
	private DefaultTableModel tableModel_1;
	private DefaultTableModel tableModel_2;
	private DefaultTableModel tableModel_3;
	private DefaultTableModel tableModel_4;
	private DefaultTableModel tableModel_5;
	
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	/**
	 * Create the panel.
	 */
	public ExDbPanel() {
		loadData = new LoadData();
		
		setPreferredSize(new Dimension(1360, 920));
		setLayout(null);
		
		JLabel tableNameLabel_1 = new JLabel("Table Name:");
		tableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_1.setBounds(133, 22, 89, 24);
		add(tableNameLabel_1);
		
		JLabel tableNameLabel_2 = new JLabel("Table Name:");
		tableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_2.setBounds(133, 195, 89, 24);
		add(tableNameLabel_2);
		
		JLabel tableNameLabel_3 = new JLabel("Table Name:");
		tableNameLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_3.setBounds(133, 365, 89, 24);
		add(tableNameLabel_3);
		
		JLabel tableNameLabel_4 = new JLabel("Table Name:");
		tableNameLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_4.setBounds(133, 546, 89, 24);
		add(tableNameLabel_4);
		
		JLabel tableNameLabel_5 = new JLabel("Table Name:");
		tableNameLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_5.setBounds(133, 732, 89, 24);
		add(tableNameLabel_5);
		
		JLabel tableNameLabel_1_1 = new JLabel("student");
		tableNameLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_1_1.setBounds(214, 22, 89, 24);
		add(tableNameLabel_1_1);
		
		JLabel tableNameLabel_2_1 = new JLabel("dept");
		tableNameLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_2_1.setBounds(214, 195, 89, 24);
		add(tableNameLabel_2_1);
		
		JLabel tableNameLabel_3_1 = new JLabel("course");
		tableNameLabel_3_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_3_1.setBounds(214, 365, 89, 24);
		add(tableNameLabel_3_1);
		
		JLabel tableNameLabel_4_1 = new JLabel("section");
		tableNameLabel_4_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_4_1.setBounds(214, 546, 89, 24);
		add(tableNameLabel_4_1);
		
		JLabel tableNameLabel_5_1 = new JLabel("enroll");
		tableNameLabel_5_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel_5_1.setBounds(214, 732, 89, 24);
		add(tableNameLabel_5_1);
		
		JLabel recordCountLabel_1 = new JLabel("Record Count:");
		recordCountLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel_1.setBounds(390, 22, 103, 24);
		add(recordCountLabel_1);
		
		JLabel recordCountLabel_2 = new JLabel("Record Count:");
		recordCountLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel_2.setBounds(390, 195, 103, 24);
		add(recordCountLabel_2);
		
		JLabel recordCountLabel_3 = new JLabel("Record Count:");
		recordCountLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel_3.setBounds(390, 365, 103, 24);
		add(recordCountLabel_3);
		
		JLabel recordCountLabel_4 = new JLabel("Record Count:");
		recordCountLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel_4.setBounds(390, 546, 103, 24);
		add(recordCountLabel_4);
		
		JLabel recordCountLabel_5 = new JLabel("Record Count:");
		recordCountLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel_5.setBounds(390, 732, 103, 24);
		add(recordCountLabel_5);
		
		JLabel minValueLabel_1 = new JLabel("Min Value");
		minValueLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel_1.setBounds(36, 68, 91, 24);
		add(minValueLabel_1);
		
		JLabel minValueLabel_2 = new JLabel("Min Value");
		minValueLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel_2.setBounds(36, 238, 91, 24);
		add(minValueLabel_2);
		
		JLabel minValueLabel_3 = new JLabel("Min Value");
		minValueLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel_3.setBounds(36, 404, 91, 24);
		add(minValueLabel_3);
		
		JLabel minValueLabel_4 = new JLabel("Min Value");
		minValueLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel_4.setBounds(36, 587, 91, 24);
		add(minValueLabel_4);
		
		JLabel minValueLabel_5 = new JLabel("Min Value");
		minValueLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minValueLabel_5.setBounds(36, 776, 91, 24);
		add(minValueLabel_5);
		
		JLabel maxValueLabel_1 = new JLabel("Max Value");
		maxValueLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel_1.setBounds(36, 89, 91, 24);
		add(maxValueLabel_1);
		
		JLabel maxValueLabel_2 = new JLabel("Max Value");
		maxValueLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel_2.setBounds(36, 261, 91, 24);
		add(maxValueLabel_2);
		
		JLabel maxValueLabel_3 = new JLabel("Max Value");
		maxValueLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel_3.setBounds(36, 425, 91, 24);
		add(maxValueLabel_3);
		
		JLabel maxValueLabel_4 = new JLabel("Max Value");
		maxValueLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel_4.setBounds(36, 609, 91, 24);
		add(maxValueLabel_4);
		
		JLabel maxValueLabel_5 = new JLabel("Max Value");
		maxValueLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxValueLabel_5.setBounds(36, 799, 91, 24);
		add(maxValueLabel_5);
		
		JLabel distinctValueLabel_1 = new JLabel("Distinct Value");
		distinctValueLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel_1.setBounds(36, 109, 91, 24);
		add(distinctValueLabel_1);
		
		JLabel distinctValueLabel_2 = new JLabel("Distinct Value");
		distinctValueLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel_2.setBounds(36, 283, 91, 24);
		add(distinctValueLabel_2);
		
		JLabel distinctValueLabel_3 = new JLabel("Distinct Value");
		distinctValueLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel_3.setBounds(36, 449, 91, 24);
		add(distinctValueLabel_3);
		
		JLabel distinctValueLabel_4 = new JLabel("Distinct Value");
		distinctValueLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel_4.setBounds(36, 632, 91, 24);
		add(distinctValueLabel_4);
		
		JLabel distinctValueLabel_5 = new JLabel("Distinct Value");
		distinctValueLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		distinctValueLabel_5.setBounds(36, 822, 91, 24);
		add(distinctValueLabel_5);
		
		warningLabel_1 = new JLabel("Warning!");
		warningLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_1.setBounds(123, 140, 716, 24);
		add(warningLabel_1);
		
		warningLabel_2 = new JLabel("Warning!");
		warningLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_2.setBounds(133, 313, 716, 24);
		add(warningLabel_2);
		
		warningLabel_3 = new JLabel("Warning!");
		warningLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_3.setBounds(133, 482, 716, 24);
		add(warningLabel_3);
		
		warningLabel_4 = new JLabel("Warning!");
		warningLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_4.setBounds(133, 665, 716, 24);
		add(warningLabel_4);
		
		warningLabel_5 = new JLabel("Warning!");
		warningLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel_5.setBounds(133, 853, 716, 24);
		add(warningLabel_5);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(475, 22, 89, 24);
		add(textField_1);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(475, 196, 89, 24);
		add(textField_2);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		textField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_3.setColumns(10);
		textField_3.setBounds(475, 366, 89, 24);
		add(textField_3);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		textField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_4.setColumns(10);
		textField_4.setBounds(475, 547, 89, 24);
		add(textField_4);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		textField_4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_5.setColumns(10);
		textField_5.setBounds(475, 733, 89, 24);
		add(textField_5);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		textField_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		tableModel_1 = new DefaultTableModel();		
		JTable table_1 = new JTable(tableModel_1);
		table_1.setRowHeight(20);
		table_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_1.setBounds(0, 0, 1, 1);
		add(table_1);
		
		tableModel_2 = new DefaultTableModel();
		JTable table_2 = new JTable(tableModel_2);
		table_2.setRowHeight(20);
		table_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_2.setBounds(0, 0, 1, 1);
		add(table_2);

		tableModel_3 = new DefaultTableModel();
		JTable table_3 = new JTable(tableModel_3);
		table_3.setRowHeight(20);
		table_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_3.setBounds(0, 0, 1, 1);
		add(table_3);
		
		tableModel_4 = new DefaultTableModel();
		JTable table_4 = new JTable(tableModel_4);
		table_4.setRowHeight(20);
		table_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_4.setBounds(0, 0, 1, 1);
		add(table_4);
		
		tableModel_5 = new DefaultTableModel();
		JTable table_5 = new JTable(tableModel_5);
		table_5.setRowHeight(20);
		table_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_5.setBounds(0, 0, 1, 1);
		add(table_5);
		
		JScrollPane scrollPane_1 = new JScrollPane(table_1);
		scrollPane_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_1.setBounds(123, 50, 875, 83);
		add(scrollPane_1);
		
		JScrollPane scrollPane_2 = new JScrollPane(table_2);
		scrollPane_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_2.setBounds(123, 224, 873, 83);
		add(scrollPane_2);
		
		JScrollPane scrollPane_3 = new JScrollPane(table_3);
		scrollPane_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_3.setBounds(123, 394, 873, 83);
		add(scrollPane_3);
		
		JScrollPane scrollPane_4 = new JScrollPane(table_4);
		scrollPane_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_4.setBounds(123, 576, 873, 83);
		add(scrollPane_4);
		
		JScrollPane scrollPane_5 = new JScrollPane(table_5);
		scrollPane_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_5.setBounds(123, 763, 873, 83);
		add(scrollPane_5);
		
		JButton loadButton_1 = new JButton("Load");
		loadButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = textField_1.getText();
				if(recordCountText.length() == 0){
					warningLabel_1.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(tableModel_1)){
					warningLabel_1.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_1_1.getText();
					createTable(tableName);
					loadData.generateFieldValues(tableName, recordCount, tableModel_1);
					warningLabel_1.setText("\" "+tableName+ " \" is created.");
				}
				warningLabel_1.setVisible(true);
			}
		});
		loadButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton_1.setBounds(909, 140, 89, 24);
		add(loadButton_1);
				
		JButton loadButton_2 = new JButton("Load");
		loadButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = textField_2.getText();
				if(recordCountText.length() == 0){
					warningLabel_2.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(tableModel_2)){
					warningLabel_2.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_2_1.getText();
					createTable(tableName);
					loadData.generateFieldValues(tableName, recordCount, tableModel_2);
					warningLabel_2.setText("\" "+tableName+ " \" is created.");
				}
				warningLabel_2.setVisible(true);
			}
		});
		loadButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton_2.setBounds(909, 313, 89, 24);
		add(loadButton_2);
		
		JButton loadButton_3 = new JButton("Load");
		loadButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = textField_3.getText();
				if(recordCountText.length() == 0){
					warningLabel_3.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(tableModel_3)){
					warningLabel_3.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_3_1.getText();
					createTable(tableName);
					loadData.generateFieldValues(tableName, recordCount, tableModel_3);
					warningLabel_3.setText("\" "+tableName+ " \" is created.");
				}
				warningLabel_3.setVisible(true);
			}
		});
		loadButton_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton_3.setBounds(909, 482, 89, 24);
		add(loadButton_3);
		
		JButton loadButton_4 = new JButton("Load");
		loadButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = textField_4.getText();
				if(recordCountText.length() == 0){
					warningLabel_4.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(tableModel_4)){
					warningLabel_4.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_4_1.getText();
					createTable(tableName);
					loadData.generateFieldValues(tableName, recordCount, tableModel_4);
					warningLabel_4.setText("\" "+tableName+ " \" is created.");
				}
				warningLabel_4.setVisible(true);
			}
		});
		loadButton_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton_4.setBounds(909, 665, 89, 24);
		add(loadButton_4);
		
		JButton loadButton_5 = new JButton("Load");
		loadButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = textField_5.getText();
				if(recordCountText.length() == 0){
					warningLabel_5.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(tableModel_5)){
					warningLabel_5.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = tableNameLabel_5_1.getText();
					createTable(tableName);
					loadData.generateFieldValues(tableName, recordCount, tableModel_5);
					warningLabel_5.setText("\" "+tableName+ " \" is created.");
				}
				warningLabel_5.setVisible(true);
			}
		});
		loadButton_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadButton_5.setBounds(909, 853, 89, 24);
		add(loadButton_5);
	}
	
	//Ekrandaki bir tablo doldurulurken butun hucrelerin doldurulup doldurulmadigini kontrol eder.
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
	
	public void editExDbLoadPanel(){
		warningLabel_1.setVisible(false);
		warningLabel_2.setVisible(false);
		warningLabel_3.setVisible(false);
		warningLabel_4.setVisible(false);
		warningLabel_5.setVisible(false);
		
		createExTableProposal_1(tableModel_1, textField_1);
		createExTableProposal_2(tableModel_2, textField_2);
		createExTableProposal_3(tableModel_3, textField_3);
		createExTableProposal_4(tableModel_4, textField_4);
		createExTableProposal_5(tableModel_5, textField_5);
	}
	
	private void createTable(String tableName){
		Transaction tx = new Transaction();
		
		String query = "";
		if(tableName.equals("student")){
			query = "create table student(sid int, sname varchar(60), gradyear int, majorid int)";
			SimpleDB.planner().executeUpdate(query, tx);
		}
		else if(tableName.equals("dept")){
			query = "create table dept(did int, dname varchar(28))";
			SimpleDB.planner().executeUpdate(query, tx);
		}
		else if(tableName.equals("course")){
			query = "create table course(cid int, title varchar(24), deptid int)";
			SimpleDB.planner().executeUpdate(query, tx);
		}		
	    else if(tableName.equals("section")){
	    	query = "create table section(sectid int, courseid int, prof varchar(60), yearoffered int)";
	    	SimpleDB.planner().executeUpdate(query, tx);
	    }	    	
	    else if(tableName.equals("enroll")){
	    	query = "create table enroll(eid int, studentid int, sectionid int, grade varchar(2))";
	    	SimpleDB.planner().executeUpdate(query, tx);
	    }
		
		tx.commit();
	}
	
	private void createExTableProposal_1(DefaultTableModel model, JTextField textField){
		textField.setText("450");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("sid");
		model.addColumn("sname");
		model.addColumn("gradyear");
		model.addColumn("majorid");
		
		model.addRow(new Object[]{"0", "8", "1960", "0"});
		model.addRow(new Object[]{"449", "60", "2009", "3"});
		model.addRow(new Object[]{"450", "450", "50", "4"});
	}
	
	private void createExTableProposal_2(DefaultTableModel model, JTextField textField){
		textField.setText("4");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("did");
		model.addColumn("dname");
		
		model.addRow(new Object[]{"0", "2"});
		model.addRow(new Object[]{"3", "28"});
		model.addRow(new Object[]{"4", "4"});
	}
	
	private void createExTableProposal_3(DefaultTableModel model, JTextField textField){
		textField.setText("32");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("cid");
		model.addColumn("title");
		model.addColumn("deptid");
		
		model.addRow(new Object[]{"0", "2", "0"});
		model.addRow(new Object[]{"31", "24", "3"});
		model.addRow(new Object[]{"32", "32", "4"});
	}
	
	private void createExTableProposal_4(DefaultTableModel model, JTextField textField){
		textField.setText("250");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("sectid");
		model.addColumn("courseid");
		model.addColumn("prof");
		model.addColumn("yearoffered");
		
		model.addRow(new Object[]{"0", "0", "3", "1960"});
		model.addRow(new Object[]{"249", "31", "20", "2009"});
		model.addRow(new Object[]{"250", "32", "25", "50"});
	}
	
	private void createExTableProposal_5(DefaultTableModel model, JTextField textField){
		textField.setText("1500");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("eid");
		model.addColumn("sectionid");
		model.addColumn("studentid");
		model.addColumn("grade");
		
		model.addRow(new Object[]{"0", "0", "0", "2"});
		model.addRow(new Object[]{"1499", "249", "449", "2"});
		model.addRow(new Object[]{"1500", "250", "450", "14"});
	}

}
