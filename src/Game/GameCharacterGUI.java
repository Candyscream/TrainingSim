package Game;

import javax.swing.*;

import Person.Person;


public class GameCharacterGUI extends JPanel{
	JLabel energy=new JLabel("Energy");
	JLabel energyVal=new JLabel();
	JLabel pain=new JLabel("pain");
	JLabel painVal=new JLabel();
	JLabel lust=new JLabel("lust");
	JLabel lustVal=new JLabel();
	JLabel pleasure=new JLabel("pleasure");
	JLabel pleasureVal=new JLabel();	
	Person gameCharacter;
	
	GameCharacterGUI(){
		
		
	}
	
	void updateLabels(){
		energyVal.setText(""+gameCharacter.getEnergy());
		painVal.setText(""+gameCharacter.getPain());
		lustVal.setText(""+gameCharacter.getLust());
		pleasureVal.setText(""+gameCharacter.getPleasure());
	}
	
	void setCharacter(Person gameCharacter){this.gameCharacter=gameCharacter;}
	
	
	
}
