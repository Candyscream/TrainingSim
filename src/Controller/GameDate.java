package Controller;

import java.text.DecimalFormat;
import java.text.Format;

public class GameDate {
	long time;
	
	public GameDate(int day, int hours, int minutes){
		this.time=minutes+60*hours+60*24*day;
	}
	
	public long getTime(){ return time;}
	public long setTime(long time){ this.time=time; return time;}
	public long getTimeTill(int d, int h, int m){
		GameDate tDate=new GameDate(getDay()+d,getHour()+h,getMinute()+m);
		return tDate.getTime()-getTime();
	}
	
	public GameDate clone(){
		return new GameDate(getDay(),getHour(),getMinute());
	}
	
	public int getDay(){
		return (int)time/(60*24);
	}
	public int getWeek(){
		return getDay()/7;
	}
	
	
	public int getMinute(){
		return (int)(time)%60;
	}
	
	public int getHour(){
		return (int)(time/(60))%24;
	}
	
	public String printDate(){
		String[] day={"Mon","Tue","Wen","Thu","Fri","Sat","Sun"};
		DecimalFormat format = new DecimalFormat("00"); 
		return "Day "+getDay()+" ("+day[getDay()%7]+") "+getHour()+":"+format.format(getMinute());
	}
	
	@Override
	public String toString(){ return printDate();}
	
	public static void main(String[] args){
		GameDate date=new GameDate(13,17,60);
		System.out.println(date.printDate());
	}

}
