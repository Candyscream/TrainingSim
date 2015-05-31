package cavity2;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Controller.GameDate;
import Game.*;

public class Cavity implements Cloneable{
	public static int TD=10; //Timedivision
	public static float CUC=(0.1f/TD); 	//CurCap Update speed
	public static float MAC=(0.01f/TD); 	// MaxCap Update speed
	public static float MIC=(0.005f/TD); 	// MinCap Update speed
	public static float MAX_CR=0.01f;		// Maximal Cap regeneration speed
	
	public static float CUC_REGMIN=0.05f;	//			
	public static float MIC_REGMIN=1f;	
	public static float MAC_REGMIN=2f;	
	public static float SPDD_REG=1f; 		// Regeneration rate for sore/pain/damage/desense
	public static float SPDD_PROD=0.25f;	// Production rate for ...		
	
	// TODO: add more static variables for balancing (regeneration speed, etc.)
	
	String name="cavity";
	private float [][]cap;
	private int prehDepth;
	private float sore, topCap, pain, damage, desense;
	private long tSore, tPain, tDam, tDes, tStretch;
	public GameDate internalDate=new GameDate(0, 0, 0);
	public boolean canProlapse=true;
	
	
	public Cavity(int depth, float minC, float maxC, float topC, int prehDepth, String label){
		topCap=topC;
		initCaps(depth,minC,maxC);
		setPain(0);
		setSore(0);
		setDamage(0);
		setDesense(0);
		this.prehDepth=prehDepth;
		name=label;
	}
	
	public Cavity() {
		this(70,0.5f,1.5f,15.0f,50,"Cavity");
	}

