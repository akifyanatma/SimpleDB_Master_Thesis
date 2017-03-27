package simpledb.test;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

/**
 * 4 tane buffer olusturulup icerikleri iceriklerine veri eklendi ve log kayýtlarý tutuldu.
 * Daha sonra bufferlar unpin ediliyor.
 * Daha sonra 4 tane yeni blok olusturulup bunlar pin edilmeye calisiyor.
 * @author Akif
 *
 */

public class LogTest_2_1 {

	@Test
	public void test() {

		SimpleDB.initFileLogAndBufferMgr("studentdb");
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr();
		int mytxnum = 1; //assume we are transaction 1
		
	
		Block blk0 = new Block("student.tbl", 0);
		Buffer buff0 = bm.pin(blk0);
		Object[] logrec0 = new Object[]{mytxnum, "student.tbl", 0, 0, "a"};
		int lsn0 = lm.append(logrec0);
		buff0.setString(0, "a", mytxnum, lsn0);
		
		Block blk1 = new Block("student.tbl", 1);
		Buffer buff1 = bm.pin(blk1);
		Object[] logrec1 = new Object[]{mytxnum, "student.tbl", 1, 0, "b"};
		int lsn1 = lm.append(logrec1);
		buff1.setString(0, "b", mytxnum, lsn1);
		
		Block blk2 = new Block("student.tbl", 2);
		Buffer buff2 = bm.pin(blk2);
		Object[] logrec2 = new Object[]{mytxnum, "student.tbl", 2, 0, "c"};
		int lsn2 = lm.append(logrec2);
		buff2.setString(0, "c", mytxnum, lsn2);
		
		Block blk3 = new Block("student.tbl", 3);
		Buffer buff3 = bm.pin(blk3);
		Object[] logrec3 = new Object[]{mytxnum, "student.tbl", 3, 0, "d"};
		int lsn3 = lm.append(logrec3);
		buff3.setString(0, "b", mytxnum, lsn3);
		
		
		
		bm.unpin(buff0);
		bm.unpin(buff1);
		bm.unpin(buff2);
		bm.unpin(buff3);
		
		Block blk4 = new Block("student.tbl", 4);
		Block blk5 = new Block("student.tbl", 5);
		Block blk6 = new Block("student.tbl", 6);
		Block blk7 = new Block("student.tbl", 7);
		
		bm.pin(blk4);
		bm.pin(blk5);
		bm.pin(blk6);
		bm.pin(blk7);
	}

}
