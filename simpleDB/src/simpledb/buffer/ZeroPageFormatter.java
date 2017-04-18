package simpledb.buffer;

import simpledb.file.Page;

public class ZeroPageFormatter implements PageFormatter {

	@Override
	public void format(Page p) {
		// TODO Auto-generated method stub		
		int offset = 0;
		while(offset < 100)
		{
			p.setInt(offset, 0);
			offset = offset + 4;
		}
		
	}  

}
