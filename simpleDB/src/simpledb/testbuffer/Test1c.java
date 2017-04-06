package simpledb.testbuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

///**
//This testcase tests LRU/MRU policy.	
//
//* Buffer state format: "buffer_id"\"block_no"\"pin_counter" 
//* If buffer is EMPTY, prints nothing: "buffer_id"\___-___\"u"
//* "pin_counter"= 1,2,3,...
//* IF buffer is unpinned, prints "u".
//* 
//Ýlk 4 istekten sonra geliþ sýrasýna göre – yani 0,3,2,5-- tamponlar unpin olmaktadýr. 
//Bundan sonraki istekler tampona yerleþtikten hemen sonra – baþka bir tampon unpin olmadan—
//unpin olmaktadýrlar.
//
//* The output for this test case:
//  0325405050 istekleri için Buffer son durumu::
//	lru-Buffer State:  0\student-4\"u"  1\student-0\"u"  2\student-2\"u"  3\student-5\"u"
//Disk IO = 6
//	mru-Buffer State: 0\student-4\"u"  1\student-3\"u"  2\student-2\"u"  3\student-0\"u"  
//	Disk IO = 9

//	0325405251 istekleri için Buffer son durumu::
//	lru-Buffer State: 0\student-1\"u"  1\student-0\"u" 2\student-2\"u"  3\student-5\"u"  
//	Disk IO = 7
//	mru-Buffer State: 0\student-4\"u"  1\student-2\"u"  2\student-3\"u"  3\student-1\"u"  
//	Disk IO = 7
//* 
//* @param args
//*/


public class Test1c {

	@Test
	public void test() {
		InitTable.initData("BufMgr100",100);  // contains "student" table
		BufferMgr bm = new BufferMgr(7);  // initialize a new "smaller" buffer pool. Last 3 buffers are used for log.
		bm.setReplacementPolicy(SimpleDB.bufferReplacementPolicy.MRU);
		
		System.out.println("Buffer State: " + bm.listBuffer());
		int DiskIO = SimpleDB.fileMgr().getBlockReadCount();
		
		//0325405050 istek sirasi
//		Buffer b0 = bm.pin(new Block("student.tbl", 0));
//		Buffer b1 = bm.pin(new Block("student.tbl", 3));
//		Buffer b2 = bm.pin(new Block("student.tbl", 2));
//		Buffer b3 = bm.pin(new Block("student.tbl", 5));
//		bm.unpin(b0);
//		bm.unpin(b1);
//		bm.unpin(b2);
//		bm.unpin(b3);
//		Buffer b5 = bm.pin(new Block("student.tbl", 4));
//		bm.unpin(b5);
//		Buffer b6 = bm.pin(new Block("student.tbl", 0));
//		bm.unpin(b6);
//		Buffer b7 = bm.pin(new Block("student.tbl", 5));
//		bm.unpin(b7);
//		Buffer b8 = bm.pin(new Block("student.tbl", 0));
//		bm.unpin(b8);
//		Buffer b9 = bm.pin(new Block("student.tbl", 5));
//		bm.unpin(b9);
//		Buffer b10 = bm.pin(new Block("student.tbl", 0));
//		bm.unpin(b10);
		
		//0325405251 istek sirasi
		Buffer b0 = bm.pin(new Block("student.tbl", 0));
		Buffer b1 = bm.pin(new Block("student.tbl", 3));
		Buffer b2 = bm.pin(new Block("student.tbl", 2));
		Buffer b3 = bm.pin(new Block("student.tbl", 5));
		bm.unpin(b0);
		bm.unpin(b1);
		bm.unpin(b2);
		bm.unpin(b3);
		Buffer b5 = bm.pin(new Block("student.tbl", 4));
		bm.unpin(b5);
		Buffer b6 = bm.pin(new Block("student.tbl", 0));
		bm.unpin(b6);
		Buffer b7 = bm.pin(new Block("student.tbl", 5));
		bm.unpin(b7);
		Buffer b8 = bm.pin(new Block("student.tbl", 2));
		bm.unpin(b8);
		Buffer b9 = bm.pin(new Block("student.tbl", 5));
		bm.unpin(b9);
		Buffer b10 = bm.pin(new Block("student.tbl", 1));
		bm.unpin(b10);
		
		
		System.out.println("Buffer State: " + bm.listBuffer());
		//System.out.println(SimpleDB.BUFFER_REPLACEMENT_POLICY + "-Buffer State: " + bm.listBuffer());
		System.out.println ("Disk IO = " + String.valueOf(SimpleDB.fileMgr().getBlockReadCount() - DiskIO));
		
		
	}

}
