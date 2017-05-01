package simpledb.testbuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.buffer.PageFormatter;
import simpledb.buffer.ZeroPageFormatter;
import simpledb.server.SimpleDB;

public class PageFormatTest {

	@Test
	public void test() {
		
		SimpleDB.initFileLogAndBufferMgr("studentdb");
		BufferMgr bm = SimpleDB.bufferMgr();
		PageFormatter pf = new ZeroPageFormatter();
		Buffer buff1 = bm.pinNew("junk", pf);
		Buffer buff2 = bm.pinNew("junk", pf);
	}

}