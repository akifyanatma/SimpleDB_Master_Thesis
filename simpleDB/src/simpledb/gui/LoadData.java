package simpledb.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.table.DefaultTableModel;

import simpledb.record.RecordFile;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class LoadData {

	//Bir tablonun kayitlarindaki field degerleri uretilir.
	public void generateFieldValues(String tableName, int recordCount, DefaultTableModel tableModel){
		Transaction tx = new Transaction();
		
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		
		Map<String, ArrayList<Integer>> allIntFieldValues = new HashMap<String, ArrayList<Integer>>();
		Map<String, ArrayList<String>> allStringFieldValues = new HashMap<String, ArrayList<String>>();
			
		for(int i=0; i<tableModel.getColumnCount();i++){
			String fld = tableModel.getColumnName(i);
					
			if(sch.type(fld) == 4){
				int minInterval = Integer.valueOf((String)tableModel.getValueAt(0, i));
				int maxInterval = Integer.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
				ArrayList<Integer> intFieldValues = generateIntField(minInterval, maxInterval, distinctValue, recordCount);
				allIntFieldValues.put(fld, intFieldValues);
				
			}
			else if(sch.type(fld) == 8){
				double minInterval = Double.valueOf((String)tableModel.getValueAt(0, i));
				double maxInterval = Double.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));	
				//Double type icin random generator function henuz yapilmadi.
			}
			else{
				int minLength = Integer.valueOf((String)tableModel.getValueAt(0, i));
				int maxLength = Integer.valueOf((String)tableModel.getValueAt(1, i));
				int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
				if(sch.length(fld) < minLength)
					minLength = 1;
				if(sch.length(fld) < maxLength)
					maxLength = sch.length(fld);
				
				ArrayList<String> stringFieldValues = generateStringField(minLength, maxLength, distinctValue, recordCount);
				allStringFieldValues.put(fld, stringFieldValues);
			}
		}
		
		loadTable(ti, recordCount, allIntFieldValues, allStringFieldValues, tx);
		setDistinctValuesToMetadata(tableName, tableModel, tx);
		
		tx.commit();
	}
	
	private ArrayList<Integer> generateIntField(int minInterval, int maxInterval, int distinctValue, int recordCount) {
	
			//NORMAL DAGILIM
		//	//Verilen aralikta olabilecek tum sayilar
		//	ArrayList<Integer> numbers = new ArrayList<>();
		//	for(int i = minInterval; i<= maxInterval;i++)
		//		numbers.add(i);
		//	
		//	//Distinct value sayilari belirleniyor
		//	ArrayList<Integer> distinctNumbers = new ArrayList<>();		
		//	for(int j=0; j<distinctValue; j++){			
		//		Random r = new Random(); 
		//		int rIndex = r.nextInt(numbers.size());
		//		
		//		int randomNumber = numbers.get(rIndex);
		//		
		//		numbers.remove(rIndex);
		//		
		//		distinctNumbers.add(randomNumber);		
		//	}
		//	
		//	ArrayList<Integer> resultList = new ArrayList<>();
		//	
		//	//Sonuc listesinde en az bir kez butun distinct value sayilari bulunmali
		//	resultList.addAll(distinctNumbers);
		//	
		//	//Sonuc listesi dolmadi ise rastgele distinct value sayilarindan secilerek ekleniyor.(normal dagilim)
		//	for(int m=0; m<recordCount-distinctNumbers.size(); m++){
		//		Random rn = new Random();
		//		int rnIndex = rn.nextInt(distinctNumbers.size());
		//		int rnNumber = distinctNumbers.get(rnIndex);
		//		resultList.add(rnNumber);
		//	}
		//	
		//	System.out.println("\n\n" + resultList.size() + "\n\n");
		//	for(int y=0;y<resultList.size();y++)
		//		System.out.println(resultList.get(y));
		//	
		//	return resultList;
	
	
		//UNIFORM DAGILIM
		//Verilen aralikta olabilecek tum sayilar
		ArrayList<Integer> numbers = new ArrayList<>();
		for(int i = minInterval; i<= maxInterval;i++)
			numbers.add(i);
		
		//Distinct value olacak sayilar belirleniyor
		ArrayList<Integer> distinctNumbers = new ArrayList<>();		
		for(int j=0; j<distinctValue; j++){			
			Random r = new Random(); 
			int rIndex = r.nextInt(numbers.size());
			
			int randomNumber = numbers.get(rIndex);
			
			numbers.remove(rIndex);
			
			distinctNumbers.add(randomNumber);		
		}
		
		
		//Record index siralarini karistirma
		ArrayList<Integer> sortedIndexList = new ArrayList<>();
		for(int k=0; k<recordCount; k++)
			sortedIndexList.add(k);
		
		ArrayList<Integer> mixedIndexList = new ArrayList<>();
		for(int m=0; m<recordCount; m++) {
			Random rn = new Random();
			int rnIndex = rn.nextInt(sortedIndexList.size());
			mixedIndexList.add(sortedIndexList.remove(rnIndex));
		}
		
		//Sonuclari olusturma
		HashMap<Integer, Integer> resultMap = new HashMap<>();
		for(int n=0; n<recordCount; n++) {
			int distincValueIndex = n%distinctNumbers.size();
			resultMap.put(mixedIndexList.get(n), distinctNumbers.get(distincValueIndex));			
		}
		
		ArrayList<Integer> resultList = new ArrayList<>();
		for(int key : resultMap.keySet())
			resultList.add(resultMap.get(key));
			
		System.out.println("\n\n" + resultList.size() + "\n\n");
		for(int y=0;y<resultList.size();y++)
			System.out.println(resultList.get(y));
		
		return resultList;		
	}
	
	private ArrayList<String> generateStringField(int minLength, int maxLength, int distinctValue, int recordCount){
			//NORMAL DAGILIM
		//	char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		//
		//	ArrayList<String> distinctWords = new ArrayList<>();
		//	
		//	for(int j=0; j<distinctValue; j++){		
		//		Random r = new Random();
		//		int length = r.nextInt((maxLength - minLength) + 1) + minLength;
		//		
		//		Random rn = new Random();
		//		String word = "";
		//		for(int k=0; k<length; k++){			
		//			int rnIndex = rn.nextInt(alphabet.length);
		//			word+=alphabet[rnIndex];
		//		}
		//		
		//		distinctWords.add(word);		
		//	}
		//	
		//	ArrayList<String> resultList = new ArrayList<>();
		//	
		//	resultList.addAll(distinctWords);
		//	
		//	for(int n=0; n<recordCount-distinctWords.size(); n++){
		//		Random rnd = new Random();
		//		int rndIndex = rnd.nextInt(distinctWords.size());
		//		
		//		String rndName = distinctWords.get(rndIndex);
		//		
		//		resultList.add(rndName);
		//	}
		//	
		//	System.out.println("\n\n" + resultList.size() + "\n\n");
		//	for(int y=0;y<resultList.size();y++)
		//		System.out.println(resultList.get(y));
		//	
		//	return resultList;
		
		
		//UNIFORM DAGILIM
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		//Distinct value olacak kelimeler belirleniyor
		ArrayList<String> distinctWords = new ArrayList<>();
		for(int j=0; j<distinctValue; j++){		
			Random r = new Random();
			int length = r.nextInt((maxLength - minLength) + 1) + minLength;
						
			String word = "";
			for(int k=0; k<length; k++){
				Random rn = new Random();
				int rnIndex = rn.nextInt(alphabet.length);
				word+=alphabet[rnIndex];
			}
			
			distinctWords.add(word);		
		}
		
		//Record index siralarini karistirma
		ArrayList<Integer> sortedIndexList = new ArrayList<>();
		for(int k=0; k<recordCount; k++)
			sortedIndexList.add(k);
		
		ArrayList<Integer> mixedIndexList = new ArrayList<>();
		for(int m=0; m<recordCount; m++) {
			Random rn = new Random();
			int rnIndex = rn.nextInt(sortedIndexList.size());
			mixedIndexList.add(sortedIndexList.remove(rnIndex));
		}
		
		//Sonuclari olusturma
		HashMap<Integer, String> resultMap = new HashMap<>();
		for(int n=0; n<recordCount; n++) {
			int distincValueIndex = n%distinctWords.size();
			resultMap.put(mixedIndexList.get(n), distinctWords.get(distincValueIndex));			
		}
		
		ArrayList<String> resultList = new ArrayList<>();
		for(int key : resultMap.keySet())
			resultList.add(resultMap.get(key));
			
		System.out.println("\n\n" + resultList.size() + "\n\n");
		for(int y=0;y<resultList.size();y++)
			System.out.println(resultList.get(y));
		
		return resultList;	
	}
	
	//Olusturulan field degerleri tabloya eklenir
	private void loadTable(TableInfo ti, int recordCount, Map<String, ArrayList<Integer>> allIntFieldValues, Map<String, ArrayList<String>> allStringFieldValues, Transaction tx){
		Schema sch = ti.schema();
		
		RecordFile rf = new RecordFile(ti, tx);
        while (rf.next())
            rf.delete();
         rf.beforeFirst();
         
         for (int i=0; i<recordCount; i++) {
        	 rf.insert();
        	 for (String fld : sch.fields()) {
        		 if(sch.type(fld) == 4)
        			 rf.setInt(fld, allIntFieldValues.get(fld).remove(0));
        		 else
        			 rf.setString(fld, allStringFieldValues.get(fld).remove(0));     		 
        	 }
        	 
          }
          rf.close();
	}
	
	private void setDistinctValuesToMetadata(String tableName, DefaultTableModel tableModel, Transaction tx){
		TableInfo ti = SimpleDB.mdMgr().getTableInfo(tableName, tx);
		Schema sch = ti.schema();
		
		for(int i=0; i<tableModel.getColumnCount();i++){
			String fldName = tableModel.getColumnName(i);
			int distinctValue = Integer.valueOf((String)tableModel.getValueAt(2, i));
			SimpleDB.mdMgr().setDistinctValue(tableName+".tbl", fldName, distinctValue, ti, tx);
		}
	}
	
}
