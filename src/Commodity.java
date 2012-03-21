import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;


public class Commodity extends Thread{
	
	Thread thread;
	Semaphore readingSem;
	Semaphore parsingSem;
	
	String filename;
	
	FileInputStream fis = null;
	DataInputStream dis = null;
	BufferedReader reader = null;
	
	public ArrayList<Tick> lol = new ArrayList<Tick>();

	public Commodity(String name,String filename){
		
		super(name);
		
		this.filename = filename;
		this.readingSem = readingSem;
		this.parsingSem = parsingSem;
		
//		thread = new Thread(name);
		
		try {
			fis = new FileInputStream(this.filename);		
		    dis  = new DataInputStream(fis);
		    reader = new BufferedReader(new InputStreamReader(dis));
		    reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("constructing commodity");
		
//		thread.start();
	}
	
	@Override
	public void run(){
		System.out.println("running");
		
		int total = 0;
		
		try {
			
			Tick inTick = new Tick();
			
		    for (int i = 0; i < 8000; i ++){
		    	//lol.add(new Tick(reader.readLine()));
		    	if(inTick.set(reader.readLine()) == true) total++;
		    }

//		    System.out.print("size: " + lol.size());
		    System.out.println(filename+" "+total);
		    
		    fis.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
