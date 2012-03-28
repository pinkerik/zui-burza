import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Tick {
	Date time;
	int type; 
	float price;
	int quantity;
	
	public static final int BID = 0;
	public static final int ASK = 1;
	public static final int TRADE = 2;
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String[] tline;
	
	public Tick(){};

	public boolean set(String line){
		try{
			tline = line.split(",");
			
			time = df.parse(tline[0]);
			
			if(tline[1].equals("BID")){
				type = BID;
			}else if (tline[1].equals("ASK")){
				type = ASK;
			}else if(tline[1].equals("TRADE")){
				type = TRADE;
			}
			
			price = Float.parseFloat(tline[2]);
			quantity = Integer.parseInt(tline[3]);
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
