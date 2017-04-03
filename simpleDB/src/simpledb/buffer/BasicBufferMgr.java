package simpledb.buffer;

import java.util.ArrayList;
import java.util.HashMap;

import simpledb.file.*;
import simpledb.server.SimpleDB;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
   
   //Akif
   private ArrayList<Buffer> unpinnedLogBuffList; //unpinned edilmis olan log bufferlarini tutacak olan liste
   private ArrayList<Buffer> unpinnedOtherBuffList; //log islemleri haricinde unpinned edilmis olan bufferlari tutacak olan liste
   private HashMap<Block, Buffer> loadedBuffMap; //Buffer havuzunda alinan bufferlari tutmak icin map
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
//   BasicBufferMgr(int numbuffs) {
//      bufferpool = new Buffer[numbuffs];
//      numAvailable = numbuffs;
//      for (int i=0; i<numbuffs; i++)
//         bufferpool[i] = new Buffer();
//   }
   
   //Akif
   BasicBufferMgr(int numbuffs) {
	   bufferpool = new Buffer[numbuffs];
	   loadedBuffMap = new HashMap<Block, Buffer>();
	   unpinnedLogBuffList = new ArrayList<Buffer>();
	   unpinnedOtherBuffList = new ArrayList<Buffer>();
	   int numLogBuffs = (int) (numbuffs*0.1);
	   if(numLogBuffs<2)
		   numLogBuffs = 3;
	   numAvailable = numbuffs-numLogBuffs ; //Log icin ayrilan bufferlar available gozukmuyor
	      
	   for (int i=0; i<numbuffs; i++) {
		   if(i<numLogBuffs) {
			   bufferpool[i] = new Buffer(i, SimpleDB.bufferTypes.LOG_BUFF_TYPE); //Bufferlara id ve tip eklendi.
			   unpinnedLogBuffList.add(bufferpool[i]);
		   }
		   else {
			   bufferpool[i] = new Buffer(i, SimpleDB.bufferTypes.OTHER_BUFF_TYPE); //Bufferlara id ve tip eklendi.
			   unpinnedOtherBuffList.add(bufferpool[i]);
		   }		   
	   }	   
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
//   synchronized Buffer pin(Block blk) {
//      Buffer buff = findExistingBuffer(blk);
//      if (buff == null) {
//         buff = chooseUnpinnedBuffer();
//         if (buff == null)
//            return null;
//         buff.assignToBlock(blk);
//      }
//      if (!buff.isPinned())
//         numAvailable--;
//      buff.pin();
//      return buff;
//   }
   
   //Akif
   synchronized Buffer pin(Block blk, SimpleDB.bufferTypes pBuffType) {
	   Buffer buff = findExistingBuffer(blk);
	   
	   //Aranan blok map icindeki bufferlardan birinde bulunmussa ve unpinned listelerinde de yer alýyorsa
	   //unpinned listelerinden cikarilmalidir.
	   if(buff != null) {
		   if(pBuffType == SimpleDB.bufferTypes.LOG_BUFF_TYPE) {
			   unpinnedLogBuffList.remove(buff);
		   }
		   else {
			   unpinnedOtherBuffList.remove(buff);
		   }
	   }
		   
	   
	   	if (buff == null) {
	   		buff = chooseUnpinnedBuffer(pBuffType);
	   		if (buff == null)
	   			return null;
	   		
	   		Block oldBlock = buff.block();//buffer icinde onceden bulunan block
	   		
	   		buff.assignToBlock(blk);
	   		
	   		if(oldBlock != null)
		    	   loadedBuffMap.remove(oldBlock); //Artik Map'te eski blok anahtar olarak kullanilmamalidir.
	   		
	   		loadedBuffMap.put(blk, buff);//Yeni blok anahtar olarak kullanilarak buffer Map'e eklenir.
	   	}
	   	if (!buff.isPinned() && pBuffType==SimpleDB.bufferTypes.OTHER_BUFF_TYPE)
	   		numAvailable--;
	    buff.pin();
	    return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
//   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
//      Buffer buff = chooseUnpinnedBuffer();
//      if (buff == null)
//         return null;
//      buff.assignToNew(filename, fmtr);
//      numAvailable--;
//      buff.pin();
//      return buff;
//   }
   
   //Akif
   synchronized Buffer pinNew(String filename, PageFormatter fmtr, SimpleDB.bufferTypes pBuffType) {
	   Buffer buff = chooseUnpinnedBuffer(pBuffType);
	   if (buff == null)
		   return null;
	   
	   Block oldBlock = buff.block();//buffer icinde onceden bulunan block
	   
	   buff.assignToNew(filename, fmtr);
	   
	   Block newBlock = buff.block();//buffer icine yeni eklenmek istenen blok
	   
	   if(oldBlock != null)
		   loadedBuffMap.remove(oldBlock); //Artik Map'te eski blok anahtar olarak kullanilmamalidir.
	   
	   loadedBuffMap.put(newBlock, buff); //Yeni blok anahtar olarak kullanilarak buffer Map'e eklenir.
	   if (pBuffType==SimpleDB.bufferTypes.OTHER_BUFF_TYPE)
		   numAvailable--;
	   buff.pin();
	   return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
//   synchronized void unpin(Buffer buff) {
//      buff.unpin();
//      if (!buff.isPinned())
//         numAvailable++;
//   }
   
   //Akif
   synchronized void unpin(Buffer buff) {
	   buff.unpin();
	   
	   if(!buff.isPinned()) {
		   if(buff.getType() == SimpleDB.bufferTypes.LOG_BUFF_TYPE) {
			   unpinnedLogBuffList.add(buff); //Unpinned edilen buffer listenin sonuna eklendi.
			                                  //Available buffer sayisi artirilmiyor.
		   }
		   else {
			   unpinnedOtherBuffList.add(buff); //Unpinned edilen buffer listenin sonuna eklendi.
			   numAvailable++;	
		   }			   
	   }
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
//   private Buffer findExistingBuffer(Block blk) {
//      for (Buffer buff : bufferpool) {
//         Block b = buff.block();
//         if (b != null && b.equals(blk))
//            return buff;
//      }
//      return null;
//   }
   
   //Akif
   //Aranan blok map'te var mi diye bakilmaktadir.
   private Buffer findExistingBuffer(Block blk) {
	   if(loadedBuffMap.containsKey(blk))
		   return loadedBuffMap.get(blk);	
	   else 
		   return null; 
   }
   
//   private Buffer chooseUnpinnedBuffer() {
//      for (Buffer buff : bufferpool)
//         if (!buff.isPinned())
//         return buff;
//      return null;
//   }
   
   //Akif
   private Buffer chooseUnpinnedBuffer(SimpleDB.bufferTypes pBuffType) {	   
	   //Aranan listedeki ilk buffer kullanilacak
	   if(pBuffType == SimpleDB.bufferTypes.LOG_BUFF_TYPE) {
		   if(unpinnedLogBuffList.size() != 0)
			   return unpinnedLogBuffList.remove(0);
		   else
			   return null;
	   }
	   else {
		   if(unpinnedOtherBuffList.size() != 0)
			   return unpinnedOtherBuffList.remove(0);
		   else
			   return null;
	   }	   
   }
   
   //Akif
   public void writeBuffContent(Buffer buff)
   {
	   buff.writeContent();
   }
   
   //Akif
   public String listBuffer() {	   
	   int id;
	   String fileName ="";
	   int blockNo;
	   int pinCount;
	   String bufferStates = "";
	   
	   for (Buffer buff : bufferpool) {
		   id = buff.getId();
		   bufferStates += String.valueOf(id);
		   bufferStates += "\\";
		   
		   Block block = buff.block();
		   if(block != null) {
			   fileName = block.fileName();
			   blockNo = block.number();			   			   
			   bufferStates += fileName;
			   bufferStates += "-";
			   bufferStates += String.valueOf(blockNo);			   		   
		   }
		   else {
			   bufferStates += "...-..."; //Buffer henuz kullanilmamis
		   }
		   bufferStates += "\\";
		   
		   pinCount = buff.getPins();
		   if(pinCount == 0) {
			   bufferStates += "u  ";
		   }
		   else {
			   bufferStates += String.valueOf(pinCount);	
			   bufferStates += "  ";
		   }
	   } 
	   bufferStates += "\n";
	   
	   return bufferStates;
   }
}
