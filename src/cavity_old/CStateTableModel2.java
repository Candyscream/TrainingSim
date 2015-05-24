package cavity_old;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class CStateTableModel2 extends AbstractTableModel{
	ArrayList<String[]> Columns;
	ArrayList<CState> cStates;

	public int getColumnCount() {
		return Columns.size();
	}

	public int getRowCount() {
		return Columns.get(0).length;
	}

	public Object getValueAt(int row, int col) {
		return Columns.get(col)[row];
	}
	
	public CStateTableModel2(ArrayList<CState> states){
		this.cStates=states;
		Columns= new ArrayList<String[]>();
		recalculateColumns();
		
	}
	
	@Override
	public String getColumnName(int index) {
	   if (index==0) return "Date";
	   else return cStates.get(cStates.size()-index).getDate().toString();
	}
	
	public void recalculateColumns(){
		
		Columns.clear();
		int cavDepth=cStates.get(0).getCavity().getMinCap().length;
		String[] col = new String[3+cavDepth];
		col[0]="Looseness";
		col[1]="Soreness";
		col[2]="Pain";
		col[3]="Opening";
		for (int i=1; i<cavDepth;i++){
			col[i+3]=i+" cm";
		}
		Columns.add(col);
		
		for (int i=0; i<cStates.size() && i<4; i++){
			CavityV1 cav = cStates.get(cStates.size()-1-i).getCavity();
			col = new String[3+cavDepth];
			col[0]=""+cav.getLooseness();
			col[1]=""+cav.getSoreness();
			col[2]=""+cav.getPain();
			ArrayList<String> caps=cav.printCap();
			for (int j=0; j<cavDepth; j++){
				col[j+3]=caps.get(j);
			}
			Columns.add(col);
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
