import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Comparator;

import com.mysql.jdbc.Statement;

public class Calculation {
	
	HashMap<String, Integer> values;
	HashMap<String, Integer> buying;
	HashMap<String, Integer> maint;
	HashMap<String, Integer> doors;
	HashMap<String, Integer> persons;
	HashMap<String, Integer> lvg_boot;
	HashMap<String, Integer> safety;
	
	public static boolean ASC = true;
    public static boolean DESC = false;
	
	ConnectionOfDB connection;
	Statement stmt;
	ResultSet rs;
	
	int confMat[][] = new int[4][4];
	
	int total = 0;
	int hit = 0;

	public Calculation(HashMap<String, Integer> values, HashMap<String, Integer> buying, HashMap<String, Integer> maint,
			HashMap<String, Integer> doors, HashMap<String, Integer> persons, HashMap<String, Integer> lvg_boot,
			HashMap<String, Integer> safety, ConnectionOfDB connection) throws FileNotFoundException, SQLException {
		
		this.values = values;
		this.buying = buying;
		this.maint = maint;
		this.doors = doors;
		this.persons = persons;
		this.lvg_boot = lvg_boot;
		this.safety = safety;
		this.connection = connection;
		rs = this.connection.rs;
		stmt = (Statement) this.connection.stmt;
		
		openFileAndCalculate();
		
		//System.out.println("Hit: "+hit);
		//System.out.println("Total: "+total);
		
		
		System.out.println("KNN Algorithm");
		
		double ratio = (double) hit / (double) total;
		
		System.out.println("Accuracy: "+ratio);
		System.out.println();
		System.out.println();
		
	}
	
	public void openFileAndCalculate() throws FileNotFoundException, SQLException {
		
		String attributes = "";
		
		Scanner scanner = new Scanner(new File("C:\\Users\\Tite-M\\Desktop\\newtest.txt"));
		
		while (scanner.hasNextLine()) {
		   
		attributes = scanner.nextLine();
		
		String[] attrin = attributes.split(" ");
		

		String buyingIN=attrin[0];
		String maintIN=attrin[1];
		String doorsIN=attrin[2];
		String personsIN=attrin[3];
		String lvg_bootIN=attrin[4];
		String safetyIN=attrin[5];
		String resultIN= attrin[6];
		
		total++;
		
		
		auxCalculate(buying.get(buyingIN),maint.get(maintIN),doors.get(doorsIN),persons.get(personsIN),lvg_boot.get(lvg_bootIN),safety.get(safetyIN),resultIN);
		
		}
		 this.connection.con.close();
	}

	private void auxCalculate(int buying, int maint, int doors, int persons, int lvg_boot, int safety, String result) throws SQLException {
		
		 int counter = 0;
		 rs = stmt.executeQuery("select * from careval.carevaluationx");
	      
		 HashMap<String, Double> results = new HashMap<String, Double>();
		 
		 
		
	      while (rs.next())
	      {
	        
	    	
	        double buyingsq = Math.pow(Math.abs(buying - this.buying.get(rs.getString("buying"))), 2);
	        
	        double maintsq = Math.pow(Math.abs(maint - this.maint.get(rs.getString("maint"))), 2);
	        
	        double doorssq = Math.pow(Math.abs(doors - this.doors.get(rs.getString("doors"))), 2);
	        
	        double personssq = Math.pow(Math.abs(persons - this.persons.get(rs.getString("persons"))), 2);
	        
	        double lvg_bootsq = Math.pow(Math.abs(lvg_boot - this.lvg_boot.get(rs.getString("lvg_boot"))), 2);
	        
	        double safetysq = Math.pow(Math.abs(safety - this.safety.get(rs.getString("safety"))), 2);
	        
	        
	        
	        counter ++;
	        
	        double sqrtofthem = Math.sqrt((buyingsq+maintsq+doorssq+personssq+lvg_bootsq+safetysq));
	        
	        results.put(rs.getString("class")+","+counter, sqrtofthem);
	        
	        
	      }
	      
	      Map<String, Double> sortedMapAsc = sortByComparator(results, ASC);
	      
	      calculateMissHit(sortedMapAsc,result);
		
	}
	
	
	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	public void calculateMissHit(Map<String, Double> map,String result)
    {
		
		
		
		
		int indexes[] = new int[4];
		indexes[0] = 0;
		indexes[1] = 0;
		indexes[2] = 0;
		indexes[3] = 0;
		
		int counter = 0;
        for (Entry<String, Double> entry : map.entrySet())
        {
        	if(counter < 7) {
        		String cl = entry.getKey().toString().substring(0,entry.getKey().toString().indexOf(','));
        		
        		if(cl.equals("vgood")) {
        			indexes[3] = indexes[3]+1;
        		}
        		if(cl.equals("good")) {
        			indexes[2] = indexes[2]+1;
        		}
        		if(cl.equals("acc")) {
        			indexes[1] = indexes[1]+1;
        		}
        		if(cl.equals("unacc")) {
        			indexes[0] = indexes[0]+1;
        		}
        		
        	}
        	counter++;
            
        }
        
        int max = indexes[0];
		int index = 0;

		for (int i = 0; i < indexes.length; i++) 
		{
			if (max < indexes[i]) 
			{
				max = indexes[i];
				index = i;
			}
		}
		
		String rez = "";
		
		if(index == 0) {
			rez = "unacc";
		}
		if(index == 1) {
			rez = "acc";
		}
		if(index == 2) {
			rez = "good";
		}
		if(index == 3) {
			rez = "vgood";
		}
		
		if(result.equals("unacc")) {
			confMat[0][index]++;
		}
		else if(result.equals("acc")) {
			confMat[1][index]++;
		}
		else if(result.equals("good")) {
			confMat[2][index]++;
		}
		else if(result.equals("vgood")) {
			confMat[3][index]++;
		}
		
		if(rez.equals(result)) {
			this.hit++;
		}
		
		
    }
	
