package simpledb.log;

import static simpledb.file.Page.INT_SIZE;

import java.util.ArrayList;
import java.util.Iterator;

import simpledb.buffer.Buffer;
import simpledb.file.Block;
import simpledb.file.FileMgr;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

public class LogForwardIterator implements Iterator<BasicLogRecord> {
	
	private Block blk;
	private Page pg = new Page();
	private int currentrec;
	private ArrayList<Integer> offsetList;
	
	LogForwardIterator(String logfile) {
	    this.blk = new Block(logfile, 0);
	    pg.read(blk);
	    currentrec = pg.getInt(LogMgr.LAST_POS);
		offsetList = new ArrayList<Integer>();
		createOffsetList();
	}

	@Override
	public boolean hasNext() {
		FileMgr fm = SimpleDB.fileMgr();
		int fileSize = fm.size(blk.fileName());
		
		int lastOffset = pg.getInt(LogMgr.LAST_POS);//Son offsetten sonra record olmadigi icin okuma yapilmamali
		if(currentrec == pg.getInt(lastOffset) && blk.number() == (fileSize-1))
			return false;
		else
			return true;
	}

	@Override
	public BasicLogRecord next() {
		int lastOffset = pg.getInt(LogMgr.LAST_POS);//Son offsetten sonra record olmadigi icin okuma yapilmamali
		if (currentrec == pg.getInt(lastOffset)) 
			moveToNextBlock();
		
		currentrec = offsetList.remove(0);
	    return new BasicLogRecord(pg, currentrec+INT_SIZE);			
	}
	
	 private void moveToNextBlock() {
		 blk = new Block(blk.fileName(), blk.number()+1);
		 pg.read(blk);
		 currentrec = pg.getInt(LogMgr.LAST_POS);
		 createOffsetList();		 
	 }
	 
	 private void createOffsetList() {
		 offsetList.clear();		 
		 
		 while(currentrec != 0) {
			 currentrec = pg.getInt(currentrec);
			 offsetList.add(0, currentrec);
		 }		 
	 }
	 
	 public void remove() {
		 throw new UnsupportedOperationException();
	 }

}
