package simpledb.testlog;

import java.util.Iterator;

import simpledb.buffer.BufferMgr;
import simpledb.file.FileMgr;
import simpledb.log.BasicLogRecord;
import simpledb.log.LogForwardIterator;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class Test3B implements Runnable {

	@Override
	public void run() {
		
		BufferMgr bm = SimpleDB.bufferMgr();
		LogMgr lm = SimpleDB.logMgr(); // use simpledb.log system log file.
										// Delete DB after testing. Because
										// logfile is dummy.
		FileMgr fm = SimpleDB.fileMgr(); // for R/W stats
		System.out
				.println("\n\n(forward) iteration used for displaying log file");
		Iterator<BasicLogRecord> forwarditer = lm.forwardIterator();
		while (forwarditer.hasNext()) {
			BasicLogRecord rec = forwarditer.next();
			int v1 = rec.nextInt();
			String v2 = rec.nextString();
			System.out.println("[" + v1 + ", " + v2 + "]");
		}
		((LogForwardIterator) forwarditer).close(); // release last used buffer
		System.out.println(bm.listBuffer());
		System.out.println("R:" + fm.blocksRead() + " \\ W:"
				+ fm.blocksWritten());
		System.out.println("*****Current num of available buffers: "
				+ bm.available());

	}

}
