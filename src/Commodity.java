import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Commodity extends Thread{
	
	public static final String path = "/home/tomas/";
	
	public static final float MAXVAL = Float.MAX_VALUE / 10;
	
	public static final String[] SChanges = {"raise","lower","same"};
	public static final char[] CChanges = {'+','-','='};
	public static final int RAISE = 0;
	public static final int LOWER = 1;
	public static final int SAME = 2;
	
	Synchronizer sync;
	
	String name;
	String filename;
	
	FileInputStream fis = null;
	DataInputStream dis = null;
	BufferedReader reader = null;
	
	Tick inTick;
	String inLine;
	
	boolean imFinished;
	
	static final int tickType = Tick.BID;
	float average,lastAverage;
	float total;
	int priceChange;

	public Commodity(String name,String filename,Synchronizer sync) throws InterruptedException{
		
		super(name);
		
		this.name = name;
		this.filename = filename;
		this.sync = sync;
		this.inTick = new Tick();
		
		this.imFinished = false;
		
		lastAverage = 0;
		
		try {
			fis = new FileInputStream(path+this.filename);		
		    dis  = new DataInputStream(fis);
		    reader = new BufferedReader(new InputStreamReader(dis));
		    reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void lock() throws InterruptedException{
		sync.readingSem.acquire();
		sync.parsingSem.release();
	}
	
	public void release() throws InterruptedException{
		sync.readingSem.release();
		sync.parsingSem.acquire();
	}
	
	public boolean finished(){
		return imFinished;
	}
	
	private void synchronize() throws InterruptedException, IOException{
		int skip = 0;
		while(inTick.time.compareTo(sync.time) < 0){
			inTick.set(reader.readLine());
			skip++;
		}
		
//		Thread.sleep(200);
		
		System.out.println(filename+" skipped: "+skip);
	}
	
	private void averageTick(){
		if(inTick.type == tickType){
			average += inTick.price;
			total++;
		}
	}
	
	private void compareChange(){
		if(total == 0) average = lastAverage;
		else average /= total;
		
    	if(average > lastAverage) priceChange = RAISE;
    	else if(average < lastAverage) priceChange = LOWER;
    	else priceChange = SAME;
    	lastAverage = average;
	}
	
	@Override
	public void run(){
		
		try {
			this.lock();
			inTick.set(reader.readLine());
			
			this.release();
			// MAIN Thread working
			this.lock();
			
			this.synchronize();
			
		    this.release();
		    // MAIN Thread working
		    
		    // MAIN LOOP START
		    while(true){
		    	this.lock();
		    	if(sync.end == true) break;
		    	
		    	
		    	average = total = 0;
		    	while(inTick.time.compareTo(sync.time) < 0){
		    		averageTick();
		    		
		    		inLine = reader.readLine();
		    		if(inLine != null){
		    			inTick.set(inLine);
		    		}else{
		    			imFinished = true;
		    			break;
		    		}
		    	}
		    	
		    	compareChange();

		    	this.release();
		    }
		    // MAIN LOOP END
		   
		    
		    
		    fis.close();
		    System.out.println(name+" finished");
		    
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
	
}
