package cavity2;

import Game.InsertionAble;

public class GeneralPenResponse implements PenResp{
	// returned by cavity to allow the scene or character to generate a Message after a penetration action
	String defDescription;		// default description/summary of the penetration action
	int startDepth, endDepth;	// how deep the penetrating object was able to bbe inserted initially/at the end
	float pleasure;				// unmodified pleasure produced by the action
	int time=0;
	Cavity old;			// copy of the ol cavity state, for comparsion (looseness, )	
	Cavity cur;	
	InsertionAble insertedObject;
	static public final int COMPLETE=0;
        static public final int OVERSIZED=1;
        static public final int PAIN_TOO_HIGH=2;
        static public final int PAIN_WENT_TOO_HIGH=3;
        
        
	
	public GeneralPenResponse(int sDepth, int eDepth, float pl, Cavity newCav, Cavity oldCav, InsertionAble in, int time, int reason){
		this.time=time;
		startDepth=sDepth;
		endDepth=eDepth;
		pleasure=pl;
		old=oldCav;
		cur=newCav;
		insertedObject=in;
		if (reason == OVERSIZED){defDescription="The "+oldCav.getDescription(0,2)+" couldn't be penetrated by the "+insertedObject.getLabel()+", because it's just too big.\n";}
		else if (reason == PAIN_TOO_HIGH) {defDescription="The "+oldCav.getDescription(0,2)+" couldn't be penetrated by the "+insertedObject.getLabel()+", because it still is in too much pain.\n";}
		else {if (startDepth==endDepth)	{defDescription="The "+insertedObject.getLabel()+" penetrated the "+oldCav.getDescription(0,2)+" "+endDepth+" cm deep.\n";}
		else							{defDescription="Initially the "+insertedObject.getLabel()+" could be inserted "+startDepth+" cm deep into the "+oldCav.getDescription(0,2)+". After a while it could be inserted "+endDepth+" cm deep.\n";};
		defDescription+="You tried for "+time+" Minutes";
		if(reason == PAIN_WENT_TOO_HIGH){defDescription+=" but stopped when it was getting too painful";}
		defDescription+=".\nYou dealt yourself "+String.format("%1.1f",(cur.getPain()-oldCav.getPain()))+" Pain, "+String.format("%1.1f",(cur.getSore()-oldCav.getSore()))+" Soreness and "+String.format("%1.1f",(pl))+" pleasure.\n";
		
		}
	}

	public String getDescription() {
		return defDescription;
	}

	public double getPainDiff() {
		// TODO Auto-generated method stub
		return cur.getPain()-old.getPain();
	}

	public double getSoreDiff() {
		// TODO Auto-generated method stub
		return cur.getSore()-old.getSore();
	}

	public Cavity getNewCavity() {
		return cur;
	}

	public Cavity getOldCavity() {
		// TODO Auto-generated method stub
		return old;
	}

   
        public int getTime() {
            return time;
        }
}
