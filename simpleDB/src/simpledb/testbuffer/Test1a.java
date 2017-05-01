package simpledb.testbuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class Test1a {

	@Test
	public void test() {
		InitTable.initData("BufMgr100",100);  // contains "student" table
		BufferMgr bm=new BufferMgr(7);  // initialize a new "smaller" buffer pool. Otherwise the buffer will not be empty initially, because of catalog blocks.
										// Last 3 buffers are log buffers, they are not used is this test program.
		SimpleDB.BUFFER_REPLACEMENT_POLICY = SimpleDB.bufferReplacementPolicy.LRU;
		
		System.out.println("\nBuffer State: " + bm.listBuffer());
		
		Buffer b0 = bm.pin(new Block("student.tbl", 0));
		Buffer b1 = bm.pin(new Block("student.tbl", 1));
		System.out.println("Buffer State: " + bm.listBuffer());
		Buffer b2 = bm.pin(new Block("teacher", 0));
		if(b2.getBlockNum()==b0.getBlockNum())
			System.err.print("buffer mgr error1!..");
		Buffer b3 = bm.pin(new Block("cashier", 1));
		System.out.println("\nBuffer State: " + bm.listBuffer());

		Buffer b4=bm.pin(new Block("student.tbl", 1));
		if(b4.getBlockNum() != b1.getBlockNum())
			System.err.print("buffer mgr error2!..");
		// unpin all remainings
		bm.unpin(b0); // lru list : (LRU)b0
		bm.unpin(b1); // lru list : (LRU)b0,b1
		bm.unpin(b2); // lru list : (LRU)b0,b1,b2
		bm.unpin(b3); // lru list : (LRU)b0,b1,b2,b3
		
		System.out.println("Buffer State: " + bm.listBuffer()); 
		System.out.println("Number of empty buffers: " + bm.available()); 
	}

}
