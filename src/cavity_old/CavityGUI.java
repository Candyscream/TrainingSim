package cavity_old;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;




public class CavityGUI extends JPanel {
	CavityController controller;
	JLabel text 	=new JLabel("Anal Training Sim");
	JTable statTable;
	JScrollPane statTableScrollPane;
	CStateTableModel2 tableModel;
	StatPanel statPanel;
	
public CavityGUI(CavityController ctr){
	super(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	controller=ctr;
	
	// Table
	tableModel =new CStateTableModel2(ctr.getState());
	statTable = new JTable(tableModel);
	statTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	statTable.setRowSelectionAllowed(true);
	statTable.setColumnSelectionAllowed(false);
	statTableScrollPane = new JScrollPane(statTable);
	statTableScrollPane.setPreferredSize(new Dimension(1000,400));
	
	tableModel.initColumnSizes(statTable);
  
	
	c.weightx = 1.0;
	c.fill = GridBagConstraints.VERTICAL;
	c.ipady = 10;  
	c.weighty = 0;
	c.weightx = 1.0;
	c.gridx=0;
	c.gridy=0;
	c.gridwidth=1;
	c.gridheight=1;
	add(text,c);
	
	c.weighty = 1.0;

	//c.ipady = 200;  
	c.gridx=0;
	c.gridy=1;
	c.gridwidth=1;
	c.gridheight=1;
	add(statTableScrollPane,c);
	
	c.gridy=2;
	statPanel=new StatPanel(""+controller.getCurCav().getSoreness(), ""+controller.getCurCav().getPain(),""+ controller.getCurCav().getLooseness());
	add(statPanel,c);
	
	c.weighty = 0;
	c.ipady = 10;  
	c.gridx=0;
	c.gridy=3;
	c.gridwidth=1;
	c.gridheight=1;
	add(new RidePanel(this),c);
	c.gridy=4;
	add(new RestPanel(this),c);

	
	
}

public void restAction(int time) {
	controller.restAction(time);
}
public void simpleInsertAction(int time, int length, float diam) {
	//System.out.println("Ride klicked with "+time+" minutes, "+ i+"cm length and "+f+" cm diameter selected." );
	controller.simpleInsertAction(time,length,diam);
}

public void updateAll() {
	CavityV1 curCav= controller.getCurCav();
	tableModel.setStates(controller.getState());
	tableModel.recalculateColumns();
	tableModel.initColumnSizes(statTable);
	statPanel.setLabels(""+curCav.getSoreness(),""+curCav.getPain(), ""+curCav.getLooseness());
	tableModel.fireTableStructureChanged();


	
}






}
