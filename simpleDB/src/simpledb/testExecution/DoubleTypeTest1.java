package simpledb.testExecution;

import simpledb.tx.Transaction;
import simpledb.query.*;
import simpledb.server.SimpleDB;
//import simpledb.server.SimpleDB.PlannerType;

/* This is a version of the StudentMajor program that
 * accesses the SimpleDB classes directly (instead of
 * connecting to it as a JDBC client).  You can run it
 * without having the server also run.
 * 
 * These kind of programs are useful for debugging
 * your changes to the SimpleDB source code.
 * 
 * 
 * This program test manipulating double type data.  
 */

public class DoubleTypeTest1 {
	public static void main(String[] args) {
		try {
			// analogous to the driver
			LoadDoubleData.initData("doubleTable");			
			
			// analogous to the connection
			Transaction tx = new Transaction();
			
			// analogous to the statement
			/*IMPORTANT POINTs: 
				1-) put a space before last " in select and from lines. "select a, b " is OK.
				    "select a, b"  is WRONG. OR write all query within a sinle line w/o any enter.
				2-) first run "select a,b from input" first. Get a b value. Then run 
				     "select ...from....where b= the value you get"
				3-) If you run SQLInterpreter and get a b value from there, those 
				    double values format is like 0,2737239. The problem here is ",". 
				    In the query below it is error to write ".....where b=0,2737239 "
				    You should write ".....where b=0.2737239 ".  Use . instead of ,
				 4-) Last point is again about SQL Interpreter. The console double precision 
				     may be less the real double value. For example the console shows s/t like
				     0,2367453678 but the real stored value is 0.23674536779.
				 5-) String constant için: ".....where b='araba' " yazman gerek.
				 From the points 3 and 4 above, it is dangerous to get b value from console 
				 which is the output of the SQLInterpreter. 				            
			*/
			String qry = "select a, b, c " 
		        + "from input "
		        + "where c='dummy' and b=-0.1303700475463838 ";	// select a b-value from generated table
					       
			// for this test planner type is irrelevant.
			//Plan p = SimpleDB.planner(PlannerType.BASIC).createQueryPlan(qry, tx);
			Plan p = SimpleDB.planner().createQueryPlan(qry, tx);
			
			// analogous to the result set
			Scan s = p.open();
			
			System.out.println("a\tb");
			while (s.next()) {
				int a = s.getInt("a"); //SimpleDB stores field names
				double b= s.getDouble("b"); //in lower case
				String c = s.getString("c"); 
				System.out.println(a + "\t" + b+ "\t" + c);
			}
			s.close();
			tx.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

