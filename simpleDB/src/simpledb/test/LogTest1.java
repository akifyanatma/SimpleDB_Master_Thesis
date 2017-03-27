package simpledb.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import simpledb.log.BasicLogRecord;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class LogTest1 {

	@Test
	public void test() {
		//SimpleDB.initFileAndLogMgr("studentdb");
		SimpleDB.initFileLogAndBufferMgr("studentdb");
		LogMgr logmgr = SimpleDB.logMgr();
		
		int lsn1 = logmgr.append(new Object[]{"a", "b"});
		int lsn2 = logmgr.append(new Object[]{"c", "d"});
		int lsn3 = logmgr.append(new Object[]{"e", "f"});
		logmgr.flush(lsn3);
		
		Iterator<BasicLogRecord> iter = logmgr.iterator();
		while(iter.hasNext()) {
			BasicLogRecord rec = iter.next();
			String v1 = rec.nextString();
			String v2 = rec.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
		}
				
	}

}
