package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.Client.Resource.DataSet;

public class TreeDataSelectionEvent extends OperationEvent
{
	DataSet dataSet;
	Class dataClass;
	int selected;

	public Object param = null;
	public Object selectedObject = null;

	public static final String type = "treedataselectionevent";

	public TreeDataSelectionEvent(Object source, DataSet dataSet, Class dataClass, int selected, Object selectedObject)
	{
		super(source, 0, type);
		this.dataSet = dataSet;
		this.dataClass = dataClass;
		this.selected = selected;
		this.selectedObject = selectedObject;
	}

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public Class getDataClass()
	{
		return dataClass;
	}

	public int getSelectionNumber()
	{
		return selected;
	}
}
