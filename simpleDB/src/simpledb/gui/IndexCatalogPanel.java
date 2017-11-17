package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import simpledb.metadata.IndexInfo;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class IndexCatalogPanel extends JPanel {
	private DefaultTableModel tableModel;

	/**
	 * Create the panel.
	 */
	public IndexCatalogPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Indexes");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(28, 29, 180, 24);
		add(lblNewLabel);
		
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Table");
		tableModel.addColumn("Field");
		tableModel.addColumn("Name");
		tableModel.addColumn("Type");
				
		JTable table = new JTable(tableModel);
		table.setRowHeight(20);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(0, 0, 1, 1);
		add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(25, 65, 928, 624);
		add(scrollPane);
		
	}
	
	public void getIndexMetaData() {
		tableModel.setRowCount(0);
		
		Transaction tx = new Transaction();
		
		ArrayList<TableInfo> tableInfos = SimpleDB.mdMgr().getAllTableInfo(tx);
		for(TableInfo ti : tableInfos) {
			String tblName = ti.fileName();
			String newtblName="";
			if(tblName.contains(".tbl"))
				newtblName = tblName.replace(".tbl", "");
			Map<String, ArrayList<IndexInfo>> indexes = SimpleDB.mdMgr().getIndexInfo_(newtblName, tx);
			
			for(String fldName : indexes.keySet()) {
				ArrayList<IndexInfo> indexList = indexes.get(fldName);
				
				for(IndexInfo ii : indexList) {
					String idxName = ii.getIndexName();
					String idxType = ii.getType();
					tableModel.addRow(new Object[]{tblName, fldName, idxName, idxType});
				}
				
			}
			
		}
		
		
		tx.commit();
	}

}
