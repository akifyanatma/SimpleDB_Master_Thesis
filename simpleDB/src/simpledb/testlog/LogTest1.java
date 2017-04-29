package simpledb.testlog;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

//import simpledb.log.BasicLogRecord;
//import simpledb.log.LSN;
//import simpledb.log.LogForwardIterator;
//import simpledb.log.LogMgr;
//import simpledb.server.SimpleDB;

import simpledb.log.BasicLogRecord;
import simpledb.log.LSN;
import simpledb.log.LogForwardIterator;
import simpledb.log.LogIterator;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;
import simpledb.tx.recovery.RecoveryMgr;

public class LogTest1 {

	@Test
	public void test() {
		System.out.println("Test 1:");
		System.out.println("******************************");
		SimpleDB.initFileLogAndBufferMgr("logtest1VT");
		// use simpledb.log system log file. Delete DB after testing. Because logfile is dummy.
		LogMgr logmgr = SimpleDB.logMgr();
		
		/*
		 * append start log records for tx=10,11,12 and 13.
		 * start's code is 1.
		 */
		LSN lsn1 = logmgr.append(new Object[]{1, 10});  // <start 10>
		LSN lsn2 = logmgr.append(new Object[]{1, 11});  // <start 11>
		LSN lsn3 = logmgr.append(new Object[]{1, 12});   // <start 12>
		LSN lsn4 = logmgr.append(new Object[]{1, 13});   // <start 13>
		logmgr.flush(lsn4);
		
		/*
		 * reward iteration over log file. Already coded.  
		 */
		System.out.println(" default(reward) iteration used during recovery.");
		Iterator<BasicLogRecord> iter = logmgr.iterator();
		while(iter.hasNext()) {
			BasicLogRecord rec = iter.next();
			int v1 = rec.nextInt();
			int v2 = rec.nextInt();
			System.out.println("[" + v1 + ", " + v2 + "]");
		}
		((LogIterator) iter).close();  // unpin the last used buffer. (first block of log file)

		
		
		/*
		 * Now to display log file, forward iterator over the log file.
		 */
		System.out.println(" (forward) iteration used for displaying log file");
		Iterator<BasicLogRecord> forwarditer = logmgr.forwardIterator();
		while(forwarditer.hasNext()) {
			BasicLogRecord rec = forwarditer.next();
			int v1 = rec.nextInt();
			int v2 = rec.nextInt();
			System.out.println("[" + v1 + ", " + v2 + "]");
		}
		((LogForwardIterator) forwarditer).close();  // unpin the last used buffer. (last block of log file)
	}

}
