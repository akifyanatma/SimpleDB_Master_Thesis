package simpledb.index.planner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import simpledb.record.RID;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.index.Index;
import simpledb.metadata.IndexInfo;
import simpledb.parse.*;
import simpledb.planner.*;
import simpledb.query.*;

/**
 * A modification of the basic update planner.
 * It dispatches each update statement to the corresponding
 * index planner.
 * @author Edward Sciore
 */
public class IndexUpdatePlanner implements UpdatePlanner {
   
//   public int executeInsert(InsertData data, Transaction tx) {
//      String tblname = data.tableName();
//      Plan p = new TablePlan(tblname, tx);
//      
//      // first, insert the record
//      UpdateScan s = (UpdateScan) p.open();
//      s.insert();
//      RID rid = s.getRid();
//      
//      // then modify each field, inserting an index record if appropriate
//      Map<String,IndexInfo> indexes = SimpleDB.mdMgr().getIndexInfo(tblname, tx);
//      Iterator<Constant> valIter = data.vals().iterator();
//      for (String fldname : data.fields()) {
//         Constant val = valIter.next();
//         System.out.println("Modify field " + fldname + " to val " + val);
//         s.setVal(fldname, val);
//         
//         IndexInfo ii = indexes.get(fldname);
//         if (ii != null) {
//            Index idx = ii.open();
//            idx.insert(val, rid);
//            idx.close();
//         }
//      }
//      s.close();
//      return 1;
//   }
	
	//Akif
	//Bir field icin birden fazla index olmasý durumunda hepsine insert islemi yapiliyor.
   public int executeInsert(InsertData data, Transaction tx) {
      String tblname = data.tableName();
      Plan p = new TablePlan(tblname, tx);
      
      // first, insert the record
      UpdateScan s = (UpdateScan) p.open();
      s.insert();
      RID rid = s.getRid();
      
      // then modify each field, inserting an index record if appropriate
      Map<String, ArrayList<IndexInfo>> indexes = SimpleDB.mdMgr().getIndexInfo_(tblname, tx);
      Iterator<Constant> valIter = data.vals().iterator();
      for (String fldname : data.fields()) {
         Constant val = valIter.next();
         System.out.println("Modify field " + fldname + " to val " + val);
         s.setVal(fldname, val);
         
         ArrayList<IndexInfo> indexList = indexes.get(fldname);
         
         if(indexList != null) {
	         for(IndexInfo ii : indexList) {
	            Index idx = ii.open();
	            idx.insert(val, rid);
	            idx.close();
	         }
         }
      }
      s.close();
      return 1;
   }
   
//   public int executeDelete(DeleteData data, Transaction tx) {
//      String tblname = data.tableName();
//      Plan p = new TablePlan(tblname, tx);
//      p = new SelectPlan(p, data.pred());
//      Map<String,IndexInfo> indexes = SimpleDB.mdMgr().getIndexInfo(tblname, tx);
//      
//      UpdateScan s = (UpdateScan) p.open();
//      int count = 0;
//      while(s.next()) {
//         // first, delete the record's RID from every index
//         RID rid = s.getRid();
//         for (String fldname : indexes.keySet()) {
//            Constant val = s.getVal(fldname);
//            Index idx = indexes.get(fldname).open();
//            idx.delete(val, rid);
//            idx.close();
//         }
//         // then delete the record
//         s.delete();
//         count++;
//      }
//      s.close();
//      return count;
//   }
   
   //Akif
   //bir field icin birden fazla index olmasi durumunda her bir indexten silme yapilir.
   public int executeDelete(DeleteData data, Transaction tx) {
      String tblname = data.tableName();
      Plan p = new TablePlan(tblname, tx);
      p = new SelectPlan(p, data.pred());
      Map<String, ArrayList<IndexInfo>> indexes = SimpleDB.mdMgr().getIndexInfo_(tblname, tx);
      
      UpdateScan s = (UpdateScan) p.open();
      int count = 0;
      while(s.next()) {
         // first, delete the record's RID from every index
         RID rid = s.getRid();
         for (String fldname : indexes.keySet()) {
            Constant val = s.getVal(fldname);
            ArrayList<IndexInfo> indexList = indexes.get(fldname);
            for(IndexInfo ii : indexList) {
            	Index idx = ii.open();
            	idx.delete(val, rid);
                idx.close();
            }
         }
         // then delete the record
         s.delete();
         count++;
      }
      s.close();
      return count;
   }
   
//   public int executeModify(ModifyData data, Transaction tx) {
//      String tblname = data.tableName();
//      String fldname = data.targetField();
//      Plan p = new TablePlan(tblname, tx);
//      p = new SelectPlan(p, data.pred());
//      
//      IndexInfo ii = SimpleDB.mdMgr().getIndexInfo(tblname, tx).get(fldname);
//      Index idx = (ii == null) ? null : ii.open();
//      
//      UpdateScan s = (UpdateScan) p.open();
//      int count = 0;
//      while(s.next()) {
//         // first, update the record
//         Constant newval = data.newValue().evaluate(s);
//         Constant oldval = s.getVal(fldname);
//         s.setVal(data.targetField(), newval);
//         
//         // then update the appropriate index, if it exists
//         if (idx != null) {
//            RID rid = s.getRid();
//            idx.delete(oldval, rid);
//            idx.insert(newval, rid);
//         }
//         count++;
//      }
//      if (idx != null) idx.close();
//      s.close();
//      return count;
//   }
   
   //Akif
   //Bir field icin birden fazla index olmasi durumunda tum indexlerin modifiye edilmesi durumu ele aliniyor.
   public int executeModify(ModifyData data, Transaction tx) {
      String tblname = data.tableName();
      String fldname = data.targetField();
      Plan p = new TablePlan(tblname, tx);
      p = new SelectPlan(p, data.pred());
      
      ArrayList<IndexInfo> indexList = SimpleDB.mdMgr().getIndexInfo_(tblname, tx).get(fldname);
          
      UpdateScan s = (UpdateScan) p.open();
      int count = 0;
      while(s.next()) {
         // first, update the record
         Constant newval = data.newValue().evaluate(s);
         Constant oldval = s.getVal(fldname);
         s.setVal(data.targetField(), newval);
         
         // then update the appropriate index        
         if(indexList != null) {
        	 for(IndexInfo ii : indexList) {
        		 Index idx = ii.open();
        		 RID rid = s.getRid();
                 idx.delete(oldval, rid);
                 idx.insert(newval, rid);
                 idx.close();
        	 }
         }
              
         count++;
      }
        
      s.close();
      return count;
   }
   
   public int executeCreateTable(CreateTableData data, Transaction tx) {
      SimpleDB.mdMgr().createTable(data.tableName(), data.newSchema(), tx);
      return 0;
   }
   
   public int executeCreateView(CreateViewData data, Transaction tx) {
      SimpleDB.mdMgr().createView(data.viewName(), data.viewDef(), tx);
      return 0;
   }
   
   public int executeCreateIndex(CreateIndexData data, Transaction tx) {
      SimpleDB.mdMgr().createIndex(data.indexName(), data.tableName(), data.fieldName(), tx);
      return 0;
   }
   
   //Akif
   public int executeDropIndex(DropIndexData data, Transaction tx) {
	   //Interface'de yer aldigi icin bu fonksiyon buraya eklendi. ýcnin dolu hali BasicUpdatePlanner'da var.
	   return 0;
   }

}
