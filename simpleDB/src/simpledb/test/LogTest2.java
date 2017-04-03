package simpledb.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import simpledb.buffer.BufferMgr;
import simpledb.log.BasicLogRecord;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class LogTest2 {

	@Test
	public void test() {
		SimpleDB.init("logtest");
		
		BufferMgr bm=SimpleDB.bufferMgr();
		System.out.println(bm.listBuffer());
		
		LogMgr lm = new LogMgr("logtestfile");
		lm.takeFirstBuffer();
		System.out.println(bm.listBuffer());
		Object[] logrec = new Object[2];
		for (int i=0; i<20; i++) {
			logrec[0] = i;   // 4B
 			logrec[1] = "this is a log rec"; //  17B+4 =21 B
 			 // total size of a log record = 21 + 4B(prevpointer) =25
 			 // 100-4 = 96 B (available size of page for log records)
 			  // max. 96/25 = 3 logrecords/page. 21B waste.
 			
			int lsn = lm.append(logrec);
			System.out.println("LSN for logrec " + i + ": " + lsn);
			System.out.println(bm.listBuffer());
		}

		System.out.println("Printing the log records:");
		
		Iterator<BasicLogRecord> iter = lm.iterator();
		while(iter.hasNext()) {
			BasicLogRecord r = iter.next();
			int v1 = r.nextInt();
			String v2 = r.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
			System.out.println(bm.listBuffer());
		}
	}

}
