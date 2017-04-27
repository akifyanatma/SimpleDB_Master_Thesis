package simpledb.testlog;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.log.LSN;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class Test4A implements Runnable {
	int NUMOFLOGRECORDS;
	public Test4A(int numofRecs){
		NUMOFLOGRECORDS=numofRecs;
	}
	
	@Override
	public void run() {
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr(); // use simpledb.log system log file.
										// Delete DB after testing. Because
										// logfile is dummy.
		FileMgr fm = SimpleDB.fileMgr(); // for R/W stats
		System.out.println("\n\nAppending log records: rpb =2: 2 records/page\n");
		Object[] logrec = new Object[2];
		logrec[1] = "This is a log recThis is a log rec"; // 17B*2=34+4 = 38 B +
													// 4 (int logrec[0],
													// later)
		// total size of a log record = 42 + 4B(prevpointer) =46
		// 100-4 = 96 B (available size of page for log records)
		// max. 96/46 = 2 logrecords/page. 4 B waste.
		for (int i = 0; i < NUMOFLOGRECORDS; i++)  {
			logrec[0] = i; // 4B
			LSN lsn = lm.append(logrec);
			System.out.println("LSN for logrec " + i + ": " + lsn.toString());
			if(i==9){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(bm.listBuffer());
			System.out.println("R:" + fm.blocksRead() + " \\ W:"+ fm.blocksWritten());
			System.out.println("*");
		}	
		lm.flush(new LSN(1, 188));  // flush the last page of log.
		if(fm.blocksWritten() != 2)
			System.out.println("*****LogTest4: FAIL!!!");
		
		
		System.out.println("\nThread 1 is finished \n");
	}
}
