package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkBinding;

import java.awt.Point;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class BindingTableModel extends AbstractTableModel implements TableModel 
{
	MapPhysicalLinkBinding binding;
	
	Point activeCoordinates = null;

	public BindingTableModel(MapPhysicalLinkBinding binding)
	{
		this.binding = binding;
	}
	
	public void setActiveElement(ObjectResource or)
	{
		activeCoordinates = binding.getBinding(or);
	}
	
	public ObjectResource getActiveElement()
	{
		return binding.getBound(activeCoordinates.x, activeCoordinates.y);
	}

	public int getRowCount()
	{
		if(binding == null)
			return 0;
		return binding.getDimension().height;
	}

	public int getColumnCount()
	{
		if(binding == null)
			return 0;
		return binding.getDimension().width;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return binding.getBound(columnIndex, rowIndex);
	}

	public String getColumnName(int columnIndex)
	{
		return null;
	}

	public Class getColumnClass(int columnIndex)
	{
		return ObjectResource.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		binding.bind((ObjectResource )aValue, columnIndex, rowIndex);
	}


	public void setActiveCoordinates(Point activeCoordinates)
	{
		this.activeCoordinates = activeCoordinates;
	}


	public Point getActiveCoordinates()
	{
		return activeCoordinates;
	}

}
