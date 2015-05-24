package cavity2;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Game.*;

public class Cavity implements Cloneable{
	public static int TD=10; //Timedivision
	public static float CUC=(0.1f/TD); 	//CurCap Update speed
	public static float MAC=(0.01f/TD); 	//MaxCap Update speed
	public static float MIC=(0.005f/TD); 	//MinCap Update speed
	public static float MAX_CR=0.01f;				//Maximal Cap regeneration speed
	public static float CUC_REGMIN=0.05f;				
	public static float MIC_REGMIN=1f;	
	public static float MAC_REGMIN=2f;	
	// TODO: add more static variables for balancing (regeneration speed, etc.)
	
	String name="cavity";
	private float [][]cap;
	private int prehDepth;
	private float soreness, topCap, pain;
	
	
	public Cavity(int depth, float minC, float maxC, float topC, int prehDepth){
		topCap=topC;
		initCaps(depth,minC,maxC);
		setPain(0);
		setSoreness(0);
		this.prehDepth=prehDepth;
	
	}
	
	public Cavity() {
		this(70,0.5f,1.5f,15.0f,50);
	}

	public CavPenetrationResponse takePenetration(InsertionAble input, int minutes) {
		Cavity clone=this.clone();
		int topD; 	//max insertion Depth
		if (input.isPrehensile()) {topD=Math.min(input.getLength(),getDepth());}
		else  {topD=Math.min(input.getLength(),getPrehDepth());};
		int t=(int)(minutes*TD); //Timeunites: minutes*Timedivision, actions per Minute...
		int curD=0; //current depth of insertion
		int minD=0;
		int maxD=0;//???
		int dir=1;	//movement direction
		float pl=0;
		boolean setMindepth=false;
		CavPenetrationResponse out;
		
		if (input.getHeadDiam()>getMacAt(0)+0.41f){
			t=0; 
			out=new CavPenetrationResponse(minD, maxD, pl/100, this, clone, input,minutes-(t/TD));
			}
		else {
		for(;t>0&&pain<100;t--){ /*For each Minute*PB */
			for (int seg=0; seg<curD;seg++){ /* Update caps, lsns, srns, tc. for each segment of the cavity with the InsertAble position */
				int inputSeg=input.getLength()-1-seg;
				float cuc=getCucAt(seg), mac=getMacAt(seg), mic=getMicAt(seg), diam=input.getDiameterAt(inputSeg), nextcuc=getCucAt(seg+1);
				
					pl+=1;
					soreness+=1;
				if (cuc<diam&&cuc<mic){pl+=1f;}
				
				if (cuc<diam){
					if (mic<diam){
						cap[1][seg]=cuc*(1f-CUC/2)+diam*(CUC/2);
					 	soreness+=((diam-cuc))*0.002f;} //fragwürdig
					else cap[1][seg]=cuc*(1f-CUC)+diam*CUC;
					}
				
				if (mac<diam){
					pl-=diam-mac;
					soreness+=(diam-mac)*2f/TD;
					pain+=(diam-mac)*0.02f/TD;
					}
				
				if (cuc>1) cap[1][seg]-=0.005/TD; // regenerate current Capacity
				
				if (0.8f*cuc>mic){
					cap[0][seg]=mic*(1f-MIC)+cuc*0.8f*MIC;}
				
				if (1.05f*cuc>mac){
					cap[2][seg]=mac*(1f-MAC)+1.05f*cuc*MAC;}
				if (seg+1<topD && nextcuc<cuc){
					cap[1][seg+1]=nextcuc*0.97f+cuc*0.03f;}
				int test=TD;
				soreness=Math.max(0,soreness-(10f/TD));
				
			} 
			if (dir==1&&(curD+1>=topD||input.getHeadDiam()>Math.max(getMacAt(curD),getCucAt(curD))+0.5||curD+1>input.getLength()-1)){
				dir=-1; if (!setMindepth){minD=curD;setMindepth=true;} // update Direction, 
				maxD=Math.max(maxD, curD);
			}
			/* when moving deeper and the next segment doesen't exest or is too tight pull out*/
			else if (dir==-1&&curD<=1){dir=1; }
			/* when almost pulled out push in again */
			curD+=dir;
			pain+=soreness/30;
			pain=Math.max(0,pain-5);
		}
			out=new CavPenetrationResponse(minD, maxD, pl/100, this, clone, input,minutes-(t/TD));
			}
		
		System.out.println(out.defDescription);
		return out;
	}


