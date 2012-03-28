import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Observation {
	
	int Type;
	int all;
	Map<String,Float> mp;
	Map<String,Float> old_mp;
	//Map<String,String> findings;
	List<finding> robin = new ArrayList<finding>();
	List<String> findings;
	
	
	String[] postfix = {"+","-","="};
	String[] commodity = {"PIVO","MASO","ROZKY","ZEMIAKY","CHLIEB"};
	String[] commodity_postfix = new String[15];

	//ArrayList robin = new ArrayList();
	
	public Observation(String type){
		
		if(type.equals("BID")){
			Type = 0;
		}else if(type.equals("ASK")){
			Type = 1;
		}else if(type.equals("TRADE")){
			Type = 2;
		}
		
		mp = new HashMap<String,Float>();
		all = 0;
		
	}
	
	public void AddTick(Tick tTick,String name){
		if(tTick.type == Type ){
			if(!mp.containsKey(name)){
				mp.put(name, tTick.price);
			}else{
		        Set s=mp.entrySet();
		        Iterator it=s.iterator();

		        while(it.hasNext())
		        {
		        	Map.Entry m =(Map.Entry)it.next();

		        	if(m.getKey().equals(name)){
		        		float h = (float)m.getValue() + tTick.price;
		        		m.setValue(h);
		        	}
		        }
			}
		
		}		
	}
	
	public void NewPeriod(String name){
        Set s=mp.entrySet();
        Iterator it=s.iterator();

        while(it.hasNext())
        {
        	Map.Entry m =(Map.Entry)it.next();

        	if(m.getKey().equals(name)){
        		float h = 0;
        		m.setValue(h);
        	}
        }	  
        
	}
	
	// robi priemernu hodnotu
	public void StopPeriod(String name,int X){
        Set s=mp.entrySet();
        Iterator it=s.iterator();

        while(it.hasNext())
        {
        	Map.Entry m =(Map.Entry)it.next();

        	if(m.getKey().equals(name)){
        		m.setValue((Float) m.getValue() / X);
        		//System.out.println(m.getKey() + ": " + m.getValue());
        	}
        }	  
        
	}
	
	public void Refresh(){
		if(old_mp == null){
    		old_mp = new HashMap<String,Float>(mp);
        }
        
        findings  = new ArrayList<String>();;
        
        Set s=mp.entrySet();
        Iterator it=s.iterator();
        Set s2=old_mp.entrySet();
        Iterator it2=s2.iterator();
        
        while(it.hasNext() && it2.hasNext() )
        {
        	Map.Entry m =(Map.Entry)it.next();
        	Map.Entry m2 =(Map.Entry)it2.next();

        	if(!findings.contains(m.getKey().toString())){
        		if((Float)m.getValue() > (Float)m2.getValue()){
        			findings.add(m.getKey().toString() + "+");
        		}else if ((Float)m.getValue() < (Float)m2.getValue()){
        			findings.add(m.getKey().toString() + "-");
        		}else{
        			findings.add(m.getKey().toString() + "=");
        		}
        	}
        }
        
        old_mp = new HashMap<String,Float>(mp);
        
        
        /*for (int i = 0; i < findings.size();i++){
        	System.out.println(findings.get(i));
        }*/
        
        // Vytvorim len vsetky mozne komodity s +,-,=, a ulozim do pola commodity_postfix
		if (robin.size() == 0){
			int index = 0;
			for (int i = 0; i < 5; i++){
				for (int j = 0; j < 3; j++){
					commodity_postfix[index] = commodity[i] + postfix[j];
					index++;
				}
			}
		}
		
		// Vytvorenie tabulky kazdy s kazdym, 180 moznosti
		if (robin.size() == 0){
			for (int i = 0; i < commodity_postfix.length; i++){
				for (int j = 0; j < commodity_postfix.length; j++){
					if(!commodity_postfix[i].substring(0,commodity_postfix[i].length()-1).equals(commodity_postfix[j].substring(0,commodity_postfix[j].length()-1))	){
	            	   finding temp = new finding(commodity_postfix[i],commodity_postfix[j],0);
	                   robin.add(temp);
					}
				}
			}
		}else{
			// update tabulky robin
	        for (int i = 0; i < findings.size();i++){
	            for (int j = 0; j < findings.size();j++){
                	if(!findings.get(i).substring(0,findings.get(i).length()-1).equals(findings.get(j).substring(0,findings.get(j).length()-1))	){
                		finding temp = new finding(findings.get(i),findings.get(j),0);
                		//System.out.println("tento temp porovnavam: " + temp.what1 + "," + temp.what2 );
                    	for (int ii = 0; ii < robin.size(); ii++){
                    		if( robin.get(ii).equals2(temp)){
                    			robin.get(ii).increase();
                    			break;
                    		}
                    	}
                	}
	            }
	        }
		}
        
        /// koniec REFRESH
	}
	
	public void Out(){
		System.out.println("--- RESULTS ---");
		
		String[] postfix = {"+","-","="};
		String[] commodity = {"PIVO","MASO","ROZKY","ZEMIAKY","CHLIEB"};
		
		for (int i = 0; i < commodity_postfix.length; i++){
		
			for(int k = 0; k < commodity.length;k++){
				
				if(!commodity_postfix[i].substring(0,commodity_postfix[i].length()-1).equals(commodity[k])){	
					
					int i1 = this.GetIndex(commodity_postfix[i], commodity[k] + postfix[0]) ; // +
					int i2 = this.GetIndex(commodity_postfix[i], commodity[k] + postfix[1]) ; // -
					int i3 = this.GetIndex(commodity_postfix[i], commodity[k] + postfix[2]) ; // =
					
					float all = (float)robin.get(i1).howMany + (float)robin.get(i2).howMany + (float)robin.get(i3).howMany;
					
					System.out.println("P(" + commodity_postfix[i] + "|" + commodity[k] + postfix[0] + ") = " + (robin.get(i1).howMany/all) + "   (" + robin.get(i1).howMany + "," + all + ")" );
					System.out.println("P(" + commodity_postfix[i] + "|" + commodity[k] + postfix[1] + ") = " + (robin.get(i2).howMany/all) + "   (" + robin.get(i2).howMany + "," + all + ")" );
					System.out.println("P(" + commodity_postfix[i] + "|" + commodity[k] + postfix[2] + ") = " + (robin.get(i3).howMany/all) + "   (" + robin.get(i3).howMany + "," + all + ")" );
					System.out.println();
					
				}
						
			}
				
			
		}	
	}
	
	private int GetIndex(String S1, String S2){
		int temp = 0;
    	for (int i = 0; i < robin.size(); i++){
    		if( robin.get(i).equals3(S1, S2)){
    			temp = i;
    			break;
    		}
    	} 
		return temp;
	}
}




















