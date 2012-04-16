import java.util.ArrayList;
import java.util.Calendar;

public class Analyzer {
	
	public static final String[] Days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
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
			total[y.priceChange]++;
		}
		
		public void print(){
			for(int j=0;j<3;j++){
				for(int i=0;i<3;i++){
					System.out.println("P("+x.name+Commodity.CChanges[i]+"|"+y.name+Commodity.CChanges[j]+") = "+Math.round(iter[i][j]/total[j]*100.0)+"%");
				}
				System.out.println("");
			}
		}
	}
		
	Commodity[] commodities;
	ArrayList<Comparation> comparations;
	
	float[][] byday = new float [7][3];
	float[] bydayTotal = new float[7];
	float[] bychangeTotal = new float[3];
	Commodity a;
	
	public Analyzer(Commodity[] commodities){
		this.commodities = commodities;
		
		comparations = new ArrayList<Comparation>(commodities.length*2);
		for(Commodity a : this.commodities){
			for(Commodity b : this.commodities){
				if(!a.equals(b)) comparations.add(new Comparation(a,b));
			}
		}
		
		a = this.commodities[0];
	}
	
	public void iteration(){
		for(Comparation c : comparations) c.iteration();
		
		byday[a.sync.cal.get(Calendar.DAY_OF_WEEK)-1][a.priceChange]++;
		bydayTotal[a.sync.cal.get(Calendar.DAY_OF_WEEK)-1]++;
		bychangeTotal[a.priceChange]++;
	}
	
	public void finish(){
		for(Comparation c : comparations) c.print();
		
		System.out.println(a.name);
		for(int i=0;i<7;i++){
			System.out.println(Days[i]);
			System.out.println(Commodity.SChanges[Commodity.RAISE]+" "+Math.round(byday[i][Commodity.RAISE]/bydayTotal[i]*100.0)+"%");
			System.out.println(Commodity.SChanges[Commodity.LOWER]+" "+Math.round(byday[i][Commodity.LOWER]/bydayTotal[i]*100.0)+"%");
			System.out.println(Commodity.SChanges[Commodity.SAME]+" "+Math.round(byday[i][Commodity.SAME]/bydayTotal[i]*100.0)+"%");
			System.out.println("");
		}
		
		for(int i=0;i<7;i++){
			System.out.println(Days[i]);
			System.out.println(Commodity.SChanges[Commodity.RAISE]+" "+Math.round(byday[i][Commodity.RAISE]/bychangeTotal[Commodity.RAISE]*100.0)+"%");
			System.out.println(Commodity.SChanges[Commodity.LOWER]+" "+Math.round(byday[i][Commodity.LOWER]/bychangeTotal[Commodity.LOWER]*100.0)+"%");
			System.out.println(Commodity.SChanges[Commodity.SAME]+" "+Math.round(byday[i][Commodity.SAME]/bychangeTotal[Commodity.SAME]*100.0)+"%");
			System.out.println("");
		}
	}
}
