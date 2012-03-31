import java.io.IOException;
import java.util.Calendar;


public class Agent {
	
	String[] files;
	int c;
	
	Commodity commodities[];
	Synchronizer sync;
	
	Analyzer analyzer;
	
	long startTime,totalTime;
	
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
	
	public boolean threadsWorking(){
		for(Commodity c : commodities) if(c.finished()) return false;
		return true;
	}
	
	public void start() throws InterruptedException{
		startTime = System.currentTimeMillis();
		
//		ArrayList<Commodity> commodities = new ArrayList<Commodity>(5);
		
		
		
		sync.readingSem.acquire(c);
		sync.parsingSem.acquire(c);
		
		int i = 0;
		for(String filename : files){
			commodities[i] = new Commodity(filename.substring(0,filename.indexOf('_')),filename,sync);
			commodities[i].start();
			i++;
		}
		
		analyzer = new Analyzer(commodities);	// DONT MOVE IT FROM HERE
		
		
		this.release();
		// Threads working
	
		System.out.println("Releasing threads");
		
		this.lock();
		
		// Looking for max time .. to synchronize all commodities
		for(Commodity c : commodities){
			System.out.println(c.name+": "+c.inTick.time);
			if(sync.time == null || sync.time.compareTo(c.inTick.time) < 0) sync.time = c.inTick.time;
		}
		
		System.out.println("max date is: "+sync.time);
		
//		System.out.print("press Enter");
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		this.release();
		
		System.out.println("Threads are synchronizing");
		
		this.lock();
		
//		System.out.print("press Enter");
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// MAIN LOOP START
		int j = 0;
		while(threadsWorking()){
			if(j > 1000) break;
			j++;
//			System.out.println("---------------- "+j+" ----------------");
			sync.add(Calendar.SECOND,20);
//			sync.add(Calendar.MINUTE,1);
			this.release();
			
			
			
			this.lock();
			
//			System.out.println(commodities[0].name+" BID change: "+Commodity.SChanges[commodities[0].priceChange]);
//			System.out.println(commodities[1].name+" BID change: "+Commodity.SChanges[commodities[1].priceChange]);
			
			if(j==1) continue;
			analyzer.iteration();
		}
		// MAIN LOOP END
		
		analyzer.finish();
		
		sync.end = true;
		Thread.sleep(200);
		
		this.release();
		
		Thread.sleep(200);
		
		totalTime = System.currentTimeMillis() - startTime;
		
		System.out.println("main finished in "+totalTime+" millis");
	}
}
