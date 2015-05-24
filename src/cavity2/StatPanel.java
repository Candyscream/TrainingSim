package cavity2;

import java.awt.FlowLayout;

import javax.swing.*;

public class StatPanel extends JPanel {
	JLabel sorenessLabel;
	JLabel painLabel;
	JLabel loosenessLabel;
	
public	StatPanel(String s, String p, String l){
	super();
	setLayout(new FlowLayout(0,20,2));
	sorenessLabel=new JLabel();
	painLabel=new JLabel();
	loosenessLabel=new JLabel();
	setLabels(s,p,l);
	add(sorenessLabel);
	add(painLabel);
	add(loosenessLabel);
	
}
public void setLabels(String soreness, String pain, String looseness){
	this.sorenessLabel.setText("soreness: "+soreness);
	this.painLabel.setText("pain: "+pain);
	this.loosenessLabel.setText("loosenes "+looseness);
}
}
