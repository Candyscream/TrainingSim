package cavity_old;

import java.util.Date;

import Controller.GameDate;

public class CState {
	private GameDate date;
	private CavityV1 cavity;
	
	CState(GameDate curDate, CavityV1 cavity){ this.date=curDate.clone(); this.cavity=(CavityV1) cavity.clone(); }
	public GameDate getDate(){return date;}
	public CavityV1 getCavity(){return cavity;};

}