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

import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class TableCatalogPanel extends JPanel {
	private DefaultTableModel tableCatalogModel;
	private DefaultTableModel statisticTableModel;
	private JPanel internalPanel;
	private JLabel tableNameLabel;
	private JLabel blockCountLabel;
	private JLabel recordCountLabel;

	/**
	 * Create the panel.
	 */
	public TableCatalogPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		internalPanel = new JPanel();
		internalPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		internalPanel.setBounds(421, 11, 886, 678);
		add(internalPanel);
		internalPanel.setLayout(null);
		
		JLabel tblCatLabel = new JLabel("Table Catalog");
		tblCatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tblCatLabel.setBounds(30, 31, 203, 24);
		add(tblCatLabel);
		
		tableCatalogModel = new DefaultTableModel();
		tableCatalogModel.addColumn("TblName");
		tableCatalogModel.addColumn("RecLength");
		
		JTable catalogTable = new JTable(tableCatalogModel);
		catalogTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		catalogTable.setRowHeight(20);
		catalogTable.setBounds(0, 0, 1, 1);
		add(catalogTable);
		
		catalogTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			 public void mouseClicked(java.awt.event.MouseEvent evt) {
			    int row = catalogTable.rowAtPoint(evt.getPoint());
			    int col = catalogTable.columnAtPoint(evt.getPoint());
			    if (col == 0) {
			    	String tableName = tableCatalogModel.getValueAt(row, col).toString();
			    	getTableStatistic(tableName);
			    }
			 }
		});
		
		JScrollPane scrollPane_1 = new JScrollPane(catalogTable);
		scrollPane_1.setBounds(30, 58, 361, 620);
		add(scrollPane_1);
		
		statisticTableModel = new DefaultTableModel();
		statisticTableModel.addColumn("Field");
		statisticTableModel.addColumn("V(T, F)");
		
		JTable statisticTable = new JTable(statisticTableModel);
		statisticTable.setBounds(0, 0, 1, 1);
		internalPanel.add(statisticTable);
		
		JScrollPane scrollPane_2 = new JScrollPane(statisticTable);
		scrollPane_2.setBounds(10, 47, 842, 620);
		internalPanel.add(scrollPane_2);
				
		tableNameLabel = new JLabel("Table Name");
		tableNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableNameLabel.setBounds(10, 20, 191, 24);
		internalPanel.add(tableNameLabel);
		
		blockCountLabel = new JLabel("B:");
		blockCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		blockCountLabel.setBounds(243, 20, 156, 24);
		internalPanel.add(blockCountLabel);
		
		recordCountLabel = new JLabel("R:");
		recordCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recordCountLabel.setBounds(409, 20, 179, 24);
		internalPanel.add(recordCountLabel);
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
	
	private void getTableStatistic(String tableName) {
		Transaction tx = new Transaction();
		
		statisticTableModel.setRowCount(0);
		
		String newTableName = "";
		if(tableName.contains(".tbl"))
			newTableName = tableName.replace(".tbl", "");
		
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(newTableName, tx);
		Schema sch = ti.schema();
			
		int B = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).blocksAccessed();
		int R = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).recordsOutput();
		
		tableNameLabel.setText(newTableName);
		blockCountLabel.setText("B:"+B);
		recordCountLabel.setText("R:"+R);
		
		for (String fld : sch.fields()) {
			int distinctValue = SimpleDB.mdMgr().getStatInfo(tableName, ti, tx).distinctValues(fld);
			statisticTableModel.addRow(new Object[]{fld, distinctValue});
		}
		
		internalPanel.setVisible(true);
		
		tx.commit();
	}
	
	public void editTableCatalogComponents() {
		internalPanel.setVisible(false);
	}

}
