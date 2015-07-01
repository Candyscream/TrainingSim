package cavity2;

import Game.InsertionAble;
import Person.Person;

public class PersPenResp implements PenResp {
	Person person;
        String defDescription;		// default description/summary of the penetration action
	int startDepth, endDepth;	// how deep the penetrating object was able to bbe inserted initially/at the end
	float pleasure;				// unmodified pleasure produced by the action
	int time=0;
	Cavity old;			// copy of the ol cavity state, for comparsion (looseness, )	
	Cavity cur;	
	InsertionAble insertedObject;
	//TODO: Write constructor
        
        
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
        
	public String getDescription() {
		return defDescription;
	}
}
