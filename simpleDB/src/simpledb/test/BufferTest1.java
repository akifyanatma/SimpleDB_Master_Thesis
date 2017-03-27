package simpledb.test;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class BufferTest1 {

	@Test
	public void test() {
		SimpleDB.initFileLogAndBufferMgr("studentdb");
		BufferMgr bm = SimpleDB.bufferMgr();
		
		Block blk = new Block("student.tbl", 0);
		Buffer buff = bm.pin(blk);
		String sname = buff.getString(46);
		int gradyr = buff.getInt(38);
		System.out.println(sname + " has gradyear " +  gradyr);
	}

}
