package com.syrus.AMFICOM.Client.General.Event;

import java.util.Collection;

import java.util.List;

public class TreeDataSelectionEvent extends OperationEvent
{
	private Class dataClass;
	private int selected;
	
	private List list;

//	private Object param = null;
	private Object selectedObject = null;

	public static final String type = "treedataselectionevent";
	
	public TreeDataSelectionEvent(Object source, List list, Class dataClass, int selected, Object selectedObject)
	{
		super(source, 0, type);
		this.list = list;
		this.dataClass = dataClass;
		this.selected = selected;
		this.selectedObject = selectedObject;
	}
	
	public Collection getList(){
		return this.list;
	}

	public Class getDataClass()
	{
		return this.dataClass;
	}

	public int getSelectionNumber()
	{
		return this.selected;
	}
	/**
	 * @return Returns the selectedObject.
	 */
	public Object getSelectedObject() {
		return this.selectedObject;
	}
}
