
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Tick {
	Date time;
	int type; 
	float price;
	int quantity;
	
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Tick(String line){
		String [] tline = line.split(",");

		try {
			time = df.parse(tline[0]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tline[1].equals("BID")){
			type = 0;
		}else if (tline[1].equals("ASK")){
			type = 1;
		}else if(tline[1].equals("TRADE")){
			type = 2;
		}
		
		price = Float.parseFloat(tline[2]);
		quantity = Integer.parseInt(tline[3]);

	}
}
