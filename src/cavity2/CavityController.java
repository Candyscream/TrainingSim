package cavity2;

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
private Cavity curCav;

CavityController() { 
	curDate=new GameDate(1,12,0);
	System.out.println(curDate.toString());

	curCav=new Cavity();
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
	float[] ins= new float[length];
	for (int i=0; i<length; i++){ins[i]=diam;}
	
	SimpleToy insObj=new SimpleToy("Toy",ins,false);
	EssentialPenResponse re = curCav.takePenetration(insObj,time);
	curDate.setTime(curDate.getTime()+(time-re.time));
	
	state.add(new CState(curDate,curCav));
	gui.output.setText(gui.output.getText()+"\n"+re.defDescription);
	gui.updateAll();
}

public void restAction(int time){
	curDate.setTime(curDate.getTime()+time);
	
	curCav.rest(time);
	
	state.add(new CState(curDate,curCav));
	
	gui.updateAll();
}

public Cavity getCurCav() {
	
	return curCav;
}



}
