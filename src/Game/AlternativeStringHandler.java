package Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AlternativeStringHandler {
	private ArrayList<ArrayList<String>> category;
	
	public AlternativeStringHandler(){
		category= new ArrayList<ArrayList<String>>();
	}
	
	public void addCategory(String[] elements){
		ArrayList<String> cat=new ArrayList<String>();
		for (int i=0;i<elements.length;i++){cat.add(elements[i]);}
		category.add(cat);
	}
	public String getRandomString(){
		if (category.isEmpty()){ 
			return "";}
		Random r = ThreadLocalRandom.current();
		int num=r.nextInt(category.size()-1);
		return category.get(num).get(r.nextInt(category.get(num).size()-1));
	}
	
	
}
