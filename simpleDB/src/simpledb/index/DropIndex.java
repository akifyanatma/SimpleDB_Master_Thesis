package simpledb.index;

import java.util.ArrayList;
import java.util.Map;

import simpledb.metadata.IndexInfo;
import simpledb.metadata.StatInfo;
import simpledb.parse.DropIndexData;
import simpledb.query.TableScan;
import simpledb.query.UpdateScan;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class DropIndex {
	
	//Bir tablonun bir fieldi uzerindeki belli isimdeki index(ya da index'ler cunku ayni tablonun ayni fieldi uzerinde ayni isimde farkli tipte index olabilir) drop edilebilir.
	//Bir tablo uzerindeki tum indeksler drop edilebilir.
	//Drop edilen index icin olusturulmus olan dosyalar silinir.
	public static int dropIndex(String tblname, String fldname, String idxname, boolean allIdx, Transaction tx) {
		int success = 0; //Drop islemi gerceklesmezse 0, gerceklesirse index katalog tabledan kac tane kayit silindigi dondurulecek
		
		//Tablo uzerinde daha once olusturulmus olan indexler icin index info nesneleri getiriliyor
		//Bir field icin birden fazla tipte index olusturulmus olabilir, bu yuzden her bir field icin liste getiriliyor
		Map<String, ArrayList<IndexInfo>> indexInfoMap = SimpleDB.mdMgr().getIndexInfo_(tblname, tx);
			   
	    //Sadece bir field uzerindeki belli isimdeki index(ya da index'ler cunku ayni isimde farkli tipte olabilir) drop edilecek
		if(allIdx == false) {
			ArrayList<IndexInfo> indexInfoList = indexInfoMap.get(fldname);	
			if(indexInfoList == null)
				return success; //Belirtilen field icin zaten hic index olusturulmamis, drop edilecek index yok
			for(IndexInfo ii : indexInfoList)
				if(ii.getIndexName().equals(idxname))
					success += dropOneIndex(tblname, fldname, idxname, ii.getType(), tx);							   
		}
		//Tablo uzerindeki tum indexler drop edilecek
		else {				
			for(String fieldName : indexInfoMap.keySet()) {
				ArrayList<IndexInfo> indexInfoList = indexInfoMap.get(fieldName);
				for(IndexInfo ii : indexInfoList) 
					success += dropOneIndex(tblname, fieldName, ii.getIndexName(), ii.getType(), tx);
			}
				   
		}
		
	   return success;		
	}
	
	//Index katalog table'dan belli isimdeki belli fielddaki belli index ismine sahip ve belli index tipindeki kayit drop edilir
	//Index ile ilgili dosyalar silinir.
	private static int dropOneIndex(String tblname, String fldname, String idxname, String idxtype, Transaction tx) {
		TableInfo ti = SimpleDB.mdMgr().getTableInfo("idxcat", tx); //index katalog table icin		
		UpdateScan s = new TableScan(ti, tx);
				
		int success = 0;
		
		while (s.next()) {
			if(s.getString("tablename").equals(tblname) && s.getString("fieldname").equals(fldname) && s.getString("indexname").equals(idxname) && s.getString("indextype").equals(idxtype)){				
				s.delete();	
				if(idxtype.equals("btree")) {
					SimpleDB.fileMgr().deleteFile(idxname+"dir.tbl");
					SimpleDB.fileMgr().deleteFile(idxname+"leaf.tbl");
				}
				else if(idxtype.equals("shash")) {
					for(int i=0; i<100; i++)
						SimpleDB.fileMgr().deleteFile(idxname+String.valueOf(i)+".tbl");
				}
				
				success = 1;
				break;
			}
		}
		
		return success;
	}
}
