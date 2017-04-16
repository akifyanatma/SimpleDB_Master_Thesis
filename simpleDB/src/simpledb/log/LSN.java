package simpledb.log;

public class LSN implements Comparable<LSN> {
	private int blknum;
	private int offset;
	
	public static LSN DUMMY = new LSN(0,0);
	
	
	public LSN(int blknum, int offset) {
		this.blknum = blknum;
		this.offset = offset;
	}
		
	@Override
	public int compareTo(LSN lsnObj) {		
		if(this.blknum >= lsnObj.getBlkNum() && this.offset >= lsnObj.getOffset())
			return 1;
		
		return -1;
	}
	
	public void setBlkNum(int blknum) {
		this.blknum = blknum;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getBlkNum() {
		return blknum;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void copyFrom (LSN lsnObj) {
		this.blknum = lsnObj.blknum;
		this.offset = lsnObj.offset;
	}

}
