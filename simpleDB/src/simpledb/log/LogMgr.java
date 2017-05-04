package simpledb.log;

import simpledb.server.SimpleDB;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.buffer.PageFormatter;
import simpledb.buffer.ZeroPageFormatter;
import simpledb.file.*;
import static simpledb.file.Page.*;
import java.util.*;

/**
 * The low-level log manager.
 * This log manager is responsible for writing log records
 * into a log file.
 * A log record can be any sequence of integer and string values.
 * The log manager does not understand the meaning of these
 * values, which are written and read by the
 * {@link simpledb.tx.recovery.RecoveryMgr recovery manager}.
 * @author Edward Sciore
 */
public class LogMgr implements Iterable<BasicLogRecord> {
   /**
    * The location where the pointer to the last integer in the page is.
    * A value of 0 means that the pointer is the first value in the page.
    */
   public static final int LAST_POS = 0;

   private String logfile;
   //private Page mypage = new Page();
   private Buffer mybuffer; //Akif
   private Block currentblk;
   private int currentpos;
   
   private LSN mostRecentLSN = new LSN(-1,-1); //Akif, en son flush edilen log blogu numarasi ve offset bilgileri
   private LSN currentLSN = new LSN(-1,-1); //Akif, en son eklenen kayit icin log blogu numarasi ve offset bilgileri
   
   public Iterator<BasicLogRecord> rewardIter; //Akif
   public Iterator<BasicLogRecord> forwardIter; //Akif
   
   /**
    * Creates the manager for the specified log file.
    * If the log file does not yet exist, it is created
    * with an empty first block.
    * This constructor depends on a {@link FileMgr} object
    * that it gets from the method
    * {@link simpledb.server.SimpleDB#fileMgr()}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until
    * {@link simpledb.server.SimpleDB#initFileMgr(String)}
    * is called first.
    * @param logfile the name of the log file
    */
//   public LogMgr(String logfile) {
//      this.logfile = logfile;
//      int logsize = SimpleDB.fileMgr().size(logfile);
//      if (logsize == 0)
//         appendNewBlock();
//      else {
//         currentblk = new Block(logfile, logsize-1);
//         mypage.read(currentblk);
//         currentpos = getLastRecordPosition() + INT_SIZE;
//      }
//   }
   
   //Akif
   public LogMgr(String logfile) {
	  this.logfile = logfile;
   }
   
   /**
    * Ensures that the log records corresponding to the
    * specified LSN has been written to disk.
    * All earlier log records will also be written to disk.
    * @param lsn the LSN of a log record
    */
//   public void flush(int lsn) {
//      if (lsn >= currentLSN())
//         flush();
//   }
   
   //Akif
   public void flush(LSN lsn) {
	   if (mostRecentLSN.compareTo(lsn) < 0) 
		   flush(); 
   }
   
   /**
    * Returns an iterator for the log records,
    * which will be returned in reverse order starting with the most recent.
    * @see java.lang.Iterable#iterator()
    */
//   public synchronized Iterator<BasicLogRecord> iterator() {
//      flush();
//      return new LogIterator(currentblk);
//   }
   
   //Akif
   public synchronized Iterator<BasicLogRecord> iterator() {
	   rewardIter = new LogIterator(currentblk);
	   return rewardIter;
   }
   
   //Akif
   public synchronized Iterator<BasicLogRecord> forwardIterator() {
	   forwardIter = new LogForwardIterator(logfile);
	   return forwardIter; 	   
   }
   

   /**
    * Appends a log record to the file.
    * The record contains an arbitrary array of strings and integers.
    * The method also writes an integer to the end of each log record whose value
    * is the offset of the corresponding integer for the previous log record.
    * These integers allow log records to be read in reverse order.
    * @param rec the list of values
    * @return the LSN of the final value
    */
//   public synchronized int append(Object[] rec) {
//      int recsize = INT_SIZE;  // 4 bytes for the integer that points to the previous log record
//      for (Object obj : rec)
//         recsize += size(obj);
//      if (currentpos + recsize >= BLOCK_SIZE){ // the log record doesn't fit,
//         flush();        // so move to the next block.
//         appendNewBlock();
//      }
//      for (Object obj : rec)
//         appendVal(obj);
//      finalizeRecord();
//      return currentLSN();
//   }
   
   //Akif
   public synchronized LSN append(Object[] rec) {
	   int recsize = INT_SIZE;  // 4 bytes for the integer that points to the previous log record
	   for (Object obj : rec)
		   recsize += size(obj);
	   if (currentpos + recsize >= BLOCK_SIZE){ // the log record doesn't fit,
		   if(mostRecentLSN.compareTo(currentLSN) < 0)
			   flush();        // so move to the next block.
	       appendNewBlock();
	   }
	   for (Object obj : rec)
	       appendVal(obj);
	   finalizeRecord();
	   currentLSN.setBlkNum(currentblk.number());
	   currentLSN.setOffset(currentpos);
	   return currentLSN;
   }

