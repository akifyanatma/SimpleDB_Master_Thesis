package simpledb.parse;

import simpledb.query.*;

//Akif
public class DropIndexData {
	private String tblname;
	private String fldname;
	private String idxname;
	private boolean allIdx;//Tablonun uzerinde olustulan tum indexlerin drop edilip edilmeyecegini belirtiyor.
	
	public DropIndexData(String tblname, String fldname, String idxname) {
		this.tblname = tblname;		
		this.fldname = fldname;
		this.idxname = idxname;
		this.allIdx = false;
	}
	
	public DropIndexData(String tblname) {
		this.tblname = tblname;
		this.allIdx= true;
		this.fldname = "";
		this.idxname = "";
	}
	
	public String tableName() {
		return tblname;
	}
	
	public String fieldName() {
		return fldname;
	}
	
	public String indexName() {
		return idxname;
	}
	
	public boolean allIndex() {
		return allIdx;
	}
}
