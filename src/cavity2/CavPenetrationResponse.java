package cavity2;

import Game.InsertionAble;

public class CavPenetrationResponse {
	// returned by cavity to allow the scene or character to generate a Message after a penetration action
	String defDescription;		// default description/summary of the penetration action
	int startDepth, endDepth;	// how deep the penetrating object was able to bbe inserted initially/at the end
	float pleasure;				// unmodified pleasure produced by the action
	float oldPain;				// old pain level
	int time=0;
	Cavity oldState;			// copy of the ol cavity state, for comparsion (looseness, )	
	InsertionAble insertedObject;
	//TODO: Write constructor
	
	CavPenetrationResponse(int sDepth, int eDepth, float pl, Cavity cur, Cavity old, InsertionAble in, int time ){
		this.time=time;
		startDepth=sDepth;
		endDepth=eDepth;
		pleasure=pl;
		oldState=old;
		insertedObject=in;
		if (endDepth==0&&cur.getPain()<100)		{defDescription="The "+oldState.getDescription(0,2)+" couldn't be penetrated by the "+insertedObject.getLabel()+", because it's just too big.\n";}
		else if (endDepth==0&&cur.getPain()>=100) {defDescription="The "+oldState.getDescription(0,2)+" couldn't be penetrated by the "+insertedObject.getLabel()+", because it still is in too much pain.\n";}
		else {if (startDepth==endDepth)	{defDescription="The "+insertedObject.getLabel()+" penetrated the "+oldState.getDescription(0,2)+" "+endDepth+" cm deep.\n";}
		else							{defDescription="Initially the "+insertedObject.getLabel()+" could be inserted "+startDepth+" cm deep into the "+oldState.getDescription(0,2)+". After a while it could be inserted "+endDepth+" cm deep.\n";};
		defDescription+="You tried for "+time+" Minutes";
		if(cur.getPain()>=100){defDescription+=" but stopped when it was getting too painful";  };
		defDescription+=".\nYou dealt yourself "+(cur.getPain()-old.getPain())+" Pain, "+(cur.getSoreness()-old.getSoreness())+" Soreness and "+pl+" pleasure.\n";
		
		}
	}
}
