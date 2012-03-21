import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Main {

	/**
	 * @param args
	 */
	
	
	
	
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("Error: No input files entered, exiting");
			return;
		}
		
		ArrayList<Commodity> commodities = new ArrayList<Commodity>();
		
		Semaphore readingSem = new Semaphore(5);
		Semaphore parsingSem = new Semaphore(5);
		
		int b = 0;
		
		for(String filename : args){
//			if(b==0) continue;
//			System.out.println(filename);
//			System.out.println(filename.indexOf('_'));
			System.out.println(filename.substring(0,filename.indexOf('_')));
			
			String name = filename.substring(0,filename.indexOf('_'));
			
			
			
//			commodities.add(new Commodity(filename.substring(0,filename.indexOf('_')),filename,readingSem,parsingSem));
			new Commodity(name,filename).start();
//			if(b!=0) tmp.start();
			b++;
//			if(b==1) break;
//			break;
//			commodities.get(commodities.size()-1).start();
		}
		
//		Commodity C = new Commodity();
		
		System.out.println("finished");

	}

}
