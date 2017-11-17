package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.record.Schema;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import javax.swing.table.TableModel;

public class QueryPanel extends JPanel {
	private JPanel internalPanel;
	private DefaultTableModel queryResultModel;
	private JLabel warningLabel;
	private JTree queryTree;
	private JTextField textField;
	/**
	 * Create the panel.
	 */
	public QueryPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setLayout(null);
		
		internalPanel = new JPanel();
		internalPanel.setBounds(10, 103, 1340, 586);
		add(internalPanel);
		internalPanel.setLayout(null);
		
		JLabel queryLabel = new JLabel("Query:");
		queryLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		queryLabel.setBounds(51, 34, 59, 24);
		add(queryLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setBounds(134, 31, 970, 24);
		add(textField);
		textField.setColumns(10);
		
		warningLabel = new JLabel("Warning!");
		warningLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		warningLabel.setBounds(51, 74, 900, 24);
		add(warningLabel);
				
		queryResultModel = new DefaultTableModel();
			
		JTable table = new JTable(queryResultModel);
		table.setRowHeight(20);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(-20, -114, 1, 1);
		internalPanel.add(table);
		
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBounds(16, 35, 641, 540);
		internalPanel.add(tableScrollPane);
		
		JScrollPane treeScrollPane = new JScrollPane();
		treeScrollPane.setBounds(719, 35, 611, 540);
		internalPanel.add(treeScrollPane);
		
		queryTree = new JTree();
		treeScrollPane.setViewportView(queryTree);
		
		JLabel resultTableLabel = new JLabel("ResultTable");
		resultTableLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		resultTableLabel.setBounds(18, 10, 133, 24);
		internalPanel.add(resultTableLabel);
		
		JLabel treeLabel = new JLabel("Query Tree");
		treeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		treeLabel.setBounds(719, 10, 119, 24);
		internalPanel.add(treeLabel);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = textField.getText();
				if(query.length() == 0){
					warningLabel.setText("Please, enter query.");
					warningLabel.setVisible(true);
				}
				else{
					handQuery(query);
					warningLabel.setVisible(false);
				}
			}
		});
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okButton.setBounds(1015, 74, 89, 24);
		add(okButton);
	}
	
	//Gelen sorgu islenir.
	private void handQuery(String query) {		
		Transaction tx = new Transaction();
		String[] words = query.split(" ");
		
		if(words[0].equals("create") || words[0].equals("insert") || words[0].equals("delete")){
			SimpleDB.planner().executeUpdate(query, tx);
			internalPanel.setVisible(false);
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
			
			internalPanel.setVisible(true);
		}
		else{
			warningLabel.setText("Query is meaningless.");
			warningLabel.setVisible(true);
		}
		
		tx.commit();		
	}
	
	//Sorgu agaci planlardan elde edilen dugumlere gore doldurulur.
	public void fillQueryTree(Plan p){
		DefaultTreeModel queryTreeModel = new DefaultTreeModel(p.getTreeNode());
		queryTree.setModel(queryTreeModel);		
	}
	
	//Sadece "Select" komutu calistiginda gosterilecek olan komponentlerin gorunurlugu ayarlanir.
	public void editQueryPanelComponents() {
		textField.setText("");
		warningLabel.setVisible(false);
		internalPanel.setVisible(false);
	}
}
