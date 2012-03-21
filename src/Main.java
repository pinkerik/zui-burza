import java.util.ArrayList;
import java.io.IOException;
import java.lang.IllegalMonitorStateException;


public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	
	
	
	
	public static void main(String[] args) throws InterruptedException {
		if(args.length == 0){
			System.err.println("Error: No input files entered, exiting");
			return;
		}
		
		Agent agent = new Agent(args);
		agent.start();
		
//		Commodity C = new Commodity();
		

	}

}
