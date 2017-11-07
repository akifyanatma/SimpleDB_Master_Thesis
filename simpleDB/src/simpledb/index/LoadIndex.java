package simpledb.index;

import static java.sql.Types.INTEGER;

import java.util.ArrayList;
import java.util.Map;

import simpledb.index.btree.BTreeIndex;
import simpledb.index.hash.HashIndex;
import simpledb.query.TableScan;
import simpledb.query.UpdateScan;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.metadata.IndexInfo;

public class LoadIndex {
	
	public static int loadIndex(String tblname, String fldname, String idxname, String idxtype, Transaction tx){	
		Map<String, ArrayList<IndexInfo>> indexInfoMap = SimpleDB.mdMgr().getIndexInfo_(tblname, tx);
		
		ArrayList<IndexInfo> indexInfoList = null;
		if(indexInfoMap.containsKey(fldname) == true) {
			indexInfoList = indexInfoMap.get(fldname);
			
			if(indexInfoList.size() != 0) {	
				//ayni tipte index daha onceden varsa index ekleme islemi yapilamasin ve fonksiyon sifir donsun.
				for(IndexInfo ii : indexInfoList) {
					if(ii.getType().equals(idxtype))
						return 0;
				}
			}
		}
								
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tblname, tx);
		if(ti == null) {
			System.out.println("There is not \""+tblname+"\" in database.");
			return 0;
		}
		
		Schema idxsch = new Schema();
	    if (ti.schema().type(fldname) == INTEGER)
	    	idxsch.addIntField("dataval");
	    else {
	    	int fldlen = ti.schema().length(fldname);
	        idxsch.addStringField("dataval", fldlen);
	    }
		idxsch.addIntField("block");
		idxsch.addIntField("id");
	
		Index idx = null;
		if (idxtype.equalsIgnoreCase("btree"))
			idx = new BTreeIndex(idxname, idxsch, tx);
		else if (idxtype.equalsIgnoreCase("shash"))
			idx = new HashIndex(idxname, idxsch, tx);
		
		if(idx == null)
			return 0;
		
		UpdateScan s = new TableScan(ti, tx);
		System.out.println("loading " + idxname + "...");
		while (s.next()) {
			idx.insert(s.getVal(fldname), s.getRid());
		}
			
		SimpleDB.mdMgr().createIndex(idxname, tblname, fldname, idxtype, tx);
		
		return 1;
	}

}
