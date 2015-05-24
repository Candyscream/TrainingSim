package cavity_old;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CavityV1 implements Cloneable {
	/* arrays: index = depth in cm, 0=sphincter*/
	/* Cap: diameter in cm */
	private float [] minCap; /* insertions under minCap won't produce soreness, insertions over it will stretch */
	private float [] curCap; /* current level of stretch*/
	private float [] maxCap; /* insertions over maxCap will produce pain, soreness and stretch */
	private float soreness;
	private float looseness; /* a high loosenes slows down the regeneration from curCap to minCap, raised by overstretching */
	private float pain;
	private float topCap; /* physical limit to which one can stretch */
	
	public float[] getMinCap() {
		return minCap;
	}
	public void setMinCap(float[] minCap) {
		this.minCap = minCap;
	}
	public float[] getCurCap() {
		return curCap;
	}
	public void setCurCap(float[] curCap) {
		this.curCap = curCap;
	}

	public float[] getMaxCap() {
		return maxCap;
	}
	public void setMaxCap(float[] maxCap) {
		this.maxCap = maxCap;
	}

	public float getSoreness() {
		return soreness;
	}
	public void setSoreness(int soreness) {
		this.soreness = Math.max(0,soreness);
	}
	public float getLooseness() {
		return looseness;
	}
	public void setLooseness(int looseness) {
		this.looseness = Math.max(0,looseness);
	}
	public float getTopCap() {
		return topCap;
	}
	public void setTopCap(int topCap) {
		this.topCap = topCap;
	}	
	public CavityV1(int depth, float minC, float maxC, float topC){
		float miC= Math.max(minC, 0);
		float maC= Math.max(maxC, 0);
		float toC= Math.max(topC, 1);
		int d  = Math.max(depth, 1);
		soreness=0;
		looseness=0;
		pain=0;
		
		topCap=topC;
		minCap=new float[d];
		maxCap=new float[d];
		curCap=new float[d];
		for (int i=0; i<depth; i++){
			minCap[i]=miC*(float)(d-i)/d;
			maxCap[i]=maC*(float)(d-i)/d;
			curCap[i]=0;
		}
	}
	public CavityV1(float looseness, float soreness, float pain, float[] minCap, float[] curCap, float[]maxCap, float topCap){
		this.looseness=looseness;
		this.soreness=soreness;
		this.pain=pain;
		this.minCap=minCap.clone();
		this.curCap=curCap.clone();
		this.maxCap=maxCap.clone();
		this.topCap=topCap;
		}
	
	public CavityV1(){ this(51,2,3,14);};
	

	

	public ArrayList<String> printCap() {
		DecimalFormat format = new DecimalFormat("00.0"); 
		ArrayList<String> capacys = new  ArrayList<String>();
		for (int i=0; i<minCap.length; i++){
			capacys.add(i, ""+format.format(minCap[i])+" | "+format.format(curCap[i])+" | "+format.format(maxCap[i]));
		}
		
		return capacys;
	
		
	}
	public Object clone(){
		CavityV1 clone = new CavityV1(looseness, soreness, pain, minCap, curCap, maxCap, topCap);
		return clone;
	}
	
	public float getPain() {
		return pain;
	}
	public void setPain(int pain) {
		this.pain = pain;
	}
	
		
	public int takeInsertion(int minutes, float[] input){
		int x=10;
		int t=minutes*x;
		int curD=0;//depth of insertion
		int minDepth=0, maxDepth=0;
		boolean setMindepth=false;
		
		int dir=1;
		if (input[input.length-1]>maxCap[0]+0.6f){t=0; System.out.println("This toy is much too big to take for now.");}
		for(;t>0&&pain<100;t--){
			for (int seg=0; seg<=curD;seg++){
				int curPos=input.length-seg-1;
				
				if (curCap[seg]<input[curPos]){
					curCap[seg]=curCap[seg]*0.99f+input[curPos]*0.01f;
					if (minCap[seg]<input[curPos]) soreness+=(1+(input[curPos]-curCap[seg])*input[curPos]/curCap[seg])*0.005;
					}
				
				
				if (maxCap[seg]<input[curPos]){
					soreness+=(input[curPos]-maxCap[seg])*0.01;
					pain+=(input[curPos]-maxCap[seg])*0.01;
					}
				
				if (curCap[seg]>1) curCap[seg]-=0.005/x;
				if (curCap[seg]>minCap[seg]){
					minCap[seg]=minCap[seg]*0.9995f+curCap[seg]*0.0005f;}
				if (1.05f*curCap[seg]>maxCap[seg]){
					maxCap[seg]=maxCap[seg]*0.999f+1.05f*curCap[seg]*0.001f;}
				if (seg+1<minCap.length && curCap[seg+1]<curCap[seg]){
					curCap[seg+1]=curCap[seg+1]*0.97f+curCap[seg]*0.03f;}
				
				soreness-=1/x;
				
			}
			
			
			
			if (dir==1&&(curD+1>=minCap.length||input[input.length-1]>Math.max(maxCap[curD],curCap[curD])+0.5||curD+1>input.length-1)){
				dir=-1; if (!setMindepth){minDepth=curD;setMindepth=true;}
				maxDepth=Math.max(maxDepth, curD);
			}
			/* when moving deeper and the next segment doesen't exest or is too tight pull out*/
			else if (dir==-1&&curD<=1){dir=1; }
			/* when almost pulled out push in again */
			curD+=dir;
			pain+=soreness/30;
			pain=Math.max(0,pain-5);
		}
			System.out.println("Stop after "+(minutes-t/x)+" of "+minutes+" minutes\nminDepth= "+minDepth+", maxDepth= "+maxDepth);
			return (int)(t/x);
	}
	public void rest(int minutes){
		soreness=Math.max(0,soreness-minutes);
		pain=Math.max(0,pain-minutes);
		for (int seg=0; seg<curCap.length;seg++){
			if (curCap[seg]>0.05f) curCap[seg]-=0.05f;
			if (minCap[seg]>1) minCap[seg]-=0.05f;
			if (maxCap[seg]>2) maxCap[seg]-=0.05f;
			if (1.05f*curCap[seg]>maxCap[seg]){maxCap[seg]=maxCap[seg]*0.995f+1.05f*curCap[seg]*0.005f;}
		}
	}
}


