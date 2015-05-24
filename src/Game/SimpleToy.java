package Game;

public class SimpleToy implements InsertionAble{
	String name;
	float diam[];
	boolean preh;
	
	public SimpleToy(String name, float[] diam, boolean preh){
		this.name=name;
		this.diam=diam;
		this.preh=preh;
	}

	public float getDiameterAt(int cm) {
		if (cm<0) {
			System.out.println("Warning: negative Index requested from SimpleToy"); return 0; 
			}
		if (cm>=diam.length) {
			System.out.println("Warning: Index requested from SimpleToy out of bounds"); return 0; 
			}
		return diam[cm];
	}

	public int getLength() {
		return diam.length;
	}

	public boolean isPrehensile() {
		return preh;
	}

	public String getLabel() {
		return name;
	}

	public float getHeadDiam() {
		return diam[diam.length-1];
	}

}
