package cavity2;
import Controller.GameDate;

public class CState {
    private GameDate date;
    private Cavity cavity;

    public void setDate(GameDate date) {
        this.date = date;
    }

    public void setCavity(Cavity cavity) {
        this.cavity = cavity;
    }
    	
    public CState(GameDate curDate, Cavity cavity){ this.date=curDate.clone(); this.cavity=cavity.clone(); }
    public GameDate getDate(){return date;}
    public Cavity getCavity(){return cavity;};

}