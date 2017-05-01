package simpledb.testbuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.log.LSN;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class BufferTest2 {

	@Test
	public void test() {
		
		SimpleDB.initFileLogAndBufferMgr("studentdb");
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr();
		int mytxnum = 1; //assume we are transaction 1
		
		Block blk = new Block("student.tbl", 0);
		Buffer buff = bm.pin(blk);
		int gradyr = buff.getInt(38);
		Object[] logrec = new Object[]{mytxnum, "student.tbl", 0, 38, gradyr};
		LSN lsn = lm.append(logrec);
		buff.setInt(38, gradyr+1, mytxnum, lsn);
		bm.unpin(buff);
					
	}
}
