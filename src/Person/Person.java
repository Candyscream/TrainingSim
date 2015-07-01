package Person;

import cavity2.*;

public class Person{
// sleep = 0: character falls asleep
// on orgasm pleasure is converted to satisfaction
// high satisfaction: fast decreasend and slowly incresing lust
// low  satisfaction: fast increasing and slowly decreasing lust
// the higher a characters libido the faster their satisfaction will reduce
private int energy, lust, pleasure, pain, satisfaction;
public String they,their,theirs,themself, name;
public Cavity anus;

public Person(String name, String[] pronouns){
	energy=100;
	lust=0;
	pleasure=0;
	pain=0;
	setSatisfaction(0);
}

public int getEnergy() {
	return energy;
}

public void setEnergy(int energy) {
	this.energy = Math.max(0,energy);
}

public int getLust() {
	return lust;
}

public void setLust(int lust) {
	this.lust = Math.max(0,lust);
}

public int getPleasure() {
	return pleasure;
}

public void setPleasure(int pleasure) {
	this.pleasure = Math.max(0,pleasure);
}

public int getPain() {
	return pain;
}

public void setPain(int pain) {
	this.pain = Math.max(0,pain);
}

public int getSatisfaction() {
	return satisfaction;
}

public void setSatisfaction(int satisfaction) {
	this.satisfaction = satisfaction;
}








}
