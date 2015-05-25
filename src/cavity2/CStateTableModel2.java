package cavity2;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


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
		
		columns.clear();
		int cavDepth=cStates.get(0).getCavity().getDepth();
		String[] col = new String[9+cavDepth];
		col[0]="Soreness";
		col[1]="Pain";
		col[2]="Looseness";
		col[3]="Strechedness";
		col[4]="Stretchyness";
		col[5]="Abusedness";
		col[6]="Prolapsedness";
		col[7]="Gapingness";
		col[8]="Opening";
		for (int i=1; i<cavDepth;i++){
			col[i+8]=i+" cm";
		}
		columns.add(col);
		
		for (int i=0; i<cStates.size() && i<4; i++){
			Cavity cav = cStates.get(cStates.size()-1-i).getCavity();
			col = new String[9+cavDepth];
			col[0]=""+format.format(cav.getSoreness());
			col[1]=""+format.format(cav.getPain());
			col[2]=""+format.format(cav.getLooseness());
			col[3]=""+format.format(cav.getStretchedness());
			col[4]=""+format.format(cav.getStretchyness());
			col[5]=""+format.format(cav.getAbusedness());
			col[6]=""+format.format(cav.getProlapsedness());
			col[7]=""+format.format(cav.getGapingness());
	
			ArrayList<String> caps=cav.printCap();
			for (int j=0; j<cavDepth; j++){
				col[j+8]=caps.get(j);
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
