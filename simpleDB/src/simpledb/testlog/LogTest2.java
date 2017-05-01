package simpledb.testlog;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.log.BasicLogRecord;
import simpledb.log.LSN;
import simpledb.log.LogForwardIterator;
import simpledb.log.LogIterator;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

/*
 * This test iterates forward / bacward over the log file. 
 * LogMgr, LogIterator and LogForwardIterator uses DB buffers.
 * LSN is <blockid, offset>.
 * The aim is to understand buffer usage during iteration and to
 * calculate R/W stats.
 * During iteration the buffers should be released after usage. 
 * The critical point is to release the last used log page at the end of iteration.
 * Buffer rep. policy is set to LRU.
 * 
 *  Buffer pool size = 5     --> change this value as 10 or above to see expected R/W stats.
 *  NUMOFLOGRECORDS=20;
 *  int logrecsize = logrec[1].toString().length()+ 4+ 4+ 4;  
 *  // 4 for string in page, 4 for "int logrec[0]", 4 for prev pointer.
 *  // logresize = 46 B
 *	int rpb=(Page.BLOCK_SIZE-4) / logrecsize;  
 *	// -4, because first 4B int is used for pointing LASTPOS in the page
 * 	int NUMOFLOGBLOCKS=NUMOFLOGRECORDS/rpb;
 *	Thus, rpb=2    --> This value does NOT change.
 *  and NUMOFLOGBLOCKS = 10
 *  
 */

public class LogTest2 {

	@Test
	public void test() {
		int NUMOFLOGRECORDS=20;
		Page.BLOCK_SIZE=100;
		SimpleDB.BUFFER_SIZE=7;
		SimpleDB.BUFFER_REPLACEMENT_POLICY = SimpleDB.bufferReplacementPolicy.LRU;
		SimpleDB.initFileLogAndBufferMgr("logtest2VT");
		BufferMgr bm=SimpleDB.bufferMgr();		
		LogMgr lm = SimpleDB.logMgr(); // use simpledb.log system log file. Delete DB after testing. Because logfile is dummy. 
		FileMgr fm=SimpleDB.fileMgr(); // for R/W stats
		
		System.out.println("Test 2: \nBlock Size: "+Page.BLOCK_SIZE + "\nBUFFER_POOL_SIZE: "+SimpleDB.BUFFER_SIZE + "\nNUMOFLOGRECORDS: "+NUMOFLOGRECORDS + "\nRPB: 2");
		System.out.println("******************************");
		System.out.println("initial buffer state: holding only first log page");				
		System.out.println(bm.listBuffer());  // display buffers formatted like:    bid\tablename-blockNo\ pin_state
												// we see 1 log buffers, first page of log file. 
		
		System.out.println("\nDisk access Stats after LogMgr initialization : ");
		System.out.println("R:" +fm.blocksRead() + " \\ W:" + fm.blocksWritten());	
		
		System.out.println("\n\n Appending log records: rpb =2: 2 records/page");		
		Object[] logrec = new Object[2];
		logrec[1] = "This is a log recThis is a log rec"; //  17B*2=34+4 = 38 B  + 4 (int logrec[0], later)
		 // total size of a log record = 42 + 4B(prevpointer) =46
		 // 100-4 = 96 B (available size of page for log records)
		  // max. 96/46 = 2 logrecords/page. 4 B waste.
		
		for (int i=0; i<NUMOFLOGRECORDS; i++) {
			logrec[0] = i;   // 4B		 			
			LSN lsn = lm.append(logrec);
			System.out.println("LSN for logrec " + i + ": " + lsn.toString());
			System.out.println(bm.listBuffer());
			System.out.println("R:" +fm.blocksRead() + " \\ W:" + fm.blocksWritten());
			System.out.println("*");	
		}
		System.out.println(bm.listBuffer());
		System.out.println("R:" +fm.blocksRead() + " \\ W:" + fm.blocksWritten());
		if (bm.available()!= SimpleDB.BUFFER_SIZE-1)
			System.out.println("*****LogTest2: FAIL!!!");
				
		
		System.out.println("\n\n(reward/default) iteration used for recovering");
		Iterator<BasicLogRecord> iter = lm.iterator();
		while(iter.hasNext()) {
			BasicLogRecord rec = iter.next();
			int v1 = rec.nextInt();
			String v2 = rec.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
//			System.out.println(bm.listBuffer());
		}
		((LogIterator) iter).close();  // release last used buffer		
		System.out.println(bm.listBuffer());  // end of iteration
		System.out.println("R:" +fm.blocksRead() + " \\ W:" + fm.blocksWritten());
		if (bm.available()!= SimpleDB.BUFFER_SIZE-1)
			System.out.println("*****LogTest2: FAIL!!!");
		
		
		
		System.out.println("\n\n(forward) iteration used for displaying log file");
		Iterator<BasicLogRecord> forwarditer = lm.forwardIterator();
		while(forwarditer.hasNext()) {
			BasicLogRecord rec = forwarditer.next();
			int v1 = rec.nextInt();
			String v2 = rec.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
		}
		((LogForwardIterator) forwarditer).close();  // release last used buffer
		System.out.println(bm.listBuffer());
		System.out.println("R:" +fm.blocksRead() + " \\ W:" + fm.blocksWritten());
		if (bm.available()!= SimpleDB.BUFFER_SIZE-1)
			System.out.println("*****LogTest2: FAIL!!!");
		
//		System.out.println("Printing the log records:");
//		for (BasicLogRecord r : lm) {
//			int v1 = r.nextInt();
//			String v2 = r.nextString();
//			System.out.println("[" + v1 + ", " + v2 + "]");
//			System.out.println(bm.listBuffer());
//		}
	}

}
