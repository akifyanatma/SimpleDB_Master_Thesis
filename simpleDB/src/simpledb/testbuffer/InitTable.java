package simpledb.testbuffer;

import java.util.Random;

import simpledb.metadata.MetadataMgr;
import simpledb.record.RecordFile;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class InitTable {
	public static int  numOfTuples=999, deptmax=40; 
	   
	   public static void initData(String dbdir,int _numOfTuples) {
	      System.out.println("BEGIN INITIALIZATION");
	      numOfTuples=_numOfTuples;
	      SimpleDB.init(dbdir);
	      if (SimpleDB.fileMgr().isNew()) {
	         System.out.println("loading data");
	         MetadataMgr md = SimpleDB.mdMgr();
	         Transaction tx = new Transaction();

	         // create and populate the student table
	         Schema sch = new Schema();
	         sch.addIntField("sid");
	         sch.addStringField("sname", 12);
	         sch.addIntField("majorid");
	         sch.addIntField("gradyear");
	         md.createTable("student", sch, tx);
	         TableInfo ti = md.getTableInfo("student", tx);

	         RecordFile rf = new RecordFile(ti, tx);
	         while (rf.next())
	            rf.delete();
	         rf.beforeFirst();
	         for (int id=0; id<numOfTuples; id++) {
	            rf.insert();
	            Random _rgen= new Random();
	            int _sid=_rgen.nextInt(numOfTuples);
	            rf.setInt("sid", _sid);
	            rf.setString("sname", "student"+id);
	            rf.setInt("majorid", id%deptmax);
	            rf.setInt("gradyear", (id%50)+1960);
	         }
	         rf.close();
	 
	         System.out.println("student.tbl size:"+ SimpleDB.fileMgr().size("student.tbl"));
	         
	         tx.commit();
	         tx = new Transaction();
	         tx.recover(); // add a checkpoint record, to limit rollback
	      }
	   }
}
