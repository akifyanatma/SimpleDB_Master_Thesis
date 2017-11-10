package simpledb.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.record.RecordFile;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.server.Startup;
import simpledb.tx.Transaction;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;

import java.awt.Component;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JMenuItem;
import javax.swing.Box;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollBar;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.swing.JMenu;


public class GUI {

	private JFrame frame;
	private JMenuBar menuBar;
		
	private ArrayList<JPanel> panels;
	
	private JPanel startupPanel;
	private JPanel queryPanel;
	private JPanel loadTablePanel;
	private JPanel loadTablePanel_1;
	private JPanel viewTablePanel;
	private JPanel viewTablePanel_1;
	private JPanel tableMetaDataPanel;
	private JPanel statisticMetaDataPanel;
	private JPanel exDbPanel;	
	
	private DefaultTableModel queryResultModel;
	private DefaultTableModel loadTableModel;
	private DefaultTableModel exTableModel_1;
	private DefaultTableModel exTableModel_2;
	private DefaultTableModel exTableModel_3;
	private DefaultTableModel exTableModel_4;
	private DefaultTableModel exTableModel_5;
	private DefaultTableModel viewTableModel;
	private DefaultTableModel tblCatModel;
	private DefaultTableModel fldCatModel;
	private DefaultTableModel statModel;
	
	private JScrollPane queryResultTableScrollPane;
	private JScrollPane queryTreeScrollPane;
	private JScrollPane loadTableScrollPane;
	private JScrollPane exDbScrollPane;
	private JScrollPane viewTableScrollPane;
	
	private JTree queryTree;
	
	private JTextField queryTextField;
	private JTextField loadTableNameTextField;
	private JTextField loadTableRecordCountTextField;
	private JTextField exDbRecCountTextField_1;
	private JTextField exDbRecCountTextField_2;
	private JTextField exDbRecCountTextField_3;
	private JTextField exDbRecCountTextField_4;
	private JTextField exDbRecCountTextField_5;
	
	private JLabel queryWarningLabel;
	private JLabel resultTableLabel;
	private JLabel queryTreeLabel;
	private JLabel loadTableNameLabel_1;
	private JLabel loadTableWarningLabel_1;
	private JLabel loadTableMinValueLabel;
	private JLabel loadTableMaxValueLabel;
	private JLabel loadTableDistValueLabel;
	private JLabel loadTableWarningLabel_2;
	private JLabel loadTableNameLabel_2;
	private JLabel loadTableRecordCountLabel;
	private JLabel exDbWarningLabel_1;
	private JLabel exDbWarningLabel_2;
	private JLabel exDbWarningLabel_3;
	private JLabel exDbWarningLabel_4;
	private JLabel exDbWarningLabel_5;
	private JLabel viewTableWarningLabel;
	private JLabel viewTableNameLabel_2;
	private JLabel viewTableBlockCountLabel;
	private JLabel viewTableRecordCountLabel;
	
