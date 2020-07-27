import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
		
		
		HashMap<String, Integer> values = new HashMap<String, Integer>();
		
		values.put("vgood", 4);
		values.put("good", 3);
		values.put("acc", 2);
		values.put("unacc", 1);
		
		HashMap<String, Integer> buying = new HashMap<String, Integer>();
		
		buying.put("vhigh", 4);
		buying.put("high", 3);
		buying.put("med", 2);
		buying.put("low", 1);
		
		
		HashMap<String, Integer> maint = new HashMap<String, Integer>();
		
		maint.put("vhigh", 4);
		maint.put("high", 3);
		maint.put("med", 2);
		maint.put("low", 1);
		
		
		HashMap<String, Integer> doors = new HashMap<String, Integer>();
		
		doors.put("0", 4);
		doors.put("4", 3);
		doors.put("3", 2);
		doors.put("2", 1);
		
		
		HashMap<String, Integer> persons = new HashMap<String, Integer>();
		
		persons.put("0", 3);
		persons.put("4", 2);
		persons.put("2", 1);
		
		HashMap<String, Integer> lvg_boot = new HashMap<String, Integer>();
		
		lvg_boot.put("big", 3);
		lvg_boot.put("med", 2);
		lvg_boot.put("small", 1);
		
		HashMap<String, Integer> safety = new HashMap<String, Integer>();
		
		safety.put("high", 3);
		safety.put("med", 2);
		safety.put("low", 1);
		
		ConnectionOfDB connection = new ConnectionOfDB();
		
		Calculation a = new Calculation(values,buying,maint,doors,persons,lvg_boot,safety,connection);
		
		a.printConfMat();
		a.precisionRecallF1();
		
		
		
		
	}
}
