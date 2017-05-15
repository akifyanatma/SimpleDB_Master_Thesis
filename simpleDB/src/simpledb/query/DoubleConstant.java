package simpledb.query;

public class DoubleConstant implements Constant {
	private Double val;
	   
	   /**
	    * Create a constant by wrapping the specified double.
	    * @param n the double value
	    */
	   public DoubleConstant(double n) {
	      val = n;
	   }
	   
	   /**
	    * Unwraps the Double and returns it.
	    * @see simpledb.query.Constant#asJavaVal()
	    */
	   public Object asJavaVal() {
	      return val;
	   }
	   
	   public boolean equals(Object obj) {
	      DoubleConstant dc = (DoubleConstant) obj;
	      return dc != null && val.equals(dc.val);
	   }
	   
	   public int compareTo(Constant c) {
		  DoubleConstant dc = (DoubleConstant) c;
	      return val.compareTo(dc.val);
	   }
	   
	   public int hashCode() {
	      return val.hashCode();
	   }
	   
	   public String toString() {
	      return val.toString();
	   }
}
