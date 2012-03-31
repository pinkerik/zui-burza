import java.util.ArrayList;

public class Analyzer {
	
	private class Comparation{
		Commodity x,y;
		
		float[][] iter;
		float total;
		
		public Comparation(Commodity x,Commodity y){
			this.x = x;
			this.y = y;
			
			iter = new float[3][3];
			
		}
		
		public void iteration(){
			iter[x.priceChange][y.priceChange]++;
			total++;
		}
		
		public void printMatrix(){
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					System.out.print(iter[i][j]+" ");
				}
				System.out.println("");
			}
		}
		
		public void print(){
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					System.out.println("P("+x.name+Commodity.CChanges[i]+","+y.name+Commodity.CChanges[j]+") = "+Math.round(iter[i][j]/total*100.0)+"%");
				}
				System.out.println("");
			}
		}
	}
	
	
	
	Commodity[] commodities;
	
	Comparation testComp;
	
	ArrayList<Comparation> comparations;
	
	public Analyzer(Commodity[] commodities){
		this.commodities = commodities;
		
		this.testComp = new Comparation(commodities[0],commodities[1]);
		
		comparations = new ArrayList<Comparation>(commodities.length*2);
		for(Commodity a : this.commodities){
			for(Commodity b : this.commodities){
				if(!a.equals(b)) comparations.add(new Comparation(a,b));
			}
		}
	}
	
	public void iteration(){
		testComp.iteration();
		
		for(Comparation c : comparations) c.iteration();
	}
	
	public void finish(){
		System.out.println("iterations: "+testComp.total);
//		testComp.printMatrix();
//		testComp.print();
		
		for(Comparation c : comparations) c.print();
	}
	
}
