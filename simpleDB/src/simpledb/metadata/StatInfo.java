package simpledb.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds three pieces of statistical information about a table:
 * the number of blocks, the number of records,
 * and the number of distinct values for each field. 
 * @author Edward Sciore
 */
public class StatInfo {
   private int numBlocks;
   private int numRecs;
   
   //Akif
   private Map<String,Integer> distinctValues;
   
   /**
    * Creates a StatInfo object.
    * Note that the number of distinct values is not
    * passed into the constructor.
    * The object fakes this value.
    * @param numblocks the number of blocks in the table
    * @param numrecs the number of records in the table
    */
//   public StatInfo(int numblocks, int numrecs) {
//      this.numBlocks = numblocks;
//      this.numRecs   = numrecs;
//   }
   
   //Akif
   public StatInfo(int numblocks, int numrecs) {
	   this.numBlocks = numblocks;
	   this.numRecs   = numrecs;
	   distinctValues = new HashMap<String,Integer>();
   }
     
   /**
    * Returns the estimated number of blocks in the table.
    * @return the estimated number of blocks in the table
    */
   public int blocksAccessed() {
      return numBlocks;
   }
   
   /**
    * Returns the estimated number of records in the table.
    * @return the estimated number of records in the table
    */
   public int recordsOutput() {
      return numRecs;
   }
   
   //Akif
   public void setDistinctValues(String fldname, int value){
	   distinctValues.put(fldname, value);
   }
   
   /**
    * Returns the estimated number of distinct values
    * for the specified field.
    * In actuality, this estimate is a complete guess.
    * @param fldname the name of the field
    * @return a guess as to the number of distinct field values
    */
//   public int distinctValues(String fldname) {
//      return 1 + (numRecs / 3);
//   }
   
   //Akif
   public int distinctValues(String fldname){
	   if(distinctValues.containsKey(fldname))
		   return distinctValues.get(fldname);
	   else
		   return 1 + (numRecs / 3);
		   
   }
}
