package simpledb.testlog;

import simpledb.log.LSN;
import simpledb.log.LogMgr;
import simpledb.server.SimpleDB;

public class Test4B implements Runnable {

	@Override
	public void run() {
		LogMgr lm = SimpleDB.logMgr(); // access simpledb.log system log file.
		// in our simulation LSN numbers <bid,50> or <bid,96>
		try {
			System.out.println("Thread 2 is sleeping");
			Thread.sleep(2000);
			lm.flush(new LSN(0, 96));
			lm.flush(new LSN(0, 50));
			lm.flush(new LSN(0, 188));
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Thread 2 is finished \n");
	}
}
