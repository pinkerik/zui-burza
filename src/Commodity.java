import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;


public class Commodity {
	
	public ArrayList<Tick> lol = new ArrayList<Tick>();

	public Commodity(){
		
	    InputStream inZemiaky = null;
		try {
			inZemiaky = new FileInputStream("ZEMIAKY_20110515_20110700.txt");
		    DataInputStream inzemiaky  = new DataInputStream(inZemiaky);
		    BufferedReader rZemiaky  = new BufferedReader(new InputStreamReader(inzemiaky ));
		    rZemiaky.readLine();
		    for (int i = 0; i < 8000000; i ++){
		    	lol.add(new Tick(rZemiaky.readLine()));
		    }

		    System.out.print("size: " + lol.size());
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
