package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class ObjectResourceTableRenderer extends DefaultTableCellRenderer
{
	ObjectResourceTableModel tableModel;

	public ObjectResourceTableRenderer()
	{
		super();
	}
	
	public ObjectResourceTableRenderer(ObjectResourceTableModel mytableModel)
	{
		super();
		this.tableModel = mytableModel;
	}

	public void setModel(ObjectResourceTableModel mytableModel)
	{
		this.tableModel = mytableModel;
	}

	public Component getTableCellRendererComponent(
			JTable table,
            Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column)
	{
		ObjectResource or = (ObjectResource)value;
//		ObjectResource or = (ObjectResource )tableModel.getValueAt(row, column);
		ObjectResourceModel mod = or.getModel();

		column = table.convertColumnIndexToModel(column);
		
		String col_id = tableModel.getColumnByNumber(column);
		String text = mod.getColumnValue(col_id);

		Component component;

		component = (Component)tableModel.getDisplayModel().getColumnRenderer(or, col_id);
		if(component == null)
			component = new JLabelRenderer(text);
/*
			component = super.getTableCellRendererComponent(
					table,
					text,
					isSelected,
					hasFocus,
					row,
					column);
*/					
//		System.out.println("renderer for " + text);
		if (!isSelected)
		{
//				System.out.println("	(not selected)");
			if(tableModel.getDisplayModel().isColumnColored(col_id))
			{
//				System.out.println("		(colored)");
				component.setForeground(table.getForeground());
				component.setBackground(tableModel.getDisplayModel().getColumnColor(or, col_id));
			}
			else
			{
				component.setForeground(table.getForeground());
				component.setBackground(table.getBackground());
			}
		}
		else //		if (isSelected) 
		{
//				System.out.println("	(selected)");
		   component.setForeground(table.getSelectionForeground());
		   component.setBackground(table.getSelectionBackground());
		}
//		System.out.println("render " + text + " with fgcolor " + component.getForeground() + " with bkcolor " + component.getBackground());
		

		if (hasFocus) 
		{
			setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
/*
			if (table.isCellEditable(row, column)) 
			{
				component.setForeground( UIManager.getColor("Table.focusCellForeground") );
				component.setBackground( UIManager.getColor("Table.focusCellBackground") );
			}
*/
		} 
		else 
		{
			setBorder(noFocusBorder);
		}

/*
			component.setBackground(super.getTableCellRendererComponent(
					table,
					text,
					isSelected,
					hasFocus,
					row,
					column).getBackground());
*/		
		int th = table.getRowHeight(row);
		if(th < component.getHeight())
			table.setRowHeight(row, component.getHeight());
		return component;
	}
}