	public void printConfMat() {
		
		System.out.println("Confusion Matrix");
		System.out.println();
		System.out.println("0    unacc acc good vgood");
		
		int counter = 0;
		
		for (int[] x : confMat)
		{
			if(counter == 0) {
				System.out.print("unacc ");
			}
			if(counter == 1) {
				System.out.print("acc   ");
			}
			if(counter == 2) {
				System.out.print("good  ");
			}
			if(counter == 3) {
				System.out.print("vgood ");
			}
			
		   for (int y : x)
		   {
		        System.out.print(y + " ");
		   }
		   counter++;
		   System.out.println();
		}
		
		System.out.println();
		System.out.println();
	}
	
	public void precisionRecallF1() {
		
		int tpOfV = confMat[0][0];
		int fpOfV = confMat[3][0]+confMat[2][0]+confMat[1][0];
		int fnOfV = confMat[0][1]+confMat[0][1]+confMat[0][3];
		
		double preV = ((double) tpOfV)/((double) tpOfV+fpOfV);
		double reV = ((double) tpOfV)/((double) tpOfV+fnOfV);
		double f1V = 2 * ((double) preV*reV) / ((double) preV+reV);
		
		System.out.println("Precision of vgood : "+preV);
		System.out.println("Precision of vgood : "+reV);
		System.out.println("F1score of vgood : "+f1V);
		System.out.println();
		System.out.println();
		
		
		int tpOfG = confMat[1][1];
		int fpOfG = confMat[3][1]+confMat[2][1]+confMat[0][1];
		int fnOfG = confMat[1][0]+confMat[1][2]+confMat[1][3];
		
		double preG = ((double) tpOfG)/((double) tpOfV+fpOfG);
		double reG = ((double) tpOfG)/((double) tpOfG+fnOfG);
		double f1G = 2 * ((double) preG*reG) / ((double) preG+reG);
		
		System.out.println("Precision of good : "+preG);
		System.out.println("Recall of good : "+reG);
		System.out.println("F1score of good : "+f1G);
		System.out.println();
		System.out.println();
		
		int tpOfA = confMat[2][2];
		int fpOfA = confMat[3][2]+confMat[1][2]+confMat[0][2];
		int fnOfA = confMat[2][0]+confMat[2][1]+confMat[2][3];
		
		double preA = ((double) tpOfA)/((double) tpOfV+fpOfA);
		double reA = ((double) tpOfA)/((double) tpOfA+fnOfA);
		double f1A = 2 * ((double) preA*reA) / ((double) preA+reA);
		
		System.out.println("Precision of acc : "+preA);
		System.out.println("Recall of acc : "+reA);
		System.out.println("F1score of acc : "+f1A);
		System.out.println();
		System.out.println();
		
		int tpOfU = confMat[3][3];
		int fpOfU = confMat[2][3]+confMat[1][3]+confMat[0][3];
		int fnOfU = confMat[3][0]+confMat[3][1]+confMat[3][2];
		
		double preU = ((double) tpOfU)/((double) tpOfU+fpOfU);
		double reU = ((double) tpOfU)/((double) tpOfU+fnOfU);
		double f1U = 2 * ((double) preU*reU) / ((double) preU+reU);
		
		System.out.println("Precision of unacc : "+preU);
		System.out.println("Recall of unacc : "+reU);
		System.out.println("F1score of unacc : "+f1U);
		System.out.println();
		System.out.println();
		
		
		
		System.out.println("Average Precision = "+((preV+preG+preA+preU)/4));
		System.out.println("Average Recall = "+((reV+reG+reA+reU)/4));
		System.out.println("Average F1Score = "+((f1V+f1G+f1A+f1U)/4));
		
		
		
	}
	
	
	

}
