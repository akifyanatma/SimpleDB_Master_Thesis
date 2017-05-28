package simpledb.testExecution;

import simpledb.tx.Transaction;
import simpledb.query.*;
import simpledb.server.SimpleDB;
//import simpledb.server.SimpleDB.PlannerType;

/* 
 * make table/insert data/ query/ modify data/ delete data execution
 * Output of this program :
 * create table newtable(x int, y double)
 * 
 * insert into newtable(x,y) values(10, 0.243)

 * select x,y from newtable where y=0.243
 * x	y
 * 10	0.243

 * update newtable set y=0.532 where y=0.243

 * select x,y from newtable where y=0.532
 * x	y
 * 10	0.532

 * delete from newtable where y=0.532

 * select x,y from newtable where y=0.532
 * x	y
 * transaction 2 committed
 */

public class DoubleTypeTest3 {
	public static void main(String[] args) {
		try {
			SimpleDB.init("doubleTable");
			Transaction tx = new Transaction();
			
			
			///////////////////////////////// CREATE ////////////////////////////////////
			System.out.println("\ncreate table newtable(x int, y double)");
			String cmd1="create table newtable(x int, y double)";
			SimpleDB.planner().executeUpdate(cmd1, tx);
			
			
			///////////////////////////////// INSERT ////////////////////////////////////
			System.out.println("\ninsert into newtable(x,y) values(10, 0.243)");
			String cmd2="insert into newtable(x,y) values(10, 0.243)";
			SimpleDB.planner().executeUpdate(cmd2, tx);
			
			
			///////////////////////////////// SELECT ////////////////////////////////////
			System.out.println("\nselect x,y from newtable where y=0.243");
			String cmd3 = "select x,y " 
		        + "from newtable "
		        + "where y=0.243 ";	
					       
			// for this test planner type is irrelevant.
			Plan p3 = SimpleDB.planner().createQueryPlan(cmd3, tx);
			
			// analogous to the result set
			Scan s3 = p3.open();
			
			System.out.println("x\ty");
			while (s3.next()) {
				int x = s3.getInt("x"); //SimpleDB stores field names
				double y= s3.getDouble("y"); //in lower case
				
				System.out.println(x + "\t" + y);
			}
			s3.close();
			
				
			///////////////////////////////// UPDATE ////////////////////////////////////			
			System.out.println("\nupdate newtable set y=0.532 where y=0.243");
			String cmd4 = "update newtable set y=0.532 where y=0.243";
			SimpleDB.planner().executeUpdate(cmd4, tx);
			
			
			///////////////////////////////// SELECT ////////////////////////////////////			
			System.out.println("\nselect x,y from newtable where y=0.532");
			String cmd5 = "select x,y " 
			        + "from newtable "
			        + "where y=0.532 ";	
						       
			// for this test planner type is irrelevant.
			Plan p5 = SimpleDB.planner().createQueryPlan(cmd5, tx);
				
			// analogous to the result set
			Scan s5 = p5.open();
				
			System.out.println("x\ty");
			while (s5.next()) {
				int x = s5.getInt("x"); //SimpleDB stores field names
				double y= s5.getDouble("y"); //in lower case
					
				System.out.println(x + "\t" + y);
			}
			s5.close();
			
			
			///////////////////////////////// DELETE ////////////////////////////////////
			System.out.println("\ndelete from newtable where y=0.532");
			String cmd6 = "delete from newtable where y=0.532 ";
			SimpleDB.planner().executeUpdate(cmd6, tx);
			
			
			///////////////////////////////// SELECT ////////////////////////////////////
			System.out.println("\nselect x,y from newtable where y=0.532");
			String cmd7 = "select x,y " 
			        + "from newtable "
			        + "where y=0.532 ";	
						       
			// for this test planner type is irrelevant.
			Plan p7 = SimpleDB.planner().createQueryPlan(cmd7, tx);
				
			// analogous to the result set
			Scan s7 = p7.open();
				
			System.out.println("x\ty");
			while (s7.next()) {
				int x = s7.getInt("x"); //SimpleDB stores field names
				double y= s7.getDouble("y"); //in lower case
					
				System.out.println(x + "\t" + y);
			}
			s7.close();
			
			
			tx.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
