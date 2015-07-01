package cavity2;

public interface PenResp {
	public String getDescription();
	public double getPainDiff();
	public double getSoreDiff();
	public Cavity getNewCavity();
	public Cavity getOldCavity();
        public int getTime();
}
