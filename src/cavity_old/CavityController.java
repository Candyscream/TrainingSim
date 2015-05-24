package cavity_old;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Controller.GameDate;
import Game.*;
import Person.*;

public class CavityController {
	
private CavityGUI gui;
private ArrayList<CState> state = new ArrayList<CState>();
private GameDate curDate;
private CavityV1 curCav;

CavityController() { 
	curDate=new GameDate(1,12,0);
	System.out.println(curDate.toString());

	curCav=new CavityV1();
	state.add(new CState(curDate,curCav));
	gui= new CavityGUI(this);
}

public static void main (String[] args){
	CavityController s = new CavityController();
	Frame F=new Frame();
    F.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent we){System.exit(0);}});
    F.add(s.gui);
    F.pack();
    F.setVisible(true);
	}

public ArrayList<CState> getState() {
	return state;
}

public void simpleInsertAction(int time,int length,float diam){
	
	
	float[] insObj= new float[length];
	for (int i=0; i<length; i++){insObj[i]=diam;}
	curDate.setTime(curDate.getTime()+(time-curCav.takeInsertion(time, insObj)));
	
	
	state.add(new CState(curDate,curCav));
	
	gui.updateAll();
}

public void restAction(int time){
	curDate.setTime(curDate.getTime()+time);
	
	curCav.rest(time);
	
	state.add(new CState(curDate,curCav));
	
	gui.updateAll();
}




public CavityV1 getCurCav() {
	
	return curCav;
}



}
