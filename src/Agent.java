import java.io.IOException;
import java.util.Date;


public class Agent {
	
	String[] files;
	int c;
	
	Commodity commodities[];
	Synchronizer sync;
	
	public Agent(String[] files){
		this.files = files;
		c = this.files.length;
		
		commodities = new Commodity[c];
		sync = new Synchronizer(c);
	}
	
	public void lock() throws InterruptedException{
		sync.readingSem.acquire(c);
		sync.parsingSem.release(c);
	}
	
	public void release() throws InterruptedException{
		sync.readingSem.release(c);
		sync.parsingSem.acquire(c);
	}
	
	public void start() throws InterruptedException{
		
//		ArrayList<Commodity> commodities = new ArrayList<Commodity>(5);
		
		
		
		sync.readingSem.acquire(c);
		sync.parsingSem.acquire(c);
		
		int i = 0;
		for(String filename : files){
			commodities[i] = new Commodity(filename.substring(0,filename.indexOf('_')),filename,sync);
			commodities[i].start();
			i++;
		}
		
		
		this.release();
		// Threads working
	
		System.out.println("threads are running");
		
		this.lock();
		
		// Looking for max time .. to synchronize all commodities
		for(Commodity c : commodities){
			System.out.println(c.name+": "+c.inTick.time);
			if(sync.time == null || sync.time.compareTo(c.inTick.time) < 0) sync.time = c.inTick.time;
		}
		
		System.out.println("max date is: "+sync.time);
		
		System.out.print("press Enter");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.release();
		
		System.out.println("threads are working again");
		
		this.lock();
		
		System.out.print("press Enter");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("main finished");
		
		this.release();
	}
}
