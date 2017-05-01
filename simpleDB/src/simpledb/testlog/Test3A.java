package simpledb.testlog;

import java.util.Iterator;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.log.BasicLogRecord;
import simpledb.log.LogIterator;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class Test3A implements Runnable {

	@Override
	public void run() {
		
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr(); // use simpledb.log system log file.
										// Delete DB after testing. Because
										// logfile is dummy.
		FileMgr fm = SimpleDB.fileMgr(); // for R/W stats
		System.out
		.println("\n\n(reward/default) iteration used for recovering");
		Iterator<BasicLogRecord> iter = lm.iterator();
		while (iter.hasNext()) {
			BasicLogRecord rec = iter.next();
			int v1 = rec.nextInt();
			String v2 = rec.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
			// System.out.println(bm.listBuffer());
		}
		((LogIterator) iter).close(); // release last used buffer
		System.out.println(bm.listBuffer()); // end of iteration
		System.out.println("R:" + fm.blocksRead() + " \\ W:"
				+ fm.blocksWritten());
		System.out.println("*****Current num of available buffers: "
				+ bm.available());

	}

}
