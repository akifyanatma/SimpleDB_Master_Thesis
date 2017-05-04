package simpledb.testlog;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

/*
*
* This test has 2 concurrent threads. The first one inserts log records, the other flushes some of them. 
* 
*  The aim is to understand "necessary flushes" of buffer during insertions.
*  Different from previous test, this test uses larger blocks. Same log records are added. RPB becomes 10.
*  
*  After adding the last log records of first page, the other thread flushes some log records, then the first thread continues to end.
*  At the end total disk writes should be 2.
*   
*  Buffer pool size = 2    
*  NUMOFLOGRECORDS=20;
*  int logrecsize = logrec[1].toString().length()+ 4+ 4+ 4;  
*  // 4 for string in page, 4 for "int logrec[0]", 4 for prev pointer.
*  // logresize = 46 B
*	int rpb=(Page.BLOCK_SIZE-4) / logrecsize;  
*	// -4, because first 4B int is used for pointing LASTPOS in the page
*  RPB = 496 / 46 = 10
* 	int NUMOFLOGBLOCKS=NUMOFLOGRECORDS/rpb;
*	Thus, rpb= 10
*  and NUMOFLOGBLOCKS = 2
*  
*/

public class LogTest4 {

	@Test
	public void test() {
		int NUMOFLOGRECORDS=20;
		Page.BLOCK_SIZE = 500;
		SimpleDB.BUFFER_SIZE = 2;
		SimpleDB.BUFFER_REPLACEMENT_POLICY = SimpleDB.bufferReplacementPolicy.LRU;
		SimpleDB.initFileLogAndBufferMgr("logtest4VT");
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr(); // use simpledb.log system log file.
										// Delete DB after testing. Because
										// logfile is dummy.
		FileMgr fm = SimpleDB.fileMgr(); // for R/W stats
		
		
		System.out.println("Test 4: \nBlock Size: "+Page.BLOCK_SIZE + "\nBUFFER_POOL_SIZE: "+SimpleDB.BUFFER_SIZE + "\nNUMOFLOGRECORDS: "+NUMOFLOGRECORDS + "\nRPB: 10");
		System.out.println("******************************");
	
		System.out.println("initial buffer state: holding only first log page");
		System.out.println(bm.listBuffer()); // display buffers formatted like:
												// bid\tablename-blockNo\
												// pin_state
												// we see 1 log buffers, first
												// page of log file.
		
		System.out.println("\nDisk access Stats after LogMgr initialization : ");
		System.out.println("R:" + fm.blocksRead() + " \\ W:"+ fm.blocksWritten());
	

		Test4A t1 = new Test4A(NUMOFLOGRECORDS);
		Thread th1 = new Thread(t1);
		th1.start();

		Test4B t2 = new Test4B();
		Thread th2 = new Thread(t2);
		th2.start();

		// concurrent execution
		try {
			th1.join();
			th2.join();
		} catch (InterruptedException e) {
		}
		
		System.out.println(bm.listBuffer());
		System.out.println("R:" + fm.blocksRead() + " \\ W:"
				+ fm.blocksWritten());
		if (bm.available() != SimpleDB.BUFFER_SIZE - 1)
			System.out.println("*****LogTest2: FAIL!!!");
		System.out.println(bm.listBuffer()); // end of iteration
	}

}