	private ExampleDb exampleDB;
	private JTextField viewTableNameTextField;
	private JTable viewTable;
	private JTable tblCatTable;
	private JTable fldCatTable;
	private JTable statisticTable;
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					GUI window = new GUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("SimpleDB");
		//frame.setBounds(100, 100, 1024, 1240);
		frame.setBounds(100, 100, 1024, 768);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));	
			
		panels = new ArrayList<>();
		exampleDB = new ExampleDb();
		
		initializeMenuBar();
		initializeStartupPanel();
		initializeQueryPanel();
		initializeLoadTablePanel();
		initializeViewTablePanel();
		initializeTableMetaDataPanel();
		initializeStatisticMetaDataPanel();
		initializeExDbLoadPanel();
	}
	
	private void initializeMenuBar(){
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setVisible(false);
		
		JMenuItem queryPanelMenuItem = new JMenuItem("Query");
		queryPanelMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editQueryPanel();
				setVisiblePanel(queryPanel);
			}
		});
		menuBar.add(queryPanelMenuItem);
		
		JMenuItem loadTableMenuItem = new JMenuItem("Load Table");
		loadTableMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisiblePanel(loadTablePanel);
				editLoadTablePanel();
			}
		});
		menuBar.add(loadTableMenuItem);
		
		JMenuItem exDBMenuItem = new JMenuItem("Example DB Load");
		exDBMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exDbScrollPane.setVisible(true);
				exDbPanel.setVisible(true);	
				setVisiblePanel(exDbPanel);
				editExDbLoadPanel();
			}
		});
		menuBar.add(exDBMenuItem);
		
		JMenuItem viewTableMenuItem = new JMenuItem("View Table");
		viewTableMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisiblePanel(viewTablePanel);
				editViewTablePanel();
			}
		});
		menuBar.add(viewTableMenuItem);
		
		JMenu metaDataMenu = new JMenu("Metadata");
		menuBar.add(metaDataMenu);
		
		JMenuItem tableMetaDataMenuItem = new JMenuItem("Table Metadata");
		tableMetaDataMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisiblePanel(tableMetaDataPanel);
				getTableMetadata();
			}
		});
		metaDataMenu.add(tableMetaDataMenuItem);
		
		JMenuItem statisticMetaDataMenuItem = new JMenuItem("Statistic Metadata");
		statisticMetaDataMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisiblePanel(statisticMetaDataPanel);
				getStatisticMetadata();
			}
		});
		metaDataMenu.add(statisticMetaDataMenuItem);
	}
	
	private void initializeStartupPanel(){		
		startupPanel = new JPanel();
		startupPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.getContentPane().add(startupPanel, "name_780862283539419");
		startupPanel.setLayout(null);
		panels.add(startupPanel);
		
		JTextPane startupTextPane = new JTextPane();
		startupTextPane.setEditable(false);
		startupTextPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startupTextPane.setText("You are now in the process of starting the system. After entering the database name, the system will start. Please, enter database name.");
		startupTextPane.setBackground(UIManager.getColor("Button.background"));
		startupTextPane.setBounds(220, 262, 615, 38);
		startupPanel.add(startupTextPane);
		
		JLabel startupDBNameLbl = new JLabel(" DB Name:");
		startupDBNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startupDBNameLbl.setBounds(220, 311, 67, 24);
		startupPanel.add(startupDBNameLbl);
		
		JTextField startupDBNameTextField = new JTextField();
		startupDBNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startupDBNameTextField.setBounds(290, 310, 299, 31);
		startupPanel.add(startupDBNameTextField);
		startupDBNameTextField.setColumns(10);
		
		JLabel startupReminderLabel = new JLabel(" Please, enter database name.");
		startupReminderLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startupReminderLabel.setBounds(220, 357, 287, 14);
		startupPanel.add(startupReminderLabel);
		startupReminderLabel.setVisible(false);
		
		JButton startupOkButton = new JButton("OK");
		startupOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dbName = startupDBNameTextField.getText();
				if(dbName.length() == 0)
					startupReminderLabel.setVisible(true);
				else{
					try {
						new Startup(dbName);
						menuBar.setVisible(true);
						setVisiblePanel(queryPanel);
						//Program ilk basta acilirken query panel sayfasi query menu itemine tiklanmadan acilmaktadir.
						//Query sayfasi komponenetlerinin gorunurlugu query menu itemine tiklaninca duzenleniyordu. 
						//Bu yuzden gerekli duzenlemeleri yapmak icin asagidaki fonksiyonlar cagirilmaktadir.
						setVisibilitySelectComponents(false);
						queryWarningLabel.setVisible(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startupReminderLabel.setVisible(false);
				}
			}
		});
		startupOkButton.setBounds(500, 354, 89, 23);
		startupPanel.add(startupOkButton);
	}
		
	private void initializeQueryPanel(){
		queryPanel = new JPanel();
		queryPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.getContentPane().add(queryPanel, "name_947200022734033");
		queryPanel.setLayout(null);
		panels.add(queryPanel);
		
		JLabel querLabel = new JLabel("Query:");
		querLabel.setBounds(20, 21, 966, 24);
		querLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(querLabel);
		
		queryTextField = new JTextField();
		queryTextField.setBounds(68, 21, 918, 24);
		queryTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(queryTextField);
		queryTextField.setColumns(10);
		
		queryWarningLabel = new JLabel("Warning!");
		queryWarningLabel.setBounds(68, 56, 212, 24);
		queryWarningLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(queryWarningLabel);
		
		JButton queryConfirmButton = new JButton("OK");
		queryConfirmButton.setBounds(897, 56, 89, 24);
		queryConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = queryTextField.getText();
				if(query.length() == 0){
					queryWarningLabel.setText("Please, enter query.");
					queryWarningLabel.setVisible(true);
				}
				else{
					handQuery(query);
				}					
			}
		});
		queryConfirmButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(queryConfirmButton);
				
		queryResultModel = new DefaultTableModel();
		JTable queryResultTable = new JTable(queryResultModel);
		queryResultTable.setBounds(0, 0, 1, 1);
		queryPanel.add(queryResultTable);
		
		queryResultTableScrollPane = new JScrollPane(queryResultTable);
		queryResultTableScrollPane.setBounds(20, 131, 475, 562);
		queryPanel.add(queryResultTableScrollPane);
		
		queryTreeScrollPane = new JScrollPane();
		queryTreeScrollPane.setAlignmentX(5.0f);
		queryTreeScrollPane.setBounds(511, 131, 475, 562);
		queryPanel.add(queryTreeScrollPane);
		
		queryTree = new JTree();
		queryTree.setVisibleRowCount(100);
		queryTree.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryTreeScrollPane.setViewportView(queryTree);
		
		resultTableLabel = new JLabel("Result Table");
		resultTableLabel.setBounds(20, 106, 143, 24);
		resultTableLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(resultTableLabel);
		
		queryTreeLabel = new JLabel("Query Tree");
		queryTreeLabel.setBounds(511, 106, 188, 24);
		queryTreeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryPanel.add(queryTreeLabel);
	}
	
	private void initializeLoadTablePanel(){
		loadTablePanel = new JPanel();
		loadTablePanel.setAlignmentY(5.0f);
		loadTablePanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.getContentPane().add(loadTablePanel, "name_958352667584731");		
		loadTablePanel.setLayout(null);
		panels.add(loadTablePanel);
		
		loadTablePanel_1 = new JPanel(); //panels listesine eklenmiyor cunku bu panel loadTablePanel icinde yer almaktadir.
		loadTablePanel_1.setBounds(0, 175, 1008, 310);
		loadTablePanel.add(loadTablePanel_1);
		loadTablePanel_1.setLayout(null);
		
		//Ok buttonu tiklandikdan sonra kullanildigi icin daha once create edilmesi gerekir.
		loadTableModel = new DefaultTableModel();
		
		JTable loadTable = new JTable(loadTableModel);
		loadTable.setRowHeight(20);
		loadTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTable.setBounds(0, 0, 1, 1);
		loadTablePanel.add(loadTable);
		
		loadTableScrollPane = new JScrollPane(loadTable);
		loadTableScrollPane.setBounds(117, 103, 867, 83);
		loadTablePanel_1.add(loadTableScrollPane);
		loadTableScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableNameLabel_1 = new JLabel("Table Name:");
		loadTableNameLabel_1.setBounds(47, 23, 83, 24);
		loadTableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTablePanel.add(loadTableNameLabel_1);
		
		loadTableNameTextField = new JTextField();
		loadTableNameTextField.setBounds(131, 26, 250, 24);
		loadTableNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTablePanel.add(loadTableNameTextField);
		loadTableNameTextField.setColumns(10);
				
		loadTableWarningLabel_1 = new JLabel("Warning!");
		loadTableWarningLabel_1.setBounds(408, 24, 446, 24);
		loadTableWarningLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTablePanel.add(loadTableWarningLabel_1);
					
		loadTableNameLabel_2 = new JLabel("Table Name");
		loadTableNameLabel_2.setBounds(117, 67, 238, 24);
		loadTablePanel_1.add(loadTableNameLabel_2);
		loadTableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableRecordCountLabel = new JLabel("Record Count:");
		loadTableRecordCountLabel.setBounds(410, 67, 103, 24);
		loadTablePanel_1.add(loadTableRecordCountLabel);
		loadTableRecordCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableRecordCountTextField = new JTextField();
		loadTableRecordCountTextField.setBounds(506, 68, 86, 24);
		loadTablePanel_1.add(loadTableRecordCountTextField);
		loadTableRecordCountTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTableRecordCountTextField.setColumns(10);
		
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		loadTableRecordCountTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
					
		loadTableWarningLabel_2 = new JLabel("Warning!");
		loadTableWarningLabel_2.setBounds(117, 197, 709, 24);
		loadTablePanel_1.add(loadTableWarningLabel_2);
		loadTableWarningLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableDistValueLabel = new JLabel("Distinct Value");
		loadTableDistValueLabel.setBounds(33, 167, 103, 24);
		loadTablePanel_1.add(loadTableDistValueLabel);
		loadTableDistValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableMaxValueLabel = new JLabel("Max Value");
		loadTableMaxValueLabel.setBounds(33, 143, 103, 24);
		loadTablePanel_1.add(loadTableMaxValueLabel);
		loadTableMaxValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		loadTableMinValueLabel = new JLabel("Min Value");
		loadTableMinValueLabel.setBounds(33, 120, 103, 24);
		loadTablePanel_1.add(loadTableMinValueLabel);
		loadTableMinValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));	
		
		JButton loadTableOkButton = new JButton("Ok");
		loadTableOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tableName = loadTableNameTextField.getText();
				if(tableName.length() == 0){
					loadTableWarningLabel_1.setText("Please enter table name.");
					loadTableWarningLabel_1.setVisible(true);
				}
				else if(getColumnNames(tableName, loadTableModel) == false){
					loadTableWarningLabel_1.setText("\""+tableName+"\" table is not exist in database.");
					loadTableWarningLabel_1.setVisible(true);
				}
				else{
					editLoadTablePanel_1();
					createTableSketch(tableName, loadTableModel);
					loadTableWarningLabel_1.setVisible(false);
					loadTableNameLabel_2.setText(tableName);
					loadTablePanel_1.setVisible(true);
				}
			}
		});
		loadTableOkButton.setBounds(292, 61, 89, 24);
		loadTableOkButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loadTablePanel.add(loadTableOkButton);
		
		JButton loadTableLoadButton = new JButton("Load");
		loadTableLoadButton.setBounds(895, 197, 89, 24);
		loadTablePanel_1.add(loadTableLoadButton);
		loadTableLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String recordCountText = loadTableRecordCountTextField.getText();
				if(recordCountText.length() == 0){
					loadTableWarningLabel_2.setText("Record count is missed. Please enter record count.");
					loadTableWarningLabel_2.setVisible(true);
				}
				else if(isThereEmptyCell(loadTableModel)){
					loadTableWarningLabel_2.setText("Missed information. Please fill all cell.");
					loadTableWarningLabel_2.setVisible(true);
				}
				else{
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = loadTableNameLabel_2.getText();
					loadTableWarningLabel_2.setVisible(false);
					generateFieldValues(tableName, recordCount, loadTableModel);				
				}				
			}
		});
		loadTableLoadButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
	}
	
	private void initializeViewTablePanel(){
		viewTablePanel = new JPanel();
		frame.getContentPane().add(viewTablePanel, "name_1042902942985250");	
		viewTablePanel.setLayout(null);
		panels.add(viewTablePanel);
		
		viewTablePanel_1 = new JPanel();
		viewTablePanel_1.setBounds(0, 97, 1008, 608);
		viewTablePanel.add(viewTablePanel_1);
		viewTablePanel_1.setLayout(null);
		
		viewTableModel = new DefaultTableModel();
		
		viewTable = new JTable(viewTableModel);
		viewTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewTable.setRowHeight(20);
		viewTable.setBounds(0, 0, 1, 1);
		viewTablePanel.add(viewTable);
		
		viewTableScrollPane = new JScrollPane(viewTable);
		viewTableScrollPane.setBounds(37, 50, 942, 518);
		viewTablePanel_1.add(viewTableScrollPane);
		
		viewTableNameLabel_2 = new JLabel("Table Name");
		viewTableNameLabel_2.setBounds(37, 15, 247, 24);
		viewTablePanel_1.add(viewTableNameLabel_2);
		viewTableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		viewTableBlockCountLabel = new JLabel("Block Count:");
		viewTableBlockCountLabel.setBounds(294, 15, 132, 24);
		viewTablePanel_1.add(viewTableBlockCountLabel);
		viewTableBlockCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		viewTableRecordCountLabel = new JLabel("Record Count:");
		viewTableRecordCountLabel.setBounds(504, 15, 193, 24);
		viewTablePanel_1.add(viewTableRecordCountLabel);
		viewTableRecordCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel viewTableNameLabel_1 = new JLabel("Table Name:");
		viewTableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewTableNameLabel_1.setBounds(34, 27, 87, 24);
		viewTablePanel.add(viewTableNameLabel_1);
		
		viewTableWarningLabel = new JLabel("Warning!");
		viewTableWarningLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewTableWarningLabel.setBounds(373, 27, 625, 24);
		viewTablePanel.add(viewTableWarningLabel);
		
		viewTableNameTextField = new JTextField();
		viewTableNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		viewTableNameTextField.setBounds(121, 27, 230, 24);
		viewTablePanel.add(viewTableNameTextField);
		viewTableNameTextField.setColumns(10);
				
		JButton viewTableOkButton = new JButton("Ok");
		viewTableOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String tableName = viewTableNameTextField.getText();
//				if(tableName.length() == 0){
//					viewTableWarningLabel.setText("Please enter table name.");
//					viewTableWarningLabel.setVisible(true);
//				}
//				else if(getColumnNames(tableName, viewTableModel) == false){
//					viewTableWarningLabel.setText("\""+tableName+"\" table is not exist in database.");
//					viewTableWarningLabel.setVisible(true);
//				}
//				else{
//					viewTablePanel_1.setVisible(true);
//					viewTableNameLabel_2.setText(tableName);
//					viewTableWarningLabel.setVisible(false);					
//					getTableRecords(tableName, viewTableModel);
//				}		
				
				String tableName = viewTableNameTextField.getText();
				if(tableName.length() == 0){
					viewTableWarningLabel.setText("Please enter table name.");
					viewTableWarningLabel.setVisible(true);
				}
				else {
					boolean thereIsTable = getColumnNames(tableName, viewTableModel);
					if(thereIsTable == false){
						viewTableWarningLabel.setText("\""+tableName+"\" table is not exist in database.");
						viewTableWarningLabel.setVisible(true);
					}
					else{
						viewTablePanel_1.setVisible(true);
						viewTableNameLabel_2.setText(tableName);
						viewTableWarningLabel.setVisible(false);					
						getTableRecords(tableName, viewTableModel);
					}
				}
			}
		});
		viewTableOkButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewTableOkButton.setBounds(262, 62, 89, 24);
		viewTablePanel.add(viewTableOkButton);
	}
		
	private void initializeTableMetaDataPanel(){		
		tableMetaDataPanel = new JPanel();
		frame.getContentPane().add(tableMetaDataPanel, "name_1071713855832394");
		panels.add(tableMetaDataPanel);
		tableMetaDataPanel.setLayout(null);
		
		tblCatModel = new DefaultTableModel();
		tblCatModel.addColumn("TblName");
		tblCatModel.addColumn("RecLength");
		
		tblCatTable = new JTable(tblCatModel);
		tblCatTable.setRowHeight(20);
		tblCatTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tblCatTable.setBounds(0, 0, 1, 1);
		tableMetaDataPanel.add(tblCatTable);
		
		JScrollPane tblCatScrollPane = new JScrollPane(tblCatTable);
		tblCatScrollPane.setBounds(24, 51, 300, 631);
		tableMetaDataPanel.add(tblCatScrollPane);
			
		fldCatModel = new DefaultTableModel();
		fldCatModel.addColumn("TblName");
		fldCatModel.addColumn("FldName");
		fldCatModel.addColumn("Type");
		fldCatModel.addColumn("Length");
		fldCatModel.addColumn("Offset");
		
		fldCatTable = new JTable(fldCatModel);
		fldCatTable.setRowHeight(20);
		fldCatTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fldCatTable.setBounds(0, 0, 1, 1);
		tableMetaDataPanel.add(fldCatTable);
		
		JScrollPane fldCatScrollPane = new JScrollPane(fldCatTable);
		fldCatScrollPane.setBounds(358, 51, 627, 631);
		tableMetaDataPanel.add(fldCatScrollPane);
		
		JLabel tblCatLabel = new JLabel("Table Catalog");
		tblCatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tblCatLabel.setBounds(24, 24, 203, 24);
		tableMetaDataPanel.add(tblCatLabel);
		
		JLabel fldCatLabel = new JLabel("Field Catalog");
		fldCatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fldCatLabel.setBounds(358, 24, 381, 24);
		tableMetaDataPanel.add(fldCatLabel);
	}
	
	private void initializeStatisticMetaDataPanel(){
		statisticMetaDataPanel = new JPanel();
		frame.getContentPane().add(statisticMetaDataPanel, "name_1071783750247772");
		panels.add(statisticMetaDataPanel);
		statisticMetaDataPanel.setLayout(null);
		
		JLabel statisticMetaDataLabel = new JLabel("Statistics");
		statisticMetaDataLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statisticMetaDataLabel.setBounds(20, 25, 115, 24);
		statisticMetaDataPanel.add(statisticMetaDataLabel);
		
		statModel = new DefaultTableModel();
		statModel.addColumn("T");
		statModel.addColumn("B(T)");
		statModel.addColumn("R(T)");
		statModel.addColumn("V(T, F)");
		
		statisticTable = new JTable(statModel);
		statisticTable.setBounds(0, 0, 1, 1);
		statisticMetaDataPanel.add(statisticTable);
		
		JScrollPane statisticMetaDataScrollPane = new JScrollPane(statisticTable);
		statisticMetaDataScrollPane.setBounds(20, 60, 964, 617);
		statisticMetaDataPanel.add(statisticMetaDataScrollPane);
	}
	
	private void initializeExDbLoadPanel(){		
		exDbPanel = new JPanel(){
	        @Override
	        public Dimension getPreferredSize() {
	            return new Dimension(980, 860);
	        }
	    };
		frame.getContentPane().add(exDbPanel, "name_983354196760041");
		panels.add(exDbPanel);
		
		exDbScrollPane = new JScrollPane(exDbPanel);
		frame.getContentPane().add(exDbScrollPane, "name_1001070937128038");
		exDbPanel.setLayout(null);
				
		JLabel exDbTableNameLabel_1 = new JLabel("Table Name:");
		exDbTableNameLabel_1.setBounds(117, 22, 89, 24);
		exDbTableNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_1);
		
		JLabel exDbTableNameLabel_2 = new JLabel("Table Name:");
		exDbTableNameLabel_2.setBounds(117, 188, 89, 24);
		exDbTableNameLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_2);
		
		JLabel exDbTableNameLabel_3 = new JLabel("Table Name:");
		exDbTableNameLabel_3.setBounds(117, 358, 89, 24);
		exDbTableNameLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_3);
		
		JLabel exDbTableNameLabel_4 = new JLabel("Table Name:");
		exDbTableNameLabel_4.setBounds(117, 524, 89, 24);
		exDbTableNameLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_4);
		
		JLabel exDbTableNameLabel_5 = new JLabel("Table Name:");
		exDbTableNameLabel_5.setBounds(117, 696, 89, 24);
		exDbTableNameLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_5);
		
		JLabel exDbTableNameLabel_1_1 = new JLabel("student");
		exDbTableNameLabel_1_1.setBounds(198, 22, 89, 24);
		exDbTableNameLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_1_1);
		
		JLabel exDbTableNameLabel_2_1 = new JLabel("dept");
		exDbTableNameLabel_2_1.setBounds(198, 188, 89, 24);
		exDbTableNameLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_2_1);
		
		JLabel exDbTableNameLabel_3_1 = new JLabel("course");
		exDbTableNameLabel_3_1.setBounds(198, 358, 89, 24);
		exDbTableNameLabel_3_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_3_1);
		
		JLabel exDbTableNameLabel_4_1 = new JLabel("section");
		exDbTableNameLabel_4_1.setBounds(198, 524, 89, 24);
		exDbTableNameLabel_4_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_4_1);
		
		JLabel exDbTableNameLabel_5_1 = new JLabel("enroll");
		exDbTableNameLabel_5_1.setBounds(198, 696, 89, 24);
		exDbTableNameLabel_5_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTableNameLabel_5_1);
		
		JLabel exDbRecordCountLabel_1 = new JLabel("Record Count:");
		exDbRecordCountLabel_1.setBounds(374, 22, 103, 24);
		exDbRecordCountLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbRecordCountLabel_1);
		
		JLabel exDbRecordCountLabel_2 = new JLabel("Record Count:");
		exDbRecordCountLabel_2.setBounds(374, 188, 103, 24);
		exDbRecordCountLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbRecordCountLabel_2);
		
		JLabel exDbRecordCountLabel_3 = new JLabel("Record Count:");
		exDbRecordCountLabel_3.setBounds(374, 358, 103, 24);
		exDbRecordCountLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbRecordCountLabel_3);
		
		JLabel exDbRecordCountLabel_4 = new JLabel("Record Count:");
		exDbRecordCountLabel_4.setBounds(374, 524, 103, 24);
		exDbRecordCountLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbRecordCountLabel_4);
		
		JLabel exDbRecordCountLabel_5 = new JLabel("Record Count:");
		exDbRecordCountLabel_5.setBounds(374, 696, 103, 24);
		exDbRecordCountLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbRecordCountLabel_5);
		
		JLabel exDbMinValLabel_1 = new JLabel("Min Value");
		exDbMinValLabel_1.setBounds(20, 68, 91, 24);
		exDbMinValLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMinValLabel_1);
		
		JLabel exDbMinValLabel_2 = new JLabel("Min Value");
		exDbMinValLabel_2.setBounds(20, 231, 91, 24);
		exDbMinValLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMinValLabel_2);
		
		JLabel exDbMinValLabel_3 = new JLabel("Min Value");
		exDbMinValLabel_3.setBounds(20, 397, 91, 24);
		exDbMinValLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMinValLabel_3);
		
		JLabel exDbMinValLabel_4 = new JLabel("Min Value");
		exDbMinValLabel_4.setBounds(20, 565, 91, 24);
		exDbMinValLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMinValLabel_4);
		
		JLabel exDbMinValLabel_5 = new JLabel("Min Value");
		exDbMinValLabel_5.setBounds(20, 740, 91, 24);
		exDbMinValLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMinValLabel_5);
		
		JLabel exDbMaxValLabel_1 = new JLabel("Max Value");
		exDbMaxValLabel_1.setBounds(20, 89, 91, 24);
		exDbMaxValLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMaxValLabel_1);
		
		JLabel exDbMaxValLabel_2 = new JLabel("Max Value");
		exDbMaxValLabel_2.setBounds(20, 254, 91, 24);
		exDbMaxValLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMaxValLabel_2);
		
		JLabel exDbMaxValLabel_3 = new JLabel("Max Value");
		exDbMaxValLabel_3.setBounds(20, 418, 91, 24);
		exDbMaxValLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMaxValLabel_3);
		
		JLabel exDbMaxValLabel_4 = new JLabel("Max Value");
		exDbMaxValLabel_4.setBounds(20, 587, 91, 24);
		exDbMaxValLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMaxValLabel_4);
		
		JLabel exDbMaxValLabel_5 = new JLabel("Max Value");
		exDbMaxValLabel_5.setBounds(20, 763, 91, 24);
		exDbMaxValLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbMaxValLabel_5);
		
		JLabel exDbDistValLabel_1 = new JLabel("Distinct Value");
		exDbDistValLabel_1.setBounds(20, 109, 91, 24);
		exDbDistValLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbDistValLabel_1);
		
		JLabel exDbDistValLabel_2 = new JLabel("Distinct Value");
		exDbDistValLabel_2.setBounds(20, 276, 91, 24);
		exDbDistValLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbDistValLabel_2);
		
		JLabel exDbDistValLabel_3 = new JLabel("Distinct Value");
		exDbDistValLabel_3.setBounds(20, 442, 91, 24);
		exDbDistValLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbDistValLabel_3);
		
		JLabel exDbDistValLabel_4 = new JLabel("Distinct Value");
		exDbDistValLabel_4.setBounds(20, 610, 91, 24);
		exDbDistValLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbDistValLabel_4);
		
		JLabel exDbDistValLabel_5 = new JLabel("Distinct Value");
		exDbDistValLabel_5.setBounds(20, 786, 91, 24);
		exDbDistValLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbDistValLabel_5);
		
		exDbWarningLabel_1 = new JLabel("Warning!");
		exDbWarningLabel_1.setBounds(117, 140, 716, 24);
		exDbWarningLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbWarningLabel_1);
		
		exDbWarningLabel_2 = new JLabel("Warning!");
		exDbWarningLabel_2.setBounds(117, 306, 716, 24);
		exDbWarningLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbWarningLabel_2);
		
		exDbWarningLabel_3 = new JLabel("Warning!");
		exDbWarningLabel_3.setBounds(117, 475, 716, 24);
		exDbWarningLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbWarningLabel_3);
		
		exDbWarningLabel_4 = new JLabel("Warning!");
		exDbWarningLabel_4.setBounds(117, 643, 716, 24);
		exDbWarningLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbWarningLabel_4);
		
		exDbWarningLabel_5 = new JLabel("Warning!");
		exDbWarningLabel_5.setBounds(117, 817, 716, 24);
		exDbWarningLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbWarningLabel_5);
		
		exDbRecCountTextField_1 = new JTextField();
		exDbRecCountTextField_1.setBounds(459, 22, 89, 24);
		exDbRecCountTextField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbRecCountTextField_1.setColumns(10);
		exDbPanel.add(exDbRecCountTextField_1);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		exDbRecCountTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		exDbRecCountTextField_2 = new JTextField();
		exDbRecCountTextField_2.setBounds(459, 189, 89, 24);
		exDbRecCountTextField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbRecCountTextField_2.setColumns(10);
		exDbPanel.add(exDbRecCountTextField_2);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		exDbRecCountTextField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		exDbRecCountTextField_3 = new JTextField();
		exDbRecCountTextField_3.setBounds(459, 359, 89, 24);
		exDbRecCountTextField_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbRecCountTextField_3.setColumns(10);
		exDbPanel.add(exDbRecCountTextField_3);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		exDbRecCountTextField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		exDbRecCountTextField_4 = new JTextField();
		exDbRecCountTextField_4.setBounds(459, 525, 89, 24);
		exDbRecCountTextField_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbRecCountTextField_4.setColumns(10);
		exDbPanel.add(exDbRecCountTextField_4);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		exDbRecCountTextField_4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		exDbRecCountTextField_5 = new JTextField();
		exDbRecCountTextField_5.setBounds(459, 697, 89, 24);
		exDbRecCountTextField_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbRecCountTextField_5.setColumns(10);
		exDbPanel.add(exDbRecCountTextField_5);
		//Kayit sayisi olarak sadece integer deger girilmesini sagliyor
		exDbRecCountTextField_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
		        }
			}
		});
		
		exTableModel_1 = new DefaultTableModel();
		JTable exDbTable_1 = new JTable(exTableModel_1);
		exDbTable_1.setBounds(0, 0, 871, 0);
		exDbTable_1.setRowHeight(20);
		exDbTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTable_1);
		
		exTableModel_2 = new DefaultTableModel();
		JTable exDbTable_2 = new JTable(exTableModel_2);
		exDbTable_2.setBounds(0, 0, 871, 0);
		exDbTable_2.setRowHeight(20);
		exDbTable_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTable_2);
		
		exTableModel_3 = new DefaultTableModel();
		JTable exDbTable_3 = new JTable(exTableModel_3);
		exDbTable_3.setBounds(0, 0, 871, 0);
		exDbTable_3.setRowHeight(20);
		exDbTable_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTable_3);
		
		exTableModel_4 = new DefaultTableModel();
		JTable exDbTable_4 = new JTable(exTableModel_4);
		exDbTable_4.setBounds(0, 0, 871, 0);
		exDbTable_4.setRowHeight(20);
		exDbTable_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTable_4);
		
		exTableModel_5 = new DefaultTableModel();
		JTable exDbTable_5 = new JTable(exTableModel_5);
		exDbTable_5.setBounds(0, 0, 871, 0);
		exDbTable_5.setRowHeight(20);
		exDbTable_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbTable_5);
		
		JScrollPane exDbScrollPane_1 = new JScrollPane(exDbTable_1);
		exDbScrollPane_1.setBounds(107, 50, 875, 83);
		exDbScrollPane_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbScrollPane_1);
		
		JScrollPane exDbScrollPane_2 = new JScrollPane(exDbTable_2);
		exDbScrollPane_2.setBounds(107, 217, 873, 83);
		exDbScrollPane_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbScrollPane_2);
		
		JScrollPane exDbScrollPane_3 = new JScrollPane(exDbTable_3);
		exDbScrollPane_3.setBounds(107, 387, 873, 83);
		exDbScrollPane_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbScrollPane_3);
		
		JScrollPane exDbScrollPane_4 = new JScrollPane(exDbTable_4);
		exDbScrollPane_4.setBounds(107, 554, 873, 83);
		exDbScrollPane_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbScrollPane_4);
		
		JScrollPane exDbScrollPane_5 = new JScrollPane(exDbTable_5);
		exDbScrollPane_5.setBounds(107, 727, 873, 83);
		exDbScrollPane_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbScrollPane_5);
			
		JButton exDbLoadButton_1 = new JButton("Load");
		exDbLoadButton_1.setBounds(893, 140, 89, 24);
		exDbLoadButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String recordCountText = exDbRecCountTextField_1.getText();
				if(recordCountText.length() == 0){
					exDbWarningLabel_1.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(exTableModel_1)){
					exDbWarningLabel_1.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = exDbTableNameLabel_1_1.getText();
					exampleDB.createTable(tableName);
					generateFieldValues(tableName, recordCount, exTableModel_1);
					exDbWarningLabel_1.setText("\" "+tableName+ " \" is created.");
				}
				exDbWarningLabel_1.setVisible(true);
			}
		});
		exDbLoadButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbLoadButton_1);
		
		JButton exDbLoadButton_2 = new JButton("Load");
		exDbLoadButton_2.setBounds(893, 306, 89, 24);
		exDbLoadButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = exDbRecCountTextField_2.getText();
				if(recordCountText.length() == 0){
					exDbWarningLabel_2.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(exTableModel_2)){
					exDbWarningLabel_2.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = exDbTableNameLabel_2_1.getText();
					exampleDB.createTable(tableName);
					generateFieldValues(tableName, recordCount, exTableModel_2);
					exDbWarningLabel_2.setText("\" "+tableName+ " \" is created.");
				}
				exDbWarningLabel_2.setVisible(true);
			}
		});
		exDbLoadButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbLoadButton_2);
		
		JButton exDbLoadButton_3 = new JButton("Load");
		exDbLoadButton_3.setBounds(893, 475, 89, 24);
		exDbLoadButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = exDbRecCountTextField_3.getText();
				if(recordCountText.length() == 0){
					exDbWarningLabel_3.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(exTableModel_3)){
					exDbWarningLabel_3.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = exDbTableNameLabel_3_1.getText();
					exampleDB.createTable(tableName);
					generateFieldValues(tableName, recordCount, exTableModel_3);
					exDbWarningLabel_3.setText("\" "+tableName+ " \" is created.");
				}
				exDbWarningLabel_3.setVisible(true);
			}
		});
		exDbLoadButton_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbLoadButton_3);
		
		JButton exDbLoadButton_4 = new JButton("Load");
		exDbLoadButton_4.setBounds(893, 643, 89, 24);
		exDbLoadButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = exDbRecCountTextField_4.getText();
				if(recordCountText.length() == 0){
					exDbWarningLabel_4.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(exTableModel_4)){
					exDbWarningLabel_4.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = exDbTableNameLabel_4_1.getText();
					exampleDB.createTable(tableName);
					generateFieldValues(tableName, recordCount, exTableModel_4);
					exDbWarningLabel_4.setText("\" "+tableName+ " \" is created.");
				}
				exDbWarningLabel_4.setVisible(true);
			}
		});
		exDbLoadButton_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbLoadButton_4);
		
		JButton exDbLoadButton_5 = new JButton("Load");
		exDbLoadButton_5.setBounds(893, 817, 89, 24);
		exDbLoadButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String recordCountText = exDbRecCountTextField_5.getText();
				if(recordCountText.length() == 0){
					exDbWarningLabel_5.setText("Please enter record count.");
				}
				else if(isThereEmptyCell(exTableModel_5)){
					exDbWarningLabel_5.setText("Missed information. Please fill all cell.");
				}
				else{				
					int recordCount = Integer.valueOf(recordCountText);
					String tableName = exDbTableNameLabel_5_1.getText();
					exampleDB.createTable(tableName);
					generateFieldValues(tableName, recordCount, exTableModel_5);
					exDbWarningLabel_5.setText("\" "+tableName+ " \" is created.");
				}
				exDbWarningLabel_5.setVisible(true);
			}
		});
		exDbLoadButton_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exDbPanel.add(exDbLoadButton_5);
	}
	
	private void getTableMetadata() {
		tblCatModel.setRowCount(0);
		fldCatModel.setRowCount(0);
		
		Transaction tx = new Transaction();
		ArrayList<TableInfo> tableInfos = SimpleDB.mdMgr().getAllTableInfo(tx);		
		
		for(int i=0; i<tableInfos.size();i++){
			TableInfo ti = tableInfos.get(i);
			String tblName = ti.fileName();
			int recLength = ti.recordLength();
			tblCatModel.addRow(new Object[]{tblName, recLength});
			
			Schema sch = ti.schema();
			for (String fld : sch.fields()) {
				int type = sch.type(fld);
				int lenght = sch.length(fld);
				int offset = ti.offset(fld);
				fldCatModel.addRow(new Object[]{tblName, fld, type, lenght, offset});
			}
		}
		
		tx.commit();
	}
	
	private void getStatisticMetadata(){
		statModel.setRowCount(0);
						
		Transaction tx = new Transaction();
		ArrayList<TableInfo> tableInfos = SimpleDB.mdMgr().getAllTableInfo(tx);	
		
		for(int i=0; i<tableInfos.size();i++){
			TableInfo ti = tableInfos.get(i);
			String tblName = ti.fileName();
			Schema sch = ti.schema();
			int B = SimpleDB.mdMgr().getStatInfo(tblName, ti, tx).blocksAccessed();
			int R = SimpleDB.mdMgr().getStatInfo(tblName, ti, tx).recordsOutput();
			
			ArrayList<String> distinctValues = new ArrayList<>();
			for (String fld : sch.fields()) {
				int distinctValue = SimpleDB.mdMgr().getStatInfo(tblName, ti, tx).distinctValues(fld);
				distinctValues.add(String.valueOf(distinctValue) + "     for "+fld);
			}
			
			statModel.addRow(new Object[]{tblName, B, R, distinctValues.get(0)});
			for(int j=1; j<distinctValues.size();j++){
				statModel.addRow(new Object[]{"", "","", distinctValues.get(j)});
			}
			
		}
			
		tx.commit();
	}
	
	//Bir tablonun kayitlarindaki field degerleri uretilir.
	private void generateFieldValues(String tableName, int recordCount, DefaultTableModel tableModel){
		Transaction tx = new Transaction();
		
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		
		Map<String, ArrayList<Integer>> allIntFieldValues = new HashMap<String, ArrayList<Integer>>();
		Map<String, ArrayList<String>> allStringFieldValues = new HashMap<String, ArrayList<String>>();
			
		for(int i=0; i<tableModel.getColumnCount();i++){
			String fld = tableModel.getColumnName(i);
					
			if(sch.type(fld) == 4){
				int minInterval = Integer.valueOf((String)tableModel.getValueAt(0, i));
				int maxInterval = Integer.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
				ArrayList<Integer> intFieldValues = generateIntField(minInterval, maxInterval, distinctValue, recordCount);
				allIntFieldValues.put(fld, intFieldValues);
				
			}
			else if(sch.type(fld) == 8){
				double minInterval = Double.valueOf((String)tableModel.getValueAt(0, i));
				double maxInterval = Double.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));	
				//Double type icin random generator function henuz yapilmadi.
			}
			else{
				int minLength = Integer.valueOf((String)tableModel.getValueAt(0, i));
				int maxLength = Integer.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
				if(sch.length(fld) < minLength)
					minLength = 1;
				if(sch.length(fld) < maxLength)
					maxLength = sch.length(fld);
				
				ArrayList<String> stringFieldValues = generateStringField(minLength, maxLength, distinctValue, recordCount);
				allStringFieldValues.put(fld, stringFieldValues);
			}
		}
		
		loadTable(ti, recordCount, allIntFieldValues, allStringFieldValues, tx);
		setDistinctValuesToMetadata(tableName, tableModel, tx);
		
		tx.commit();
	}
	
	//Olusturulan field degerleri tabloya eklenir
	private void loadTable(TableInfo ti, int recordCount, Map<String, ArrayList<Integer>> allIntFieldValues, Map<String, ArrayList<String>> allStringFieldValues, Transaction tx){
		Schema sch = ti.schema();
		
		RecordFile rf = new RecordFile(ti, tx);
        while (rf.next())
            rf.delete();
         rf.beforeFirst();
         
         for (int i=0; i<recordCount; i++) {
        	 rf.insert();
        	 for (String fld : sch.fields()) {
        		 if(sch.type(fld) == 4)
        			 rf.setInt(fld, allIntFieldValues.get(fld).remove(0));
        		 else
        			 rf.setString(fld, allStringFieldValues.get(fld).remove(0));     		 
        	 }
        	 
          }
          rf.close();
	}
	
	private void setDistinctValuesToMetadata(String tableName, DefaultTableModel tableModel, Transaction tx){
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		
		for(int i=0; i<tableModel.getColumnCount();i++){
			String fldName = tableModel.getColumnName(i);
			int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
			SimpleDB.mdMgr().setDistinctValue(tableName+".tbl", fldName, distinctValue, ti, tx);
		}
	}
	
	//Bir tablo doldurulurken butun hucrelerin doldurulup doldurulmadigini kontrol eder.
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
	
	private ArrayList<Integer> generateIntField(int minInterval, int maxInterval, int distinctValue, int recordCount){
		
		//NORMAL DAGILIM
//		//Verilen aralikta olabilecek tum sayilar
//		ArrayList<Integer> numbers = new ArrayList<>();
//		for(int i = minInterval; i<= maxInterval;i++)
//			numbers.add(i);
//		
//		//Distinct value sayilari belirleniyor
//		ArrayList<Integer> distinctNumbers = new ArrayList<>();		
//		for(int j=0; j<distinctValue; j++){			
//			Random r = new Random(); 
//			int rIndex = r.nextInt(numbers.size());
//			
//			int randomNumber = numbers.get(rIndex);
//			
//			numbers.remove(rIndex);
//			
//			distinctNumbers.add(randomNumber);		
//		}
//		
//		ArrayList<Integer> resultList = new ArrayList<>();
//		
//		//Sonuc listesinde en az bir kez butun distinct value sayilari bulunmali
//		resultList.addAll(distinctNumbers);
//		
//		//Sonuc listesi dolmadi ise rastgele distinct value sayilarindan secilerek ekleniyor.(normal dagilim)
//		for(int m=0; m<recordCount-distinctNumbers.size(); m++){
//			Random rn = new Random();
//			int rnIndex = rn.nextInt(distinctNumbers.size());
//			int rnNumber = distinctNumbers.get(rnIndex);
//			resultList.add(rnNumber);
//		}
//		
//		System.out.println("\n\n" + resultList.size() + "\n\n");
//		for(int y=0;y<resultList.size();y++)
//			System.out.println(resultList.get(y));
//		
//		return resultList;
		
		
		//UNIFORM DAGILIM
		//Verilen aralikta olabilecek tum sayilar
		ArrayList<Integer> numbers = new ArrayList<>();
		for(int i = minInterval; i<= maxInterval;i++)
			numbers.add(i);
		
		//Distinct value olacak sayilar belirleniyor
		ArrayList<Integer> distinctNumbers = new ArrayList<>();		
		for(int j=0; j<distinctValue; j++){			
			Random r = new Random(); 
			int rIndex = r.nextInt(numbers.size());
			
			int randomNumber = numbers.get(rIndex);
			
			numbers.remove(rIndex);
			
			distinctNumbers.add(randomNumber);		
		}
		
		
		//Record index siralarini karistirma
		ArrayList<Integer> sortedIndexList = new ArrayList<>();
		for(int k=0; k<recordCount; k++)
			sortedIndexList.add(k);
		
		ArrayList<Integer> mixedIndexList = new ArrayList<>();
		for(int m=0; m<recordCount; m++) {
			Random rn = new Random();
			int rnIndex = rn.nextInt(sortedIndexList.size());
			mixedIndexList.add(sortedIndexList.remove(rnIndex));
		}
		
		//Sonuclari olusturma
		HashMap<Integer, Integer> resultMap = new HashMap<>();
		for(int n=0; n<recordCount; n++) {
			int distincValueIndex = n%distinctNumbers.size();
			resultMap.put(mixedIndexList.get(n), distinctNumbers.get(distincValueIndex));			
		}
		
		ArrayList<Integer> resultList = new ArrayList<>();
		for(int key : resultMap.keySet())
			resultList.add(resultMap.get(key));
			
		System.out.println("\n\n" + resultList.size() + "\n\n");
		for(int y=0;y<resultList.size();y++)
			System.out.println(resultList.get(y));
		
		return resultList;		
	}
		
	private ArrayList<String> generateStringField(int minLength, int maxLength, int distinctValue, int recordCount){
		//NORMAL DAGILIM
//		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//	
//		ArrayList<String> distinctWords = new ArrayList<>();
//		
//		for(int j=0; j<distinctValue; j++){		
//			Random r = new Random();
//			int length = r.nextInt((maxLength - minLength) + 1) + minLength;
//			
//			Random rn = new Random();
//			String word = "";
//			for(int k=0; k<length; k++){			
//				int rnIndex = rn.nextInt(alphabet.length);
//				word+=alphabet[rnIndex];
//			}
//			
//			distinctWords.add(word);		
//		}
//		
//		ArrayList<String> resultList = new ArrayList<>();
//		
//		resultList.addAll(distinctWords);
//		
//		for(int n=0; n<recordCount-distinctWords.size(); n++){
//			Random rnd = new Random();
//			int rndIndex = rnd.nextInt(distinctWords.size());
//			
//			String rndName = distinctWords.get(rndIndex);
//			
//			resultList.add(rndName);
//		}
//		
//		System.out.println("\n\n" + resultList.size() + "\n\n");
//		for(int y=0;y<resultList.size();y++)
//			System.out.println(resultList.get(y));
//		
//		return resultList;
		
		
		//UNIFORM DAGILIM
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		//Distinct value olacak kelimeler belirleniyor
		ArrayList<String> distinctWords = new ArrayList<>();
		for(int j=0; j<distinctValue; j++){		
			Random r = new Random();
			int length = r.nextInt((maxLength - minLength) + 1) + minLength;
						
			String word = "";
			for(int k=0; k<length; k++){
				Random rn = new Random();
				int rnIndex = rn.nextInt(alphabet.length);
				word+=alphabet[rnIndex];
			}
			
			distinctWords.add(word);		
		}
		
		//Record index siralarini karistirma
		ArrayList<Integer> sortedIndexList = new ArrayList<>();
		for(int k=0; k<recordCount; k++)
			sortedIndexList.add(k);
		
		ArrayList<Integer> mixedIndexList = new ArrayList<>();
		for(int m=0; m<recordCount; m++) {
			Random rn = new Random();
			int rnIndex = rn.nextInt(sortedIndexList.size());
			mixedIndexList.add(sortedIndexList.remove(rnIndex));
		}
		
		//Sonuclari olusturma
		HashMap<Integer, String> resultMap = new HashMap<>();
		for(int n=0; n<recordCount; n++) {
			int distincValueIndex = n%distinctWords.size();
			resultMap.put(mixedIndexList.get(n), distinctWords.get(distincValueIndex));			
		}
		
		ArrayList<String> resultList = new ArrayList<>();
		for(int key : resultMap.keySet())
			resultList.add(resultMap.get(key));
			
		System.out.println("\n\n" + resultList.size() + "\n\n");
		for(int y=0;y<resultList.size();y++)
			System.out.println(resultList.get(y));
		
		return resultList;	
	}
	
	//Doldurulacak olan tablonun field isimlerini getirir
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
	
	private void getTableRecords(String tableName, DefaultTableModel tableModel){
		Transaction tx = new Transaction();
		
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		int B = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).blocksAccessed();
		int R = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).recordsOutput();
		
		viewTableBlockCountLabel.setText("B: "+String.valueOf(B));
		viewTableRecordCountLabel.setText("R: "+String.valueOf(R));
				
		RecordFile recordFile = new RecordFile(ti, tx);
		
		while(recordFile.next()){
			Vector<Object> rowData = new Vector<Object>();
			for (String fld : sch.fields()) {
				if(sch.type(fld) == 4)
					rowData.add(recordFile.getInt(fld));
//				else if(sch.type(fld) == 8)
//					rowData.add(recordFile.getDouble(fld));
				else
					rowData.add(recordFile.getString(fld));
			}
			
			viewTableModel.addRow(rowData);
		}	
		tx.commit();
	}
	
	//Query sayfasi acildiginda query panel componentlerini clear eder ve gorunurlugunu ayarlar.
	private void editQueryPanel(){
		queryTextField.setText("");
		queryWarningLabel.setVisible(false);
		setVisibilitySelectComponents(false);
	}
		
	//Sadece "Select" komutu calistiginda gosterilecek olan komponentlerin gorunurlugu ayarlanir.
	private void setVisibilitySelectComponents(boolean visibility){
		queryResultTableScrollPane.setVisible(visibility);
		queryTreeScrollPane.setVisible(visibility);
		resultTableLabel.setVisible(visibility);
		queryTreeLabel.setVisible(visibility);
	}
	
	//Load table paneli acildiginda componetlerin durumunu duzenler.
	private void editLoadTablePanel(){
		loadTableNameTextField.setText("");
		loadTableWarningLabel_1.setText("");
		loadTableWarningLabel_1.setVisible(false);
		loadTablePanel_1.setVisible(false);
	}
	
	//Load table panelinde Ok butonuna basildiktan sonra acilacak olan kucuk panelin componentlerinin ayarlari yapilir.
	private void editLoadTablePanel_1(){
		loadTablePanel_1.setVisible(true);
		loadTableRecordCountTextField.setText("");
		loadTableWarningLabel_2.setText("");
		loadTableWarningLabel_2.setVisible(false);
	}
		
	////View table paneli acildiginda componetlerin durumunu duzenler.
	private void editViewTablePanel(){
		viewTableNameTextField.setText("");
		viewTableWarningLabel.setVisible(false);
		viewTablePanel_1.setVisible(false);	
	}
		
	private void editExDbLoadPanel(){
		exDbWarningLabel_1.setVisible(false);
		exDbWarningLabel_2.setVisible(false);
		exDbWarningLabel_3.setVisible(false);
		exDbWarningLabel_4.setVisible(false);
		exDbWarningLabel_5.setVisible(false);
		
		exampleDB.createExTableProposal_1(exTableModel_1, exDbRecCountTextField_1);
		exampleDB.createExTableProposal_2(exTableModel_2, exDbRecCountTextField_2);
		exampleDB.createExTableProposal_3(exTableModel_3, exDbRecCountTextField_3);
		exampleDB.createExTableProposal_4(exTableModel_4, exDbRecCountTextField_4);
		exampleDB.createExTableProposal_5(exTableModel_5, exDbRecCountTextField_5);
	}
	
	//Sadece bir tane panel gorunur yapilir.
	private void setVisiblePanel(JPanel visiblePanel){
		for(JPanel panel  : panels){
			panel.setVisible(false);
		}
		visiblePanel.setVisible(true);
	}
	
	//Gelen sorgu islenir.
	private void handQuery(String query){
		
		Transaction tx = new Transaction();
		String[] words = query.split(" ");
		
		if(words[0].equals("create") || words[0].equals("insert") || words[0].equals("delete")){
			SimpleDB.planner().executeUpdate(query, tx);
			setVisibilitySelectComponents(false);
		}
		else if(words[0].equals("select")){
			queryResultModel.setColumnCount(0);
			queryResultModel.setRowCount(0);
			
			Plan p = SimpleDB.planner().createQueryPlan(query, tx);	
			fillQueryTree(p);
			Schema sch = p.schema();
			// analogous to the result set
			Scan s = p.open();
			
			for (String fld : sch.fields()){
				queryResultModel.addColumn(fld);
			}
			
			while (s.next()) {
				Vector<Object> rowData = new Vector<Object>();
				for (String fld : sch.fields()){
					if(sch.type(fld) == 4)
						rowData.add(s.getInt(fld));
//					else if(sch.type(fld) == 8)
//						rowData.add(recordFile.getDouble(fld));
					else
						rowData.add(s.getString(fld));
				}
				queryResultModel.addRow(rowData);
			}			
			s.close();
			
			setVisibilitySelectComponents(true);
		}
		else{
			queryWarningLabel.setText("Query is meaningless.");
			queryWarningLabel.setVisible(true);
		}
		
		tx.commit();		
	}
	
	//Sorgu agaci planlardan elde edilen dugumlere gore doldurulur.
	public void fillQueryTree(Plan p){
		DefaultTreeModel queryTreeModel = new DefaultTreeModel(p.getTreeNode());
		queryTree.setModel(queryTreeModel);		
	}
}