	public String getDescription() {
		AlternativeStringHandler output=new AlternativeStringHandler();
		switch ((int)(getLooseness()/25)){
		case 1: {output.addCategory(new String[]{"tight"}); break;}
		case 2: {output.addCategory(new String[]{"slightly loose"}); break;}
		case 3: {output.addCategory(new String[]{"loose"}); break;}
		case 4: {output.addCategory(new String[]{"pretty loose","loose"}); break;}
		case 5: {output.addCategory(new String[]{"very loose"}); break;}
		default:{output.addCategory(new String[]{"incredibly loose","extremly sore"}); break;}
		}
		
		switch ((int)(soreness/25)){
		case 1: {break;}
		case 2: {output.addCategory(new String[]{"slightly sore"}); break;}
		case 3: {output.addCategory(new String[]{"sore"}); break;}
		case 4: {output.addCategory(new String[]{"pretty sore","sore"}); break;}
		case 5: {output.addCategory(new String[]{"very sore"}); break;}
		default:{output.addCategory(new String[]{"incredibly sore","extremly sore"}); break;}
		
		
		}
		return output.getRandomString()+" "+getLabel();
	}

	public int getLooseness() {
		return (int)(100*((cap[1][0]/topCap)+(cap[1][0]/(cap[2][0]+1))));
	}

	public String getLabel() {
		return name;
	}
	
	private float[] generateInitCapArray(int depth, float w){
		float val=Math.min(Math.max(w, 0), topCap);
		int d=Math.max(depth, 1);
		float[] c=new float[d];
		if (val==0)	{	for(int i=0; i<d;i++){c[i]=0;};  }
		else 		{ 	for(int i=0; i<d;i++){c[i]=(d-i)*(val/d);}; }
		return c;
	}

	public void setSoreness(int soreness) {
		float v=Math.max(0, soreness);
		this.soreness = v;
	}

	public float getPain() {
		return pain;
	}

	public void setPain(float pain) {
		this.pain = pain;
	}

	public void setTopCap(float topCap) {
		float v=Math.max(0.01f, topCap);
		this.topCap = v;
	}

	public float getMicAt(int cm) {
		if (cm<cap[0].length) return cap[0][cm];
		else return -1;
	}

	public float getCucAt(int cm) {
		if (cm<cap[1].length) return cap[1][cm];
		else return -1;
	}

	public float getMacAt(int cm) {
		if (cm<cap[2].length) return cap[2][cm];
		else return -1;
	}

	public int getDepth() {
		return cap[0].length;
	}

	public int getPrehDepth() {
		return prehDepth;
	}

	public float getTopCap() {
		return topCap;
	}

	public void initMinCap(float w) {
		cap[0]=generateInitCapArray(cap[0].length,w);
	}

	public void initCurCap(float w) {
		cap[1]=generateInitCapArray(cap[1].length,w);
	}

	public void initMaxCap(float w) {
		cap[2]=generateInitCapArray(cap[2].length,w);
	}

	public void initCaps(float wMin, float wMax) {
		initCurCap(0f);
		initMinCap(wMin);
		initMaxCap(wMax);
	}

	public void initCaps(int newDepth, float wMin, float wMax) {
		cap=new float[3][newDepth];
		initCaps(wMin,wMax);
	}

	public void setMicAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[0][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setMinCapAt() out of Bounds");
		//TODO: better Exception handling
	}

