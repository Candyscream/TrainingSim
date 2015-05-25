package cavity2;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;




public class RidePanel extends JPanel {
	JButton ride =			new JButton("Ride");
	JSpinner lengthSpinner= new JSpinner();
	JLabel label1 = 		new JLabel(" cm long and ");
	JSpinner widthSpinner= 	new JSpinner();
	JLabel label2 = 		new JLabel(" cm width toy for ");
	JComboBox timeBox=		new JComboBox();
	JLabel label3 = 		new JLabel(" minutes ");
	CavityGUI gui;
	
	public RidePanel(CavityGUI gui){
		this.gui=gui;
		setLayout(new FlowLayout(0,2,2));
		

		
		add(ride);
	
		add(lengthSpinner);
		lengthSpinner.setPreferredSize(new Dimension(50,20));
		lengthSpinner.setMinimumSize(new Dimension(30,20));
		SpinnerModel lSpinnerModel =
		        new SpinnerNumberModel(40, 5, 60, 1);
		lengthSpinner.setModel(lSpinnerModel);
		add(label1);
		
		add(widthSpinner);
		widthSpinner.setPreferredSize(new Dimension(50,20));
		widthSpinner.setMinimumSize(new Dimension(30,20));
		SpinnerModel wSpinnerModel =
		        new SpinnerNumberModel(2, 1, 14, 0.10);
		widthSpinner.setModel(wSpinnerModel);
		add(label2);
		
		add(timeBox);
		timeBox.setModel(new DefaultComboBoxModel(new String[]{"5 min","10 min","15 min","20 min","30 min","45 min","1,0h","1,5h", "2,0h", "3,0h"}));
		timeBox.setSelectedIndex(4);
		timeBox.setPrototypeDisplayValue("10 min");
		
		add(label3);
		
	    class rideButtonListeneer  implements ActionListener {
	    	CavityGUI gui;
	    	
	    	public rideButtonListeneer(CavityGUI gui){
	    		this.gui=gui;
	    	}
	    	
	      public void actionPerformed(ActionEvent e){
	    	 int t[]={5,10,15,20,30,45,60,90,120,180};
	    	 timeBox.updateUI();
	    	 this.gui.simpleInsertAction(t[timeBox.getSelectedIndex()], ((Integer)lengthSpinner.getValue()).intValue(), (float)((Double)widthSpinner.getValue()).floatValue() );
	      }
	    }
	    
	    ride.addActionListener(new rideButtonListeneer(gui));
		
	}
	
	
}