   /**
    * Adds the specified value to the page at the position denoted by
    * currentpos.  Then increments currentpos by the size of the value.
    * @param val the integer or string to be added to the page
    */
//   private void appendVal(Object val) {
//      if (val instanceof String)
//         mypage.setString(currentpos, (String)val);
//      else
//         mypage.setInt(currentpos, (Integer)val);
//      currentpos += size(val);
//   }
   
   //Akif
   private void appendVal(Object val) {
	  if (val instanceof String)
		  mybuffer.setString(currentpos, (String)val, -1, LSN.DUMMY);
	  else
	      mybuffer.setInt(currentpos, (Integer)val, -1, LSN.DUMMY);
	  currentpos += size(val);
   }

   /**
    * Calculates the size of the specified integer or string.
    * @param val the value
    * @return the size of the value, in bytes
    */
   private int size(Object val) {
      if (val instanceof String) {
         String sval = (String) val;
         return STR_SIZE(sval.length());
      }
      else
         return INT_SIZE;
   }

   /**
    * Returns the LSN of the most recent log record.
    * As implemented, the LSN is the block number where the record is stored.
    * Thus every log record in a block has the same LSN.
    * @return the LSN of the most recent log record
    */
//   private int currentLSN() {
//      return currentblk.number();
//   }
   
   //Akif
   private LSN currentLSN() {
	   return currentLSN;
   }

   /**
    * Writes the current page to the log file.
    */
//   private void flush() {
//      mypage.write(currentblk);
//   }
   
   //Akif
   private void flush() {
	   System.out.println("--------------> flushing log page " + currentblk.number() + "\n");
	   SimpleDB.bufferMgr().writeBuffContent(mybuffer);
	   mostRecentLSN.setBlkNum(currentblk.number());
	   mostRecentLSN.setOffset(currentpos);
   }
   
   /**
    * Clear the current page, and append it to the log file.
    */
//   private void appendNewBlock() {
//      setLastRecordPosition(0);
//      currentpos = INT_SIZE;
//      currentblk = mypage.append(logfile);
//   }
      
   //Akif
   private void appendNewBlock() {
	  BufferMgr bm = SimpleDB.bufferMgr();
	  if(mybuffer != null)
		  bm.unpin(mybuffer);
	  currentpos = INT_SIZE;
	  PageFormatter pf = new ZeroPageFormatter();  
	  mybuffer = bm.pinNew(logfile, pf, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
	  currentblk = mybuffer.block();
	  mostRecentLSN.setBlkNum(currentblk.number());
	  mostRecentLSN.setOffset(0);
	  setLastRecordPosition(0);
   }
   
   /**
    * Sets up a circular chain of pointers to the records in the page.
    * There is an integer added to the end of each log record
    * whose value is the offset of the previous log record.
    * The first four bytes of the page contain an integer whose value
    * is the offset of the integer for the last log record in the page.
    */
//   private void finalizeRecord() {
//      mypage.setInt(currentpos, getLastRecordPosition());
//      setLastRecordPosition(currentpos);
//      currentpos += INT_SIZE;
//   }
   
   //Akif
   private void finalizeRecord() {
	   mybuffer.setInt(currentpos, getLastRecordPosition(), -1, LSN.DUMMY);
	   setLastRecordPosition(currentpos);
	   currentpos += INT_SIZE;
   }

//   private int getLastRecordPosition() {
//      return mypage.getInt(LAST_POS);
//   }
   
   //Akif
   private int getLastRecordPosition() {
	  return mybuffer.getInt(LAST_POS);
   }

//   private void setLastRecordPosition(int pos) {
//      mypage.setInt(LAST_POS, pos);
//   }
   
   //Akif
   private void setLastRecordPosition(int pos) {
	   mybuffer.setInt(LAST_POS, pos, -1, LSN.DUMMY);
   }
   
   //Akif
   public void takeFirstBuffer() {
     int logsize = SimpleDB.fileMgr().size(logfile);
     if (logsize == 0)
        appendNewBlock();
     else {
        currentblk = new Block(logfile, logsize-1);
        mybuffer = SimpleDB.bufferMgr().pin(currentblk, SimpleDB.bufferTypes.LOG_BUFF_TYPE);
        currentpos = getLastRecordPosition() + INT_SIZE;
     }
   }
}