	public EssentialPenResponse takePenetration(InsertionAble input, int minutes) {
		Cavity clone=this.clone();
		int topD; 	//max insertion Depth
		if (input.isPrehensile()) {topD=Math.min(input.getLength(),getDepth());}
		else  {topD=Math.min(input.getLength(),getPrehDepth());};
		int t=(int)(minutes*TD); //Timeunites: minutes*Timedivision, actions per Minute...
		int curD=0; //current depth of insertion
		int minD=0; // Tracking initial depth that could be reached
		int maxD=0; // Tracking maximal depth that could be reached
		int dir=1;	//movement direction
		float pl=0;
		boolean setMindepth=false;
		EssentialPenResponse out;
		
		if (input.getHeadDiam()>getMacAt(0)+0.41f){
			t=0; 
		
			out=new EssentialPenResponse(minD, maxD, pl/100, this, clone, input,minutes);
			}
		else {
		for(;t>0&&pain<100+desense;t--){ /*For each Minute*PB */
			if (t%TD==0) updateTimes(1);
			for (int seg=0; seg<curD;seg++){ /* Update caps, lsns, srns, tc. for each segment of the cavity with the InsertAble position */
				int inputSeg=input.getLength()-1-seg;
				float cuc=getCucAt(seg), mac=getMacAt(seg), mic=getMicAt(seg), diam=input.getDiameterAt(inputSeg), nextcuc=getCucAt(seg+1);
				pl+=1;
				sore+=1;
				
				if (cuc<diam){
					if (mic<diam){
						cap[1][seg]=cuc*(1f-CUC/2)+diam*(CUC/2);
						addSore(((diam-cuc))*0.002f);}
					
					else cap[1][seg]=cuc*(1f-CUC)+diam*CUC;
					}
				
				if (mac<diam){
					// overstretching
					pl-=diam-mac; //TODO: what about masochism? :3
					addSore((diam-mac)*2f/TD);
					addPain((diam-mac)*0.02f/TD);
					}
				
				if (cuc>1) cap[1][seg]-=0.005/TD; // regenerate current Capacity
				
				if (0.8f*cuc>mic){
					cap[0][seg]=mic*(1f-MIC)+cuc*0.8f*MIC;}
				
				if (1.05f*cuc>mac){
					cap[2][seg]=mac*(1f-MAC)+1.05f*cuc*MAC;}
				if (seg+1<topD && nextcuc<cuc){
					cap[1][seg+1]=nextcuc*0.97f+cuc*0.03f;}
				regSore(internalDate.getTime()+t/TD);
				
			} 
			if (dir==1&&(curD+1>=topD||input.getHeadDiam()>Math.max(getMacAt(curD),getCucAt(curD))+0.5||curD+1>input.getLength()-1)){
				dir=-1; sore+=5;  if (!setMindepth){minD=curD; setMindepth=true;} // update Direction, 
				maxD=Math.max(maxD, curD);
			}
			/* when moving deeper and the next segment doesen't exest or is too tight pull out*/
			else if (dir==-1&&curD<=1){dir=1; }
			/* when almost pulled out push in again */
			curD+=dir;
			pain+=sore/30;
			pain=Math.max(0,pain-5);
		}
			out=new EssentialPenResponse(minD, maxD, pl/100, this, clone, input,minutes-(t/TD));
			}
		
		System.out.println(out.defDescription);
		return out;
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

	public Cavity clone(){
		int depth=getDepth();
		Cavity clone =new Cavity(this.getDepth(), cap[0][0], cap[2][0], getTopCap(), this.prehDepth, name);
		for (int i=0; i<depth; i++){
				clone.setMicAt(i, cap[0][i]);  
				clone.setCucAt(i, cap[1][i]);
				clone.setMacAt(i, cap[2][i]);  
		} 
		clone.pain=pain;
		clone.sore=sore;
		clone.desense=desense;
		clone.damage=damage;
		
		clone.topCap=topCap;
		clone.name=new String(name);
		clone.canProlapse=canProlapse;
		clone.tDam=tDam;
		clone.tDes=tDes;
		clone.tPain=tPain;
		clone.tSore=tSore;
		clone.tStretch=tStretch;
		
		clone.internalDate=new GameDate(internalDate);
		
		
		
		
		return clone;
	}

	public String getDescription(int min, int max) {
		AlternativeStringHandler output=new AlternativeStringHandler();
		String out;
		switch ((int)(7*getLooseness())){
		case 0: {output.addCategory(new String[]{"tensed"}); break;}
		case 1: {output.addCategory(new String[]{"relaxed"}); break;}
		case 2: {output.addCategory(new String[]{"slightly loosened"}); break;}
		case 3: {output.addCategory(new String[]{"loosed"}); break;}
		case 4: {output.addCategory(new String[]{"pretty loosened","loose"}); break;}
		case 5: {output.addCategory(new String[]{"very loosened up"}); break;}
		default:{output.addCategory(new String[]{"incredibly loose","extremly loose"}); break;}
		}
		
		switch ((int)(7*getGapingness())){
		case 0: {break;}
		case 1: {output.addCategory(new String[]{"slightly opened"}); break;}
		case 2: {output.addCategory(new String[]{"opened"}); break;}
		case 3: {output.addCategory(new String[]{"gaping"}); break;}
		case 4: {output.addCategory(new String[]{"wide open","gaping wide"}); break;}
		case 5: {output.addCategory(new String[]{"incredibly wide gaping","extremly wide opened"}); break;}
		default:{output.addCategory(new String[]{"completly opened"}); break;}
		}
		
		switch ((int)(6*getSoreness())){
		case 0: {break; }
		case 1: {output.addCategory(new String[]{"slightly sore"}); break;}
		case 2: {output.addCategory(new String[]{"sore"}); break;}
		case 3: {output.addCategory(new String[]{"pretty sore","sore"}); break;}
		case 4: {output.addCategory(new String[]{"very sore"}); break;}
		default:{output.addCategory(new String[]{"incredibly sore","extremly sore"}); break;}
		}
		int c=(int)(7*getAbusedness());
		switch (Math.max(0,c)){
		case 0: {break; }
		case 1: {break; }
		case 2: {output.addCategory(new String[]{"slightly abused"}); break;}
		case 3: {output.addCategory(new String[]{"abused","wrecked"}); break;}
		case 4: {output.addCategory(new String[]{"thoroughly abused","thoroughly wrecked"}); break;}
		case 5: {output.addCategory(new String[]{"badly abused","badly wrecked" }); break;}
		case 6: {output.addCategory(new String[]{"horribly abused","horribly wrecked" }); break;}
		default:{output.addCategory(new String[]{"extremly abused","extremly wrecked","incredibly abused","incredibly wrecked"}); break;}
		}
		out = output.getRandomString(min, max);
		if (out!="") out+=" ";
		return out+getLabel();	
	}

	public float getSoreness(){
		return (1f-(10f/(10+sore)));
	}
	
	public float getLooseness() {
		
		float val=0, div=0, tval;
		for (int i=0; i<getDepth();i++){
			tval=((getCucAt(i)+0.1f)/(getMacAt(i)+0.1f));
			tval/=(1+i);
			div+=1f/(1+i);
			val+=tval;
		}
		val/=div;
		val=Math.max(val, 0);
		val=Math.min(val, 1);
		return val;
	}
	
	public float getGapingness(){
		float val=0, div=0, tval;
		for (int i=0; i<getDepth();i++){
			tval=(getCucAt(i)/topCap);
			tval/=(1+i);
			div+=1f/(1+i);
			val+=tval;
		}
		val/=div;
		val=Math.max(val, 0);
		val=Math.min(val, 1);
		return val;
	}
	
	public float getProlapsedness(){
		return getDamagedness()*getGapingness();
	}
	
	public float getDamagedness() {
		return (1f-(10f/(10+damage)));
	}

	public float getAbusedness(){
		float val=(getSoreness()+getLooseness()+getDamagedness())/3;
		return val;
	}

	public float getStretchyness(){
		float val=0, div=0, tval;
		for (int i=0; i<getDepth();i++){
			tval=((getMicAt(i)+getMacAt(i))/(2*topCap));
			tval/=(1+i);
			div+=1f/(1+i);
			val+=tval;
		}
		val/=div;
		val=Math.max(0, val);
		val=Math.min(1, val);
		return val;
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

	public void setTopCap(float topCap) {
		float v=Math.max(0.01f, topCap);
		this.topCap = v;
	}

	public int getDepth() {
		return cap[0].length;
	}

	public int getPrehDepth() {
		return prehDepth;
	}
	public void setPrehDepth(int cm) {
		prehDepth=Math.min(Math.max(2,cm),cap[0].length+1);	
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

	public float getMicAt(int cm) {
		if (cm<cap[0].length) return cap[0][cm];
		else return -1;
	}
	public void setMicAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[0][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setMinCapAt() out of Bounds");
		//TODO: better Exception handling
	}
	public void addMicAt(int cm, float w) {
		setMicAt(cm, getMicAt(cm)+w);
	}

	public float getCucAt(int cm) {
		if (cm<cap[1].length) return cap[1][cm];
		else return -1;
	}
	public void setCucAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[1][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setCurCapAt() out of Bounds");
		//TODO: better Exception handling
	}
	public void addCucAt(int cm, float w) {
		setCucAt(cm,getCucAt(cm)+w);
	}
	
	public float getMacAt(int cm) {
		if (cm<cap[2].length) return cap[2][cm];
		else return -1;
	}
	public void setMacAt(int cm, float w) {
		if (cm <getDepth()&&cm>=0){ this.cap[2][cm] = Math.min(Math.max(w, 0), topCap); }
		else System.out.println("Warning:setCurCapAt() out of Bounds");
		//TODO: better Exception handling
	}
	public void addMacAt(int cm, float w) {
		setMacAt(cm,getMacAt(cm)+w);
	}

	
	
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



	public float getSore() {
		return sore;
	}
	private void setSore(float f) {
		float v=Math.max(0, f);
		this.sore = v;
	}	
	
	private void addSore(float f) {
		if (f>0) {
			tSore=0; 
			if (sore>100) {
				addPain(0.1f*f);
			}
		}
		setSore(sore+f);
	}
	private void regSore(long addminutes) {
		long dt=internalDate.getTime()+addminutes-tSore;
		addSore(-SPDD_REG*(1+dt)/8);
	}
	
	public float getDesense() {
		return desense;
	}
	private void setDesense(float f) {
		this.desense = Math.max(f,0);
	}
	private void addDesense(float f) {
		if (f>0) tDes=0;
		setDesense(desense+(f/(1+desense)));
	}
	
	private void regDesense(long addminutes){
		long dt=internalDate.getTime()+addminutes-tSore;
		addDesense(-SPDD_REG*(1+dt)/40);
	}
	
	public float getDamage() {
		return damage;
	}
	private void setDamage(float f) {
		this.damage = Math.max(f,0);
	}
	private void addDamage(float f) {
		if(f>0) { tDam=0;}
		setDamage(damage+f);
	}
	private void regDamage(long addminutes) {
		long dt=internalDate.getTime()+addminutes-tDam;
		addDamage(-SPDD_REG*(1+dt)/32);
	}
	
	public float getPain() {
		return pain;
	}
	private void setPain(float pain) {
		this.pain = Math.max(0,pain);
	}
	private void addPain(float f) {
		float val=f+sore/100+damage/100;
		if (f>0) {
			tPain=0;
			if (pain>60){
				addDamage(f*0.5f);
				addDesense(f*0.5f);}
		}
		setPain(pain+val);		
	}
	private void regPain(long addminutes){
		long dt=internalDate.getTime()+addminutes-tPain;
		addPain(-SPDD_REG*(1+dt)/4);
	}

	private void regAll(long addminutes){
		regSore(addminutes);
		regPain(addminutes);
		regDamage(addminutes);
		regDesense(addminutes);
	}
	
	public void rest(int minutes) {
		for (int seg=0; seg<getDepth();seg++){
			addCucAt(seg, -MAX_CR);
			addMicAt(seg, -MAX_CR);
			addMacAt(seg, -MAX_CR);
			if (1.05f*getCucAt(seg)>getMacAt(seg)){setMacAt(seg,getMacAt(seg)*0.995f+getCucAt(seg)*0.005f);}
		}
		for (;minutes>0;minutes--){regAll(1);updateTimes(1); }
	}

	private void updateTimes(int i) {
		tSore+=i; 
		tPain+=i; 
		tDam+=i; 
		tDes+=i; 
		tStretch+=i; 
		internalDate.addMinutes(i);
	}
	
}

