package simpledb.buffer;

import java.util.ArrayList;
import java.util.HashMap;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
   //Akif
   //unpinned edilmis olan bufferlari tutacak olan liste
   private ArrayList<Buffer> unpinnedBuffList;
   //Akif
   //Buffer havuzunda alinan tamponlari tutmak icin map
   private HashMap<Block, Buffer> loadedBuffMap;
   
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
	   unpinnedBuffList = new ArrayList<Buffer>();
	   numAvailable = numbuffs;
	   for (int i=0; i<numbuffs; i++) {
		   bufferpool[i] = new Buffer(i); //Bufferlara id eklendi.
		   unpinnedBuffList.add(bufferpool[i]); //Ilk basta tum bufferlar unpinned olarak listeye ekleniyor.
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
   synchronized Buffer pin(Block blk) {
	   Buffer buff = findExistingBuffer(blk);
	   	if (buff == null) {
	   		buff = chooseUnpinnedBuffer();
	   		if (buff == null)
	   			return null;
	   		
	   		Block oldBlock = buff.block();//buffer icinde onceden bulunan block
	   		
	   		buff.assignToBlock(blk);
	   		
	   		if(oldBlock != null)
		    	   loadedBuffMap.remove(oldBlock); //Artik Map'te eski blok anahtar olarak kullanilmamalidir.
	   		
	   		loadedBuffMap.put(blk, buff);//Yeni blok anahtar olarak kullanilarak buffer Map'e eklenir.
	   	}
	   	if (!buff.isPinned())
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
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
	   Buffer buff = chooseUnpinnedBuffer();
	   if (buff == null)
		   return null;
	   
	   Block oldBlock = buff.block();//buffer icinde onceden bulunan block
	   
	   buff.assignToNew(filename, fmtr);
	   
	   Block newBlock = buff.block();//buffer icine yeni eklenmek istenen blok
	   
	   if(oldBlock != null)
		   loadedBuffMap.remove(oldBlock); //Artik Map'te eski blok anahtar olarak kullanilmamalidir.
	   
	   loadedBuffMap.put(newBlock, buff); //Yeni blok anahtar olarak kullanilarak buffer Map'e eklenir.
	   
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
	   if (!buff.isPinned()) {
		   unpinnedBuffList.add(buff); //Unpinned edilen buffer listenin sonuna eklendi.
		   numAvailable++;		   
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
   private Buffer chooseUnpinnedBuffer() {
	   if(unpinnedBuffList.size() != 0) 
		   return unpinnedBuffList.remove(0); //listedeki ilk buffer kullanilacak
	   else 
		   return null;
   }
}
