
public class finding {
	
	public String what1;
	public String what2;
	public long howMany;
	
	public finding(String w1, String w2, long hM){
		this.what1 = w1;
		this.what2 = w2;
		this.howMany = hM;
	}
	
	public boolean equals2(finding f){
		boolean temp = false;
	    if(what1.equals(f.what1) && what2.equals(f.what2)){
	    	temp = true;
	    }
		return temp;
	}
	
	public boolean equals3(String S1, String S2){
		boolean temp = false;
	    if(what1.equals(S1) && what2.equals(S2)){
	    	temp = true;
	    }
		return temp;
	}
	
	public void increase(){
		howMany++;
	}
	

}
