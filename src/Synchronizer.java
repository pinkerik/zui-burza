import java.util.Date;
import java.util.concurrent.Semaphore;


public class Synchronizer {

	Semaphore readingSem;
	Semaphore parsingSem;
	Date time;
	
	public Synchronizer(int count){
		readingSem = new Semaphore(count);
		parsingSem = new Semaphore(count);
		time = null;
	}
}
