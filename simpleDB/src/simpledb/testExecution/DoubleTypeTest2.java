package simpledb.testExecution;

import simpledb.tx.Transaction;
import simpledb.query.*;
import simpledb.server.SimpleDB;
//import simpledb.server.SimpleDB.PlannerType;

/* 
 * make table/insert data/ query execution
 * Output of this program :
 * x	y
*  10	0.243
 * In case executing repeatedly should result in
 *  x	y
 *  10	0.243
 *  10	0.243
 *  10	0.243
 *  10	0.243
 */

public class DoubleTypeTest2 {
	public static void main(String[] args) {
		try {
		
			//SimpleDB.init("doubleTable",null,0);
			SimpleDB.init("doubleTable");
			Transaction tx = new Transaction();
			String cmd1="create table newtable(x int, y double)";
			//SimpleDB.planner(PlannerType.BASIC).executeUpdate(cmd1, tx);
			SimpleDB.planner().executeUpdate(cmd1, tx);
			
			String cmd2="insert into newtable(x,y) values(10, 0.243)";
			//SimpleDB.planner(PlannerType.BASIC).executeUpdate(cmd2, tx);
			SimpleDB.planner().executeUpdate(cmd2, tx);
			
			String cmd3 = "select x,y " 
		        + "from newtable "
		        + "where y=0.243 ";	
					       
			// for this test planner type is irrelevant.
			//Plan p = SimpleDB.planner(PlannerType.BASIC).createQueryPlan(cmd3, tx);
			Plan p = SimpleDB.planner().createQueryPlan(cmd3, tx);
			
			// analogous to the result set
			Scan s = p.open();
			
			System.out.println("x\ty");
			while (s.next()) {
				int x = s.getInt("x"); //SimpleDB stores field names
				double y= s.getDouble("y"); //in lower case
				
				System.out.println(x + "\t" + y);
			}
			s.close();
			tx.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
