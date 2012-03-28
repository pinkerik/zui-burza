
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Commodity extends Thread{
	
	Synchronizer sync;
	
	String name;
	String filename;
	
	FileInputStream fis = null;
	DataInputStream dis = null;
	BufferedReader reader = null;
	
	Tick inTick;
	
	Observation observer;

	public Commodity(String name,String filename,Synchronizer sync, Observation O) throws InterruptedException{
		
		super(name);
		
		this.name = name;
		this.filename = filename;
		this.sync = sync;
		this.inTick = new Tick();
		this.observer = O;
		
//		sync.parsingSem.acquire();
		
		try {
			fis = new FileInputStream(this.filename);		
		    dis  = new DataInputStream(fis);
		    reader = new BufferedReader(new InputStreamReader(dis));
		    reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	public void synchronize() throws InterruptedException, IOException{
		int skip = 0;
		while(inTick.time.compareTo(sync.time) < 0){
			inTick.set(reader.readLine());
			skip++;
		}
		
//		Thread.sleep(200);
		
		System.out.println(filename+" skipped: "+skip);
	}
	
	@Override
	public void run(){
		
		int total = 0;
		
		System.out.println("running");
		
		try {
			this.lock();
			
			System.out.println("reading first line");
			inTick.set(reader.readLine());
//			Thread.sleep(200);
			
//			System.out.println(name+": "+inTick.time);
			
			this.release();
			// MAIN Thread working
			this.lock();
			
			this.synchronize();
			
		    this.release();
		    // MAIN Thread working
		    
		    // MAIN LOOP START
		    while(true){
		    	this.lock();
		    	if(sync.end == true){
		    		System.out.println("breaking out");
		    		break;
		    	}
		    	
		    	
				    
		    	total = 0;
		    	observer.NewPeriod(name);
		    	while(inTick.time.compareTo(sync.time) < 0){
		    		if(inTick.set(reader.readLine())){
		    			total++;
		    			//spracovanie pomocou Observation
		    			observer.AddTick(inTick,name);

		    		
		    			//koniec spracovanie pomocou Observation
		    		}
		    	}
		    	if (total != 0 ){
		    		observer.StopPeriod(name,total);
		    	}
		    	
		    	//System.out.println(filename+" read: "+total);
		    	this.release();
		    }
		    // MAIN LOOP END
		   
		    
		    
		    fis.close();
		    //System.out.println(name+" finished");
		    
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
	
}
