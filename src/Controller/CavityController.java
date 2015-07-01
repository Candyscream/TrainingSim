package Controller;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JComboBox;

import Controller.GameDate;
import Game.*;
import Person.*;
import cavity2.CState;
import cavity2.Cavity;
import cavity2.GeneralPenResponse;
import trainingsimgui.CavityTrainingSimGUI;

public class CavityController {
	
private CavityTrainingSimGUI gui;
private ArrayList<CState> state = new ArrayList<CState>();
private GameDate curDate;
private Cavity curCav;

CavityController() { 
	curDate=new GameDate(1,12,0);
	System.out.println(curDate.toString());
	curCav=new Cavity();
	state.add(new CState(curDate,curCav));
	gui= new CavityTrainingSimGUI(this);
}



public static void main (String[] args){
	CavityController s = new CavityController();
	Frame F=s.gui;
        F.setVisible(true);
	}

public ArrayList<CState> getState() {
	return state;
}

public void simpleInsertAction(int time,int length,float diam,float painlimit){
	float[] ins= new float[length];
	for (int i=0; i<length; i++){ins[i]=diam;}
	
	SimpleToy insObj=new SimpleToy("Toy",ins,false);
	GeneralPenResponse re = curCav.takePenetration(insObj,time,painlimit);
	curDate.setTime(curDate.getTime()+(time-re.getTime()));
	
	state.add(new CState(curDate,curCav));
        gui.setLogAreaText(gui.getLogAreaText()+"\n"+re.getDescription());
}

public void restAction(int time){
	curDate.setTime(curDate.getTime()+time);
	
	curCav.rest(time);
	
	state.add(new CState(curDate,curCav));
	
	//gui.updateAll();
}

public Cavity getCurCav() {
	
	return curCav;
}

    public GameDate getDate() {
       return curDate;
    }



}
