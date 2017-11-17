package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class TableCatalogPanel extends JPanel {
	private DefaultTableModel tableCatalogModel;

	/**
	 * Create the panel.
	 */
	public TableCatalogPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		JLabel tblCatLabel = new JLabel("Table Catalog");
		tblCatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tblCatLabel.setBounds(30, 31, 203, 24);
		add(tblCatLabel);
		
		tableCatalogModel = new DefaultTableModel();
		tableCatalogModel.addColumn("TblName");
		tableCatalogModel.addColumn("RecLength");
		
		JTable table = new JTable(tableCatalogModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(20);
		table.setBounds(0, 0, 1, 1);
		add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30, 58, 361, 631);
		add(scrollPane);
	}
	
	public void getTableCatalogData() {
		tableCatalogModel.setRowCount(0);
	
		Transaction tx = new Transaction();
		ArrayList<TableInfo> tableInfos = SimpleDB.mdMgr().getAllTableInfo(tx);		
	
		for(int i=0; i<tableInfos.size();i++){
			TableInfo ti = tableInfos.get(i);
			String tblName = ti.fileName();
			int recLength = ti.recordLength();
			tableCatalogModel.addRow(new Object[]{tblName, recLength});
		}
	
		tx.commit();
	}

}
