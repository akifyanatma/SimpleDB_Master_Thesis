package simpledb.gui;

import javax.swing.JPanel;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class FieldCatalogPanel extends JPanel {
	private DefaultTableModel fieldCatalogModel;

	/**
	 * Create the panel.
	 */
	public FieldCatalogPanel() {
		setPreferredSize(new Dimension(1360, 700));
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLayout(null);
		
		fieldCatalogModel = new DefaultTableModel();
		fieldCatalogModel.addColumn("TblName");
		fieldCatalogModel.addColumn("FldName");
		fieldCatalogModel.addColumn("Type");
		fieldCatalogModel.addColumn("Length");
		fieldCatalogModel.addColumn("Offset");
				
		JTable table = new JTable(fieldCatalogModel);
		table.setRowHeight(20);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(0, 0, 1, 1);
		add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(26, 58, 627, 631);
		add(scrollPane);
			
		JLabel fldCatLabel = new JLabel("Field Catalog");
		fldCatLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fldCatLabel.setBounds(26, 31, 381, 24);
		add(fldCatLabel);

	}
	
	public void getFieldCatalogData() {
		fieldCatalogModel.setRowCount(0);
		
		Transaction tx = new Transaction();
		ArrayList<TableInfo> tableInfos = SimpleDB.mdMgr().getAllTableInfo(tx);		
		
		for(int i=0; i<tableInfos.size();i++){
			TableInfo ti = tableInfos.get(i);
			String tblName = ti.fileName();
						
			Schema sch = ti.schema();
			for (String fld : sch.fields()) {
				int type = sch.type(fld);
				int lenght = sch.length(fld);
				int offset = ti.offset(fld);
				fieldCatalogModel.addRow(new Object[]{tblName, fld, type, lenght, offset});
			}
		}
		
		tx.commit();
	}

}
