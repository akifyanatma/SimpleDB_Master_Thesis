package simpledb.parse;

import java.util.*;

import simpledb.metadata.MetadataMgr;
import simpledb.query.*;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import static java.sql.Types.INTEGER;
import static java.sql.Types.DOUBLE;

/**
 * The SimpleDB parser.
 * @author Edward Sciore
 */
public class Parser {
   private Lexer lex;
   
   public Parser(String s) {
      lex = new Lexer(s);
   }
   
// Methods for parsing predicates, terms, expressions, constants, and fields
   
   public String field() {
      return lex.eatId();
   }
   
//   public Constant constant() {
//      if (lex.matchStringConstant())
//         return new StringConstant(lex.eatStringConstant());
//      else
//         return new IntConstant(lex.eatIntConstant());
//   }
      
   //Akif
   public Constant constant(int type) {
	   if (lex.matchStringConstant())
		   return new StringConstant(lex.eatStringConstant());
	   else if(type == INTEGER)
		   return new IntConstant(lex.eatIntConstant());
	   else
		   return new DoubleConstant(lex.eatDoubleConstant());	       
   }
   
//   public Expression expression() {
//      if (lex.matchId())
//         return new FieldNameExpression(field());
//      else
//         return new ConstantExpression(constant());
//   }
   
   //Akif
   public Expression expression(String fldName, List<Schema> schList) {
	   if (lex.matchId()) {
		   return new FieldNameExpression(field());
	   }
	   else {
		   //lhs olan expression'un field'nin tipi ogreniliyor schema'lardan
		   int type = 4; //default olarak integer olarak ayarladim
		   for(int i=0; i<schList.size(); i++) {
			   Schema sch = schList.get(i);
			   if(sch.hasField(fldName)) {
				   type = sch.type(fldName);
				   break;
			   }				   
		   }
		   return new ConstantExpression(constant(type));
	   }
   }
   
//   public Term term() {
//      Expression lhs = expression();
//      lex.eatDelim('=');
//      Expression rhs = expression();
//      return new Term(lhs, rhs);
//   }
   
   //Akif
   public Term term(List<Schema> schList) {
	   String fldName ="";
	   Expression lhs = expression(fldName, schList); 
	   fldName = lhs.asFieldName();
	   lex.eatDelim('=');
	   Expression rhs = expression(fldName, schList);
	   return new Term(lhs, rhs);
   }
   
//   public Predicate predicate() {
//      Predicate pred = new Predicate(term());
//      if (lex.matchKeyword("and")) {
//         lex.eatKeyword("and");
//         pred.conjoinWith(predicate());
//      }
//      return pred;
//   }
   
   //Akif
   public Predicate predicate(List<Schema> schList) {
	   Predicate pred = new Predicate(term(schList));
	   if (lex.matchKeyword("and")) {
		   lex.eatKeyword("and");
	       pred.conjoinWith(predicate(schList));
	   }
	   return pred;
   }
   
// Methods for parsing queries
   
//   public QueryData query() {
//      lex.eatKeyword("select");
//      Collection<String> fields = selectList();
//      lex.eatKeyword("from");
//      Collection<String> tables = tableList();
//      Predicate pred = new Predicate();
//      if (lex.matchKeyword("where")) {
//         lex.eatKeyword("where");
//         pred = predicate();
//      }
//      return new QueryData(fields, tables, pred);
//   }
   
   //Akif
   public QueryData query(Transaction tx) {
	   lex.eatKeyword("select");
	   Collection<String> fields = selectList();
	   lex.eatKeyword("from");
	   Collection<String> tables = tableList();
	   
	   MetadataMgr mdm = SimpleDB.mdMgr();
	   List<Schema> schList = new ArrayList<Schema>();
	   Iterator<String> iterator = tables.iterator();
	   while(iterator.hasNext()){
		   String tblname = iterator.next();
		   TableInfo ti = mdm.getTableInfo(tblname, tx);
		   Schema sch = ti.schema();
		   schList.add(sch);		   
	   }
		   	   
	   Predicate pred = new Predicate();
	   if (lex.matchKeyword("where")) {
		   lex.eatKeyword("where");
	       pred = predicate(schList);
	   }
	   return new QueryData(fields, tables, pred);
   }
   
