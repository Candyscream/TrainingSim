package cavity_old;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class RestPanel extends JPanel {
	CavityGUI gui;
	JButton rest		=new JButton("Rest");
	JLabel label1  		=new JLabel(" for ");
	JComboBox timeBox	=new JComboBox();

	public RestPanel(CavityGUI gui){
		this.gui=gui;
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 4;  
		c.weightx = 0.0;
		c.gridx=0;
		add(rest,c);
		
		c.gridx=1;
		add(label1,c);
	
		c.gridx=2;
		timeBox.setPrototypeDisplayValue("10 min");
		timeBox.setModel(new DefaultComboBoxModel(new String[]{"5 min","10 min","15 min","20 min","30 min","45 min","1,0h","1,5h", "2,0h", "3,0h"}));
		timeBox.setSelectedIndex(4);
		add(timeBox,c);
		c.gridx=3;
		c.weightx = 1.0;
		add(new JPanel(),c);
		
	    class restButtonListener  implements ActionListener {
	    	CavityGUI gui;
	    	public restButtonListener(CavityGUI gui){
	    		this.gui=gui;
	    	}
	    	
	      public void actionPerformed(ActionEvent e){
	    	 int t[]={5,10,15,20,30,45,60,90,120,180};
	    	 timeBox.updateUI();
	    	 this.gui.restAction(t[timeBox.getSelectedIndex()]);
	      }
	    }
	    
	    rest.addActionListener(new restButtonListener(gui));
	}
}
