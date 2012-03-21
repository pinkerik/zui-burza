import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;


public class Synchronizer {

	Semaphore readingSem;
	Semaphore parsingSem;
	Date time;
	boolean end;
	
	Calendar cal = Calendar.getInstance();
	
	public Synchronizer(int count){
		readingSem = new Semaphore(count,true);
		parsingSem = new Semaphore(count,true);
		time = null;
		end = false;
	}
	
	public void add(int field,int amount){
		cal.setTime(time);
		switch(field){
			case Calendar.MINUTE:
			case Calendar.HOUR:
			case Calendar.DAY_OF_MONTH:
			case Calendar.WEEK_OF_YEAR:
				cal.add(field,1);
				break;
			default:
				cal.add(Calendar.HOUR,1);
		}
		time = cal.getTime();
	}
}
