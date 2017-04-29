package simpledb.log;

import static simpledb.file.Page.INT_SIZE;

import java.util.ArrayList;
import java.util.Iterator;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

public class LogForwardIterator implements Iterator<BasicLogRecord> {
	
	private Block blk;
	//private Page pg = new Page();
	private Buffer buff; //Akif
	private int currentrec;
	private ArrayList<Integer> offsetList;
	
	LogForwardIterator(String logfile) {
	    this.blk = new Block(logfile, 0);
	    buff = SimpleDB.bufferMgr().pin(blk, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
	    currentrec = buff.getInt(LogMgr.LAST_POS);
		offsetList = new ArrayList<Integer>();
		createOffsetList();
	}

	@Override
	public boolean hasNext() {
		FileMgr fm = SimpleDB.fileMgr();
		int fileSize = fm.size(blk.fileName());
		
		int lastOffset = buff.getInt(LogMgr.LAST_POS);//Son offsetten sonra record olmadigi icin okuma yapilmamali
		if(currentrec == buff.getInt(lastOffset) && blk.number() == (fileSize-1))
			return false;
		else
			return true;
	}

	@Override
	public BasicLogRecord next() {
		int lastOffset = buff.getInt(LogMgr.LAST_POS);//Son offsetten sonra record olmadigi icin okuma yapilmamali
		if (currentrec == buff.getInt(lastOffset)) 
			moveToNextBlock();
		
		currentrec = offsetList.remove(0);
	    return new BasicLogRecord(buff, currentrec+INT_SIZE);			
	}
	
	 private void moveToNextBlock() {
		 BufferMgr bm = SimpleDB.bufferMgr();
		   if(buff != null)
			   bm.unpin(buff);
		 
		 blk = new Block(blk.fileName(), blk.number()+1);
		 buff = bm.pin(blk, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
		 currentrec = buff.getInt(LogMgr.LAST_POS);
		 createOffsetList();		 
	 }
	 
	 private void createOffsetList() {
		 offsetList.clear();		 
		 
		 while(currentrec != 0) {
			 currentrec = buff.getInt(currentrec);
			 offsetList.add(0, currentrec);
		 }		 
	 }
	 
	 public void remove() {
		 throw new UnsupportedOperationException();
	 }
	 
	 public void close() {
		 BufferMgr bm = SimpleDB.bufferMgr();
		 if(buff != null)
			 bm.unpin(buff);
	 }

}
