package simpledb.gui;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class ExampleDb {
	public void createTable(String tableName){
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
	
	public void createExTableProposal_1(DefaultTableModel model, JTextField textField){
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
	
	public void createExTableProposal_2(DefaultTableModel model, JTextField textField){
		textField.setText("4");
		
		model.setColumnCount(0);
		model.setRowCount(0);
		
		model.addColumn("did");
		model.addColumn("dname");
		
		model.addRow(new Object[]{"0", "2"});
		model.addRow(new Object[]{"3", "28"});
		model.addRow(new Object[]{"4", "4"});
	}
	
	public void createExTableProposal_3(DefaultTableModel model, JTextField textField){
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
	
	public void createExTableProposal_4(DefaultTableModel model, JTextField textField){
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
	
	public void createExTableProposal_5(DefaultTableModel model, JTextField textField){
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
