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
	
	private static GUI instance = null;

	private JFrame frame;
	private JMenuBar menuBar;
		
	private ArrayList<JPanel> panels;
	
	private StartupPanel startupPanel;
	private QueryPanel queryPanel;
	private LoadingPanel loadingPanel;
	private ViewPanel viewPanel;
	private TableCatalogPanel tableCatalogPanel;
	private FieldCatalogPanel fieldCatalogPanel;
	private StatisticCatalogPanel statisticCatalogPanel;
	private IndexCatalogPanel indexCatalogPanel;
	private ExDbPanel exDbPanel;
	private JScrollPane exDbScrollPane;

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
					GUI window = getInstance();
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
	private GUI() {
		initialize();
	}
	
	public static GUI getInstance( ) {
		if(instance == null) {
			instance = new GUI();
	    }
	    return instance;
	}

	/**
	 * Initialize the contents of the frame.
	 */	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("SimpleDB");
		//frame.setBounds(100, 100, 1024, 1240);
		frame.setBounds(100, 100, 1366, 768);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));	
			
		panels = new ArrayList<>();
		
		initializeMenuBar();
		initializeStartupPanel();
		initializeQueryPanel();
		initializeLoadingPanel();
		initializeViewPanel();
		initializeFieldCatalogPanel();
		initializeStatisticPanel();
		initializeIndexCatalogPanel();
		initializeExDbLoadPanel();
	}
	
	private void initializeMenuBar(){
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setVisible(false);
		
		
		JMenuItem queryPanelMenuItem = new JMenuItem("Query");
		queryPanelMenuItem.setPreferredSize(new Dimension(60, 22));
		queryPanelMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryPanel.editQueryPanelComponents();
				setVisiblePanel(queryPanel);
			}
		});
		
		
		JMenu metaDataMenu = new JMenu("Metadata");
		metaDataMenu.setPreferredSize(new Dimension(100, 22));
		menuBar.add(metaDataMenu);
		
		
		JMenuItem tableCatalogMenuItem = new JMenuItem("Table Catalog");
		tableCatalogMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tableCatalogPanel.editTableCatalogComponents();
				tableCatalogPanel.getTableCatalogData();
				setVisiblePanel(tableCatalogPanel);
			}
		});
		metaDataMenu.add(tableCatalogMenuItem);
		
		
		JMenuItem fieldCatalogMenuItem = new JMenuItem("Field Catalog");
		fieldCatalogMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fieldCatalogPanel.getFieldCatalogData();
				setVisiblePanel(fieldCatalogPanel);
			}
		});
		metaDataMenu.add(fieldCatalogMenuItem);
		
		
		JMenuItem statisticCatalogMenuItem = new JMenuItem("Statistic Catalog");
		statisticCatalogMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				statisticCatalogPanel.getStatisticMetadata();
				setVisiblePanel(statisticCatalogPanel);
			}
		});
		metaDataMenu.add(statisticCatalogMenuItem);
		
		
		JMenuItem indexCatalogMenuItem = new JMenuItem("Index Catalog");
		indexCatalogMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexCatalogPanel.getIndexMetaData();
				setVisiblePanel(indexCatalogPanel);
			}
		});
		metaDataMenu.add(indexCatalogMenuItem);
		menuBar.add(queryPanelMenuItem);
		
		
		JMenuItem loadTableMenuItem = new JMenuItem("Load Table");
		loadTableMenuItem.setPreferredSize(new Dimension(60, 22));
		loadTableMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadingPanel.editLoadingPanelComponents();
				setVisiblePanel(loadingPanel);
			}
		});
		menuBar.add(loadTableMenuItem);
		
		
		JMenuItem exDBMenuItem = new JMenuItem("Example DB Load");
		exDBMenuItem.setPreferredSize(new Dimension(60, 22));
		exDBMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				exDbPanel.editExDbLoadPanel();
				setVisiblePanel(exDbPanel);					
				exDbScrollPane.setVisible(true);
			}
		});
		menuBar.add(exDBMenuItem);
		
		
		JMenuItem viewTableMenuItem = new JMenuItem("View Table");
		viewTableMenuItem.setPreferredSize(new Dimension(60, 22));
		viewTableMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPanel.editViewPanelComponents();
				setVisiblePanel(viewPanel);
			}
		});
		menuBar.add(viewTableMenuItem);
		
		Component glue = Box.createGlue();
		glue.setPreferredSize(new Dimension(700, 0));
		menuBar.add(glue);
	}
	
	private void initializeStartupPanel(){
		startupPanel = new StartupPanel();
		frame.getContentPane().add(startupPanel);
		panels.add(startupPanel);
	}

	private void initializeQueryPanel() {
		queryPanel = new QueryPanel();
		frame.getContentPane().add(queryPanel);
		panels.add(queryPanel);
	}
	
	private void initializeLoadingPanel(){
		loadingPanel = new LoadingPanel();
		frame.getContentPane().add(loadingPanel);
		panels.add(loadingPanel);
	}
	
	private void initializeViewPanel(){
		viewPanel = new ViewPanel();
		frame.getContentPane().add(viewPanel);
		panels.add(viewPanel);
	}
	
	public void initializeTableCatalogPanel(){
		tableCatalogPanel = new TableCatalogPanel();
		frame.getContentPane().add(tableCatalogPanel);
		panels.add(tableCatalogPanel);
		//Startup'dan sonra table katalog verisi ekran geleceginden asagidaki fonksiyonlar burada cagiriliyor.
		tableCatalogPanel.editTableCatalogComponents();
		tableCatalogPanel.getTableCatalogData();
		setVisiblePanel(tableCatalogPanel);
		menuBar.setVisible(true);
	}
	
	public void initializeFieldCatalogPanel(){
		fieldCatalogPanel = new FieldCatalogPanel();
		frame.getContentPane().add(fieldCatalogPanel);
		panels.add(fieldCatalogPanel);
	}
	
	private void initializeStatisticPanel() {
		statisticCatalogPanel = new StatisticCatalogPanel();
		frame.getContentPane().add(statisticCatalogPanel);
		panels.add(statisticCatalogPanel);
	}
	
	private void initializeIndexCatalogPanel() {
		indexCatalogPanel = new IndexCatalogPanel();
		frame.getContentPane().add(indexCatalogPanel);
		panels.add(indexCatalogPanel);
	}
	
	private void initializeExDbLoadPanel(){	
		exDbPanel = new ExDbPanel();
		frame.getContentPane().add(exDbPanel);
		panels.add(exDbPanel);
			
		exDbScrollPane = new JScrollPane(exDbPanel);
		frame.getContentPane().add(exDbScrollPane, "name_204988973856185");
		exDbScrollPane.setVisible(false);
	}
	
	//Sadece bir tane panel gorunur yapilir.
	private void setVisiblePanel(JPanel visiblePanel){
		for(JPanel panel  : panels){
			panel.setVisible(false);
		}
		visiblePanel.setVisible(true);
		exDbScrollPane.setVisible(false);
	}
}
