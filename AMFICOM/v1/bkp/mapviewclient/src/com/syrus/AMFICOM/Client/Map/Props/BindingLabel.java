package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BindingLabel extends JLabel implements TableCellRenderer 
{
	ObjectResource or;
	
	{
		setOpaque(true);
	}

	public void bind(ObjectResource or)
	{
		this.or = or;
		if(or == null)
			setText("");
		else
			setText(or.getName());
	}
	
	public void unbind()
	{
		this.or = null;
		setText("");
	}

	public Component getTableCellRendererComponent(
			JTable table, 
			Object value, 
			boolean isSelected, 
			boolean hasFocus, 
			int row, 
			int column)
	{
		ObjectResource or = (ObjectResource )value;
		BindingTableModel model = (BindingTableModel )table.getModel();
		if(or == null)
			setText("");
		else
			setText(or.getName());
		Point ac = model.getActiveCoordinates();
		if(ac != null
			&& ac.x == column
			&& ac.y == row)
		{
			setBackground(Color.PINK);
		}
		else
		{
			setBackground(SystemColor.window);
		}
		return this;
	}
}
