package simpledb.test;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.buffer.ZeroPageFormatter;
import simpledb.file.Block;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

public class LRUTest1 {

	@Test
	public void test() {
		SimpleDB.initFileMgr("studentdb");
		FileMgr fm = SimpleDB.fileMgr();
		
		Page mypage = new Page();
		
		//myfile'a 100 tane blok eklendi.
		for(int i=0; i<100; i++) { 
			mypage.append("myfile");
		}
		
		int fileSize = fm.size("myfile");		
		System.out.println("myfile size:" + fileSize + "\n");
		
		BufferMgr bm = new BufferMgr(7);  // initialize a new "smaller" buffer pool.
		System.out.println("Buffer State: " + bm.listBuffer());
		
		Buffer b0 = bm.pin(new Block("myfile", 0));
		Buffer b1 = bm.pin(new Block("myfile", 1));
		System.out.println("Buffer State: " + bm.listBuffer());
		Buffer b2 = bm.pin(new Block("myfile", 2));
		Buffer b3 = bm.pin(new Block("myfile", 3));
		System.out.println("Buffer State: " + bm.listBuffer());
		
		bm.unpin(b2);  // lru list: b2
		bm.unpin(b0);  // lru list: b2,b0
		bm.unpin(b1);  // lru list: b2,b0,b1
		bm.unpin(b3);  // lru list: b2,b0,b1,b3
		
		System.out.println("Buffer State: " + bm.listBuffer());  // all buffers are un-pinned
		System.out.println("# of available buffers: " + bm.available() + "\n");  // should print 4
		
		Buffer b4 = bm.pin(new Block("myfile", 4));  // b2 should be selected.
		System.out.println("Buffer State: " + bm.listBuffer());

		Buffer b5 = bm.pin(new Block("myfile", 5));   // b0 should be selected.
		System.out.println("Buffer State: " + bm.listBuffer());
		
		bm.pin(new Block("myfile", 1));   // already in the pool. lru list becames: b3
		System.out.println("Buffer State: " + bm.listBuffer());
		bm.unpin(b1);//lru list becames: b3,b1
		System.out.println("Buffer State: " + bm.listBuffer());
		
		Buffer b6 = bm.pin(new Block("myfile", 6)); // b3 should be selected.
		System.out.println("Buffer State: " + bm.listBuffer());
		
		b6 = bm.pin(new Block("myfile", 6));
		System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 2
		
		b6 = bm.pin(new Block("myfile", 6));
		System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 3
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 2
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 pin counter becaomes 1
		bm.unpin(b6);System.out.println("Buffer State: " + bm.listBuffer());// b3 becaomes un-pinned. 
		
		bm.pin(new Block("myfile", 1));  // lru list : b3, findExsistingBuffer finds b1. should remove from unpinned list.
		bm.pin(new Block("myfile", 10)); // lru list : boþ
		System.out.println("Buffer State: " + bm.listBuffer());
		
		// unpin all remainings
		bm.unpin(b0); // lru list : (LRU)b0
		bm.unpin(b1); // lru list : (LRU)b0,b1
		bm.unpin(b2); // lru list : (LRU)b0,b1,b2
		bm.unpin(b3); // lru list : (LRU)b0,b1,b2,b3
		System.out.println("Buffer State: " + bm.listBuffer()); // All are shown as unpinned..
	}

}
