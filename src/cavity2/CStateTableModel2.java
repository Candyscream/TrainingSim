package cavity2;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Controller.GameDate;


public class CStateTableModel2 extends AbstractTableModel{
	ArrayList<String[]> columns;
	ArrayList<CState> cStates;
	DecimalFormat format = new DecimalFormat("0.00"); 

	public int getColumnCount() {
		return columns.size();
	}

	public int getRowCount() {
		return columns.get(0).length;
	}

	public Object getValueAt(int row, int col) {
		return columns.get(col)[row];
	}
	
	public CStateTableModel2(ArrayList<CState> states){
		this.cStates=states;
		columns= new ArrayList<String[]>();
		recalculateColumns();
		
	}
	
	@Override
	public String getColumnName(int index) {
	   if (index==0) return "Date";
	   else return cStates.get(cStates.size()-index).getDate().toString();
	}
	
	public void recalculateColumns(){
		int C=12;
		columns.clear();
		int cavDepth=cStates.get(0).getCavity().getDepth();
		String[] col = new String[C+1+cavDepth];
		col[0]="datecheck";
		col[1]="vSore";
		col[2]="vPain";
		col[3]="vDesense";
		col[4]="vDamage";
		
		col[5]="Looseness";
		col[6]="Soreness";
		col[7]="Stretchyness";
		col[8]="Abusedness";
		col[9]="Prolapsedness";
		col[10]="Gapingness";
		col[11]="Damagedness";
		col[12]="Opening";
		
		for (int i=1; i<cavDepth;i++){
			col[i+C]=i+" cm";
		}
		columns.add(col);
		
		for (int i=0; i<cStates.size() && i<4; i++){
			Cavity cav = cStates.get(cStates.size()-1-i).getCavity();
			col = new String[C+1+cavDepth];
			col[0]=""+new GameDate(cav.internalDate.getDay()+1,cav.internalDate.getHour()+12,cav.internalDate.getMinute()).toString();
			//col[0]=""+cav.internalDate.toString();
			col[1]=""+format.format(cav.getSore());
			col[2]=""+format.format(cav.getPain());
			col[3]=""+format.format(cav.getDamage());
			col[4]=""+format.format(cav.getDesense());
			
			col[5]=""+format.format(100*cav.getLooseness());
			col[6]=""+format.format(100*cav.getSoreness());
			col[7]=""+format.format(100*cav.getStretchyness());
			col[8]=""+format.format(100*cav.getAbusedness());
			col[9]=""+format.format(100*cav.getProlapsedness());
			col[10]=""+format.format(100*cav.getGapingness());
			col[11]=""+format.format(100*cav.getDamagedness());
	
			ArrayList<String> caps=cav.printCap();
			for (int j=0; j<cavDepth; j++){
				col[j+C]=caps.get(j);
			}
			columns.add(col);
		}
	}
	
public void initColumnSizes(JTable table) {
	    TableColumn column = null;
	    Component comp = null;
	    int headerWidth = 50;
	    int cellWidth = 50;
	    TableCellRenderer headerRenderer =
	        table.getTableHeader().getDefaultRenderer();

	    for (int i = 0; i < getColumnCount(); i++) {
	    	column = new TableColumn();
	    	if (i==0)column.setHeaderValue("Date");
	    	else {column.setHeaderValue(cStates.get(i-1).getDate().toString());}
	    	
	        comp = headerRenderer.getTableCellRendererComponent(
	                             null, column.getHeaderValue(),
	                             false, false, 0, 0);
	        headerWidth = comp.getPreferredSize().width;

	        comp = table.getDefaultRenderer(getColumnClass(i)).
	                         getTableCellRendererComponent(
	                             table, getValueAt(0, i),
	                             false, false, 0, i);
	        cellWidth = comp.getPreferredSize().width;

	        column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	    }
	}
public void setStates(ArrayList<CState> states) {
	this.cStates=states;
};

}
