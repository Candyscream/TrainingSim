package Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AlternativeStringHandler {
	//private static Random r= ThreadLocalRandom.current();
	private static Random r= new Random();
	private ArrayList<ArrayList<String>> category;
	
	public AlternativeStringHandler(){
		category= new ArrayList<ArrayList<String>>();
	}
	
	public void addCategory(String[] elements){
		ArrayList<String> cat=new ArrayList<String>();
		for (int i=0;i<elements.length;i++){cat.add(elements[i]);}
		category.add(cat);
	}
	public String getRandomString(int mi, int ma){
		//mostly for random descriptive adjectives
		String out="", lastout="";
		int num1, num2, min, max, n;
		max=Math.max(mi, ma);
		max=Math.max(max,0);
		min=Math.max(mi,0);
		if (max-min==0)n=min;
		else {n=min+r.nextInt(max-min+1);}
		
		for (int i=0; i<n;i++){
			
			
			if (category.isEmpty()){ 
				return out;}
			num1=r.nextInt(category.size());
			if (category.get(num1).size()<=0) lastout="";
			else if (category.get(num1).size()==1) {
						if (i>0 ) lastout =", "+ category.get(num1).get(0);
						else lastout = category.get(num1).get(0); }
			else { 		num2=r.nextInt(category.get(num1).size()-1);	
						if (i>0 ) lastout =", "+category.get(num1).get(num2);
						else lastout = category.get(num1).get(num2);}
			category.remove(num1);
			out+=lastout;
		}

		return out;}
	
	
}
