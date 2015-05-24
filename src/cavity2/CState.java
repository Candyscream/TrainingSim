package cavity2;

import java.util.Date;

import Controller.GameDate;

public class CState {
	private GameDate date;
	private Cavity cavity;
	
	CState(GameDate curDate, Cavity cavity){ this.date=curDate.clone(); this.cavity=cavity.clone(); }
	public GameDate getDate(){return date;}
	public Cavity getCavity(){return cavity;};

}