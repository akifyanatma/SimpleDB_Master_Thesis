package simpledb.log;

import static simpledb.file.Page.INT_SIZE;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.*;
import simpledb.server.SimpleDB;

import java.util.Iterator;

/**
 * A class that provides the ability to move through the
 * records of the log file in reverse order.
 * 
 * @author Edward Sciore
 */
public class LogIterator implements Iterator<BasicLogRecord> {
   private Block blk;
   //private Page pg = new Page();
   private Buffer buff; //Akif
   private int currentrec;
   
   /**
    * Creates an iterator for the records in the log file,
    * positioned after the last log record.
    * This constructor is called exclusively by
    * {@link LogMgr#iterator()}.
    */
//   LogIterator(Block blk) {
//      this.blk = blk;
//      pg.read(blk);
//      currentrec = pg.getInt(LogMgr.LAST_POS);
//   }
   
   //Akif
   LogIterator(Block blk) {
	   this.blk = blk;
	   buff = SimpleDB.bufferMgr().pin(blk, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
	   currentrec = buff.getInt(LogMgr.LAST_POS);
   }
   
   /**
    * Determines if the current log record
    * is the earliest record in the log file.
    * @return true if there is an earlier record
    */
   public boolean hasNext() {
      return currentrec>0 || blk.number()>0;
   }
   
   /**
    * Moves to the next log record in reverse order.
    * If the current log record is the earliest in its block,
    * then the method moves to the next oldest block,
    * and returns the log record from there.
    * @return the next earliest log record
    */
//   public BasicLogRecord next() {
//      if (currentrec == 0) 
//         moveToNextBlock();
//      currentrec = pg.getInt(currentrec);
//      return new BasicLogRecord(pg, currentrec+INT_SIZE);
//   }
   
   //Akif
   public BasicLogRecord next() {
	  if (currentrec == 0) 
		  moveToNextBlock();
	  currentrec = buff.getInt(currentrec);
	  return new BasicLogRecord(buff, currentrec+INT_SIZE);
   }
   
   public void remove() {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Moves to the next log block in reverse order,
    * and positions it after the last record in that block.
    */
//   private void moveToNextBlock() {
//      blk = new Block(blk.fileName(), blk.number()-1);
//      pg.read(blk);
//      currentrec = pg.getInt(LogMgr.LAST_POS);
//   }
   
   //Akif
   private void moveToNextBlock() {
	   BufferMgr bm = SimpleDB.bufferMgr();
	   if(buff != null)
		   bm.unpin(buff);
	   blk = new Block(blk.fileName(), blk.number()-1);
	   buff = bm.pin(blk, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
	   currentrec = buff.getInt(LogMgr.LAST_POS);
   }
   
   //Akif
   public void close() {
	   BufferMgr bm = SimpleDB.bufferMgr();
	   if(buff != null)
		   bm.unpin(buff);
   }
}
