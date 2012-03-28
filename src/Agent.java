import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class Agent {
	
	String[] files;
	int c;
	
	
	Commodity commodities[];
	Synchronizer sync;
	
	Observation O;
	
	public Agent(String[] files){
		this.files = files;
		c = this.files.length;
		
		commodities = new Commodity[c];
		sync = new Synchronizer(c);
		
		O = new Observation("ASK");
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
			/// ta 40 je tam preto lebo ja tam mam cestu, zmen si to na 0; Mozno to treba nejak inak vyriesit
			commodities[i] = new Commodity(filename.substring(40,filename.indexOf('_')),filename,sync,O);
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
		
		// MAIN LOOP START
		for(int j=0;j<(60480*5);j++){ // 3 dni = 25920, 7 dni = 60480
			System.out.println("---------------- "+j+" ----------------");
			sync.add(Calendar.SECOND,10);
			this.release();
			
			
			
			this.lock();
			O.Refresh();
		}
		O.Out();
		// MAIN LOOP END
		
		sync.end = true;
		Thread.sleep(200);
		
		this.release();
		
		Thread.sleep(200);
		
		
		
		System.out.println("main finished");
	}
}
