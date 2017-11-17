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

public class StatisticCatalogPanel extends JPanel {
	private DefaultTableModel statisticCatalogModel;
	/**
	 * Create the panel.
	 */
	public StatisticCatalogPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		JLabel statisticLabel = new JLabel("Statistics");
		statisticLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statisticLabel.setBounds(23, 22, 115, 24);
		add(statisticLabel);
		
		statisticCatalogModel = new DefaultTableModel();
		statisticCatalogModel.addColumn("T");
		statisticCatalogModel.addColumn("B(T)");
		statisticCatalogModel.addColumn("R(T)");
		statisticCatalogModel.addColumn("V(T, F)");
		
		JTable table = new JTable(statisticCatalogModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(20);
		table.setBounds(0, 0, 1, 1);
		add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(23, 57, 964, 617);
		add(scrollPane);

	}
	
	public void getStatisticMetadata(){
		statisticCatalogModel.setRowCount(0);
					
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
			
			statisticCatalogModel.addRow(new Object[]{tblName, B, R, distinctValues.get(0)});
			for(int j=1; j<distinctValues.size();j++){
				statisticCatalogModel.addRow(new Object[]{"", "","", distinctValues.get(j)});
			}
			
		}
			
		tx.commit();
	}

}
