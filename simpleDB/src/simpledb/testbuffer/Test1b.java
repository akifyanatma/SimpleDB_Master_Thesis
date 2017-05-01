package simpledb.testbuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;


///**
//This testcase tests LRU policy.
//In this test program new buffer manager is created, last 3 buffer is allocated for log manager.
//But new log manager is not created, so log file's last block is not buffered this these buffers. 
//
//* Buffer state format: "buffer_id"\"file_name - block_no"\"pin_counter" 
//* If buffer is EMPTY, prints nothing: "buffer_id\___-___\"u" "
//* "pin_counter"= 1,2,3,...
//* IF buffer is unpinned, prints "u".
//* 
//* The output for this test case:
//* Buffer State: 0\...-...\"u"  1\...-...\"u" 2\...-...\"u" 3\...-...\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-0\1  1\student.tbl-1\1  2\...-...\"u"  3\...-...\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-0\1  1\student.tbl-1\1  2\student.tbl-2\1  3\student.tbl-3\1  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-0\"u"  1\student.tbl-1\"u"  2\student.tbl-2\"u"  3\student.tbl-3\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* # of available buffers: 4
//* Buffer State: 0\student.tbl-0\"u"  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-3\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-3\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\1  2\student.tbl-4\1  3\student.tbl-3\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-3\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\1  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\2  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\3  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\2  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\1  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\"u"  2\student.tbl-4\1  3\student.tbl-6\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\1  1\student.tbl-1\1  2\student.tbl-4\1  3\student.tbl-10\1  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"  
//* Buffer State: 0\student.tbl-5\"u"  1\student.tbl-1\"u"  2\student.tbl-4\"u"  3\student.tbl-10\"u"  4\...-...\"u"  5\...-...\"u"  6\...-...\"u"
//* 
//*/

public class Test1b {

	@Test
	public void test() {
		InitTable.initData("BufMgr100",100);  // contains "student" table
		BufferMgr bm = new BufferMgr(7);  // initialize a new "smaller" buffer pool. 
		SimpleDB.BUFFER_REPLACEMENT_POLICY = SimpleDB.bufferReplacementPolicy.LRU;
		
		System.out.println("\nBuffer State: " + bm.listBuffer());
		
		Buffer b0 = bm.pin(new Block("student.tbl", 0));
		Buffer b1 = bm.pin(new Block("student.tbl", 1));
		System.out.println("Buffer State: " + bm.listBuffer());
		Buffer b2 = bm.pin(new Block("student.tbl", 2));
		Buffer b3 = bm.pin(new Block("student.tbl", 3));
		System.out.println("Buffer State: " + bm.listBuffer());

		bm.unpin(b2);  // lru list: b2
		bm.unpin(b0);  // lru list: b2,b0
		bm.unpin(b1);  // lru list: b2,b0,b1
		bm.unpin(b3);  // lru list: b2,b0,b1,b3
		
		System.out.println("Buffer State: " + bm.listBuffer());  // all buffers are un-pinned
		System.out.println("# of available buffers: " + bm.available());  // should print 4
//		bm.unpin(b3);    // decrease to negative..but, IGNORE THIS BUG: UPPER SYSTEM MODULES uses Buffer appropriately.
//		System.out.println(bm.listBuffer());
//		System.out.println(bm.available());
		
		Buffer b4 = bm.pin(new Block("student.tbl", 4));  // b2 should be selected.
		System.out.println("\nBuffer State: " + bm.listBuffer());

		Buffer b5 = bm.pin(new Block("student.tbl", 5));   // b0 should be selected.
		System.out.println("Buffer State: " + bm.listBuffer());

		bm.pin(new Block("student.tbl", 1));   // already in the pool. lru list becames: b3
		System.out.println("Buffer State: " + bm.listBuffer());
		bm.unpin(b1);//lru list becames: b3,b1
		System.out.println("Buffer State: " + bm.listBuffer()); 
		
		Buffer b6 = bm.pin(new Block("student.tbl", 6)); // b3 should be selected.
		System.out.println("Buffer State: " + bm.listBuffer());
		
		b6 = bm.pin(new Block("student.tbl", 6));
		System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 2
		
		b6 = bm.pin(new Block("student.tbl", 6));
		System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 3
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 2
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 1
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 becaomes un-pinned. 
		
		bm.pin(new Block("student.tbl", 1));  // lru list : b3, findExsistingBuffer finds b1. should remove from unpinned list.
		bm.pin(new Block("student.tbl", 10)); // lru list : boþ
		System.out.println("Buffer State: " + bm.listBuffer());
		
		// unpin all remainings
		bm.unpin(b0); // lru list : (LRU)b0
		bm.unpin(b1); // lru list : (LRU)b0,b1
		bm.unpin(b2); // lru list : (LRU)b0,b1,b2
		bm.unpin(b3); // lru list : (LRU)b0,b1,b2,b3
		System.out.println("Buffer State: " + bm.listBuffer()); // All are shown as unpinned..
	}

}
