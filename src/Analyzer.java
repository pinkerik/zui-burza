import java.util.ArrayList;

public class Analyzer {
	
	private class Comparation{
		Commodity x,y;
		
		float[][] iter;
		float total[];
		
		public Comparation(Commodity x,Commodity y){
			this.x = x;
			this.y = y;
			
			iter = new float[3][3];
			total = new float[3];
		}
		
		public void iteration(){
			iter[x.priceChange][y.priceChange]++;
			total[x.priceChange]++;
		}
		
		public void print(){
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					System.out.println("P("+x.name+Commodity.CChanges[i]+","+y.name+Commodity.CChanges[j]+") = "+Math.round(iter[i][j]/total[i]*100.0)+"%");
				}
				System.out.println("");
			}
		}
	}
		
	Commodity[] commodities;
	ArrayList<Comparation> comparations;
	
	public Analyzer(Commodity[] commodities){
		this.commodities = commodities;
		
		comparations = new ArrayList<Comparation>(commodities.length*2);
		for(Commodity a : this.commodities){
			for(Commodity b : this.commodities){
				if(!a.equals(b)) comparations.add(new Comparation(a,b));
			}
		}
	}
	
	public void iteration(){
		for(Comparation c : comparations) c.iteration();
	}
	
	public void finish(){
		for(Comparation c : comparations) c.print();
	}
}