	public void setCucAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[1][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setCurCapAt() out of Bounds");
		//TODO: better Exception handling
	}

	public void setMacAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[2][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setCurCapAt() out of Bounds");
		//TODO: better Exception handling
	}
	
	public Cavity clone(){
		int depth=getDepth();
		Cavity clone =new Cavity(this.getDepth(), cap[0][0], cap[2][0], getTopCap(), this.prehDepth);
		for (int i=0; i<depth; i++){
				clone.setMicAt(i, cap[0][i]);  
				clone.setCucAt(i, cap[1][i]);
				clone.setMacAt(i, cap[2][i]);  
		} 
		clone.pain=getPain();
		return clone;}

	public void setDepth(int cm) {
		int oldDepth=cap[0].length;
		if (cap[0].length<cm){
			float tmpCap[][]=cap.clone();
			float endCap[][]=new float[3][0];
			cap=new float[3][cm];
			endCap[0]=generateInitCapArray(cm-oldDepth,tmpCap[0][tmpCap[0].length-1]);
			endCap[1]=generateInitCapArray(cm-oldDepth,tmpCap[1][tmpCap[0].length-1]);
			endCap[2]=generateInitCapArray(cm-oldDepth,tmpCap[2][tmpCap[0].length-1]);
			for (int i=0;i<tmpCap[0].length;i++){
				cap[0][i]=tmpCap[0][i];
				cap[1][i]=tmpCap[1][i]; 
				cap[2][i]=tmpCap[2][i]; }
			for (int i=0;i<endCap[0].length;i++){ 
				cap[0][i+oldDepth]=endCap[0][i];
				cap[1][i+oldDepth]=endCap[1][i];
				cap[2][i+oldDepth]=endCap[2][i];
			}
			
		};
	}

	public void setPrehDepth(int cm) {
		prehDepth=Math.min(Math.max(2,cm),cap[0].length+1);	
	}

	public ArrayList<String> printCap() {
		int depth=getDepth();
		DecimalFormat format = new DecimalFormat("00.0"); 
		ArrayList<String> capacys = new  ArrayList<String>();
		for (int i=0; i<depth; i++){
			capacys.add(i, ""+format.format(getMicAt(i))+" | "+format.format(getCucAt(i))+" | "+format.format(getMacAt(i)));
		}
		return capacys;
	}

	public float getSoreness() {
		return soreness;
	}
	
	public void addToCurCapAt(int seg, float diam){
		//setCucAt(seg,Math.max(CUC_REGMIN,getCucAt(seg)+ diam));
		if (diam<0 && getCucAt(seg)+diam>CUC_REGMIN){setCucAt(seg,getCucAt(seg)+diam);}
		else if (diam>0)setCucAt(seg,getCucAt(seg)+diam);
		
	}
	
	public void addToMinCapAt(int seg, float diam){
		if (diam<0 && getMicAt(seg)+diam>MIC_REGMIN){setMicAt(seg,getMicAt(seg)+diam);}
		else if (diam>0) setCucAt(seg,getMicAt(seg)+diam);
	}
	
	public void addToMaxCapAt(int seg, float diam){
		if (diam<0 && getMacAt(seg)+diam>MAC_REGMIN){setMacAt(seg,getMacAt(seg)+diam);}
		else if (diam>0) setCucAt(seg,getMacAt(seg)+diam);
	}

	public void rest(int minutes) {
		soreness=Math.max(0,soreness-minutes);
		pain=Math.max(0,pain-minutes);
		for (int seg=0; seg<getDepth();seg++){
			addToCurCapAt(seg, -MAX_CR);
			addToMinCapAt(seg, -MAX_CR);
			addToMaxCapAt(seg, -MAX_CR);
			if (1.05f*getCucAt(seg)>getMacAt(seg)){setMacAt(seg,getMacAt(seg)*0.995f+getCucAt(seg)*0.005f);}
		}
		
	}
}
