package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTableModel;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ObjectResourcePropertyTableRenderer extends DefaultTableCellRenderer
{
	ObjectResourcePropertyTableModel tableModel;

	public ObjectResourcePropertyTableRenderer()
	{
		super();
	}
	
	public ObjectResourcePropertyTableRenderer(ObjectResourcePropertyTableModel mytableModel)
	{
		super();
		this.tableModel = mytableModel;
	}

	public void setModel(ObjectResourcePropertyTableModel mytableModel)
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
		String text = "";
		column = table.convertColumnIndexToModel(column);
		if(column == 0)
		{
//			text = tableModel.getDisplayModel().getPropertyName((String)value);
			text = tableModel.getDisplayModel().getColumnName((String)value);
//			System.out.println("render " + (String )value + " as " + text);
			return super.getTableCellRendererComponent(
				table, text, isSelected, hasFocus, row, column);
		}
		else
		{
//			Component rend = (Component)tableModel.getDisplayModel().getPropertyRenderer(null, (String)value);
			Component rend = (Component)tableModel.getDisplayModel().getColumnRenderer(null, (String)value);

			if(rend == null)
			{
				text = tableModel.getContents().getModel().getPropertyValue((String)value);
				rend = new TextFieldEditor(text);
			}
			
			if(isSelected)
			{
				rend.setForeground(table.getSelectionForeground());
				rend.setBackground(table.getSelectionBackground());
			}
/*
			else
			{
				rend.setForeground(table.getForeground());
				rend.setBackground(table.getBackground());
			}
*/
//System.out.println("render " + (String )value + " with " + rend);
			return rend;

			//super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
		}
	}
}
