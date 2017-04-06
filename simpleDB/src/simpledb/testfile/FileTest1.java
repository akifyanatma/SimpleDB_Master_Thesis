package simpledb.testfile;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.file.Block;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

public class FileTest1 {

	@Test
	public void test() {
		//fail("Not yet implemented");
		
		SimpleDB.initFileMgr("studentdb");
		FileMgr fm = SimpleDB.fileMgr();
		
		
//		//File empty
//		Block blk1 = new Block("student.tbl", 0);
//		Page p1 = new Page();
//		p1.read(blk1);
//		String sname = p1.getString(46);
//		int gy = p1.getInt(38);
//		System.out.println(sname + " has gradyr " + gy);
		
		//Add a new block 0
		Page p2 = new Page();
		p2.setString(16, "tim");
		p2.setInt(8,  2012);
		Block blk2 = p2.append("student.tbl");
		
		//Add a new block 1
		Page p3 = new Page();
		p3.setString(16, "ali");
		p3.setInt(8,  2017);
		Block blk3 = p3.append("student.tbl");
		
		
		//Read and print block 0
		Block blk4 = new Block("student.tbl", 0);
		Page p4 = new Page();
		p4.read(blk4);
		String myString = p4.getString(16);
		int myInt = p4.getInt(8);
		System.out.println(myString + " has gradyr " + myInt);
		
//		//Read and print block 1
//		Block blk5 = new Block("student.tbl", 1);
//		Page p5 = new Page();
//		p5.read(blk5);
//		myString = p5.getString(16);
//		myInt = p5.getInt(8);
//		System.out.println(myString + " has gradyr " + myInt);
//				
	}

}