   private Collection<String> selectList() {
      Collection<String> L = new ArrayList<String>();
      L.add(field());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(selectList());
      }
      return L;
   }
   
   private Collection<String> tableList() {
      Collection<String> L = new ArrayList<String>();
      L.add(lex.eatId());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(tableList());
      }
      return L;
   }
   
// Methods for parsing the various update commands
   
//   public Object updateCmd() {
//      if (lex.matchKeyword("insert"))
//         return insert();
//      else if (lex.matchKeyword("delete"))
//         return delete();
//      else if (lex.matchKeyword("update"))
//         return modify();
//      else
//         return create();
//   }
   
   //Akif
   public Object updateCmd(Transaction tx) {
	   if (lex.matchKeyword("insert"))
		   return insert(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
	   else if (lex.matchKeyword("delete"))
	       return delete(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
	   else if (lex.matchKeyword("update"))
		   return modify(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
	   else
	       return create(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
   }
   
//   private Object create() {
//      lex.eatKeyword("create");
//      if (lex.matchKeyword("table"))
//         return createTable();
//      else if (lex.matchKeyword("view"))
//         return createView();
//      else
//         return createIndex();
//   }
   
   //Akif
   private Object create(Transaction tx) {
	   lex.eatKeyword("create");
	   if (lex.matchKeyword("table"))
		   return createTable();
	   else if (lex.matchKeyword("view"))
		   return createView(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
	   else
	       return createIndex();
   }
   
// Method for parsing delete commands
   
//   public DeleteData delete() {
//      lex.eatKeyword("delete");
//      lex.eatKeyword("from");
//      String tblname = lex.eatId();
//      Predicate pred = new Predicate();
//      if (lex.matchKeyword("where")) {
//         lex.eatKeyword("where");
//         pred = predicate();
//      }
//      return new DeleteData(tblname, pred);
//   }
   
   //Akif
   public DeleteData delete(Transaction tx) {
	   lex.eatKeyword("delete");
	   lex.eatKeyword("from");
	   String tblname = lex.eatId();
	   Predicate pred = new Predicate();
	   
	   //Field'lerin tipini ogrenmede kullanmak icin schema elde ediliyor.
	   MetadataMgr mdm = SimpleDB.mdMgr();
	   List<Schema> schList = new ArrayList<Schema>();
	   TableInfo ti = mdm.getTableInfo(tblname, tx);
	   Schema sch = ti.schema();
	   schList.add(sch);		   
	   
	   if (lex.matchKeyword("where")) {
		   lex.eatKeyword("where");
	       pred = predicate(schList);
	   }
	   return new DeleteData(tblname, pred);
   }
   
// Methods for parsing insert commands
   
//   public InsertData insert() {
//      lex.eatKeyword("insert");
//      lex.eatKeyword("into");
//      String tblname = lex.eatId();
//      lex.eatDelim('(');
//      List<String> flds = fieldList();
//      lex.eatDelim(')');
//      lex.eatKeyword("values");
//      lex.eatDelim('(');
//      List<Constant> vals = constList();
//      lex.eatDelim(')');
//      return new InsertData(tblname, flds, vals);
//   }
   
   //Akif
   public InsertData insert(Transaction tx) {
	   lex.eatKeyword("insert");
	   lex.eatKeyword("into");
	   String tblname = lex.eatId();
	   lex.eatDelim('(');
	   List<String> flds = fieldList();
	   lex.eatDelim(')');
	   lex.eatKeyword("values");
	   lex.eatDelim('(');
	   
	   MetadataMgr mdm = SimpleDB.mdMgr();
	   TableInfo ti = mdm.getTableInfo(tblname, tx);
	   Schema sch = ti.schema(); //Insert yapilacak olan tablonun schema'si elde ediliyor.
	   List<String> tempFlds = new ArrayList<String>();
	   tempFlds.addAll(flds); //Field'lerin tipini ogrenirken field listesi modifiyeye ugramamasi icin ayni elemanlardan yeni bir liste olusturuluyor.
	   
	   List<Constant> vals = constList(sch, tempFlds);
	   lex.eatDelim(')');
	   return new InsertData(tblname, flds, vals);
   }
   
   private List<String> fieldList() {
      List<String> L = new ArrayList<String>();
      L.add(field());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(fieldList());
      }
      return L;
   }
   
//   private List<Constant> constList() {
//      List<Constant> L = new ArrayList<Constant>();
//      L.add(constant());
//      if (lex.matchDelim(',')) {
//         lex.eatDelim(',');
//         L.addAll(constList());
//      }
//      return L;
//   }
   
   //Akif
   private List<Constant> constList(Schema sch, List<String> tempFlds) {
	   List<Constant> L = new ArrayList<Constant>();
	   int fieldType = sch.type(tempFlds.remove(0));
	   L.add(constant(fieldType));
	   if (lex.matchDelim(',')) {
		   lex.eatDelim(',');
	       L.addAll(constList(sch, tempFlds));
	   }
	   return L;
   }
   
// Method for parsing modify commands
   
//   public ModifyData modify() {
//      lex.eatKeyword("update");
//      String tblname = lex.eatId();
//      lex.eatKeyword("set");
//      String fldname = field();
//      lex.eatDelim('=');
//      Expression newval = expression();
//      Predicate pred = new Predicate();
//      if (lex.matchKeyword("where")) {
//         lex.eatKeyword("where");
//         pred = predicate();
//      }
//      return new ModifyData(tblname, fldname, newval, pred);
//   }
   
   //Akif
   public ModifyData modify(Transaction tx) {
	   lex.eatKeyword("update");
	   String tblname = lex.eatId();
	   lex.eatKeyword("set");
	   String fldname = field();
	   lex.eatDelim('=');
	   
	   
	   MetadataMgr mdm = SimpleDB.mdMgr();
	   List<Schema> schList = new ArrayList<Schema>();
	   TableInfo ti = mdm.getTableInfo(tblname, tx);
	   Schema sch = ti.schema();
	   schList.add(sch);
	   
	   
	   Expression newval = expression(fldname, schList);
	   Predicate pred = new Predicate();
	   if (lex.matchKeyword("where")) {
		   lex.eatKeyword("where");
	       pred = predicate(schList);
	   }
	   return new ModifyData(tblname, fldname, newval, pred);
   }
   
// Method for parsing create table commands
   
   public CreateTableData createTable() {
      lex.eatKeyword("table");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      Schema sch = fieldDefs();
      lex.eatDelim(')');
      return new CreateTableData(tblname, sch);
   }
   
   private Schema fieldDefs() {
      Schema schema = fieldDef();
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         Schema schema2 = fieldDefs();
         schema.addAll(schema2);
      }
      return schema;
   }
   
   private Schema fieldDef() {
      String fldname = field();
      return fieldType(fldname);
   }
   
//   private Schema fieldType(String fldname) {
//      Schema schema = new Schema();
//      if (lex.matchKeyword("int")) {
//         lex.eatKeyword("int");
//         schema.addIntField(fldname);
//      }
//      else {
//         lex.eatKeyword("varchar");
//         lex.eatDelim('(');
//         int strLen = lex.eatIntConstant();
//         lex.eatDelim(')');
//         schema.addStringField(fldname, strLen);
//      }
//      return schema;
//   }
   
   //Akif
   private Schema fieldType(String fldname) {
	   Schema schema = new Schema();
	   if (lex.matchKeyword("int")) {
		   lex.eatKeyword("int");
	       schema.addIntField(fldname);
	   }
	   else if(lex.matchKeyword("double")) {
		   lex.eatKeyword("double");
	       schema.addDoubleField(fldname);
	   }
	   else {
	         lex.eatKeyword("varchar");
	         lex.eatDelim('(');
	         int strLen = lex.eatIntConstant();
	         lex.eatDelim(')');
	         schema.addStringField(fldname, strLen);
	   }
	   return schema;
   }
   
// Method for parsing create view commands
   
//   public CreateViewData createView() {
//      lex.eatKeyword("view");
//      String viewname = lex.eatId();
//      lex.eatKeyword("as");
//      QueryData qd = query();
//      return new CreateViewData(viewname, qd);
//   }
   
   //Akif
   public CreateViewData createView(Transaction tx) {
	   lex.eatKeyword("view");
	   String viewname = lex.eatId();
	   lex.eatKeyword("as");
	   QueryData qd = query(tx);//Bir tablonun schema bilgisini elde etmek icin tx parametresi gonderiliyor.
	   return new CreateViewData(viewname, qd);
   }
   
   
//  Method for parsing create index commands
   
   public CreateIndexData createIndex() {
      lex.eatKeyword("index");
      String idxname = lex.eatId();
      lex.eatKeyword("on");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      String fldname = field();
      lex.eatDelim(')');
      return new CreateIndexData(idxname, tblname, fldname);
   }
}

