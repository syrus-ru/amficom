package com.syrus.AMFICOM.Client.General.Event;

import java.util.Collection;

import com.syrus.AMFICOM.Client.Resource.DataSet;

public class TreeDataSelectionEvent extends OperationEvent
{
	DataSet dataSet;
	Class dataClass;
	int selected;
	
	Collection collection;

	public Object param = null;
	/**
	 * @deprecated use use getter accessor for this field
	 */
	public Object selectedObject = null;

	public static final String type = "treedataselectionevent";

	/**
	 * @deprecated use  TreeDataSelectionEvent(Object source, Collection collection, Class dataClass, int selected, Object selectedObject)
	 * DataSet usage is deprecated
	 * @param source
	 * @param dataSet
	 * @param dataClass
	 * @param selected
	 * @param selectedObject
	 */
	public TreeDataSelectionEvent(Object source, DataSet dataSet, Class dataClass, int selected, Object selectedObject)
	{
		super(source, 0, type);
		this.dataSet = dataSet;
		this.dataClass = dataClass;
		this.selected = selected;
		this.selectedObject = selectedObject;
	}
	
	public TreeDataSelectionEvent(Object source, Collection collection, Class dataClass, int selected, Object selectedObject)
	{
		super(source, 0, type);
		this.collection = collection;
		this.dataClass = dataClass;
		this.selected = selected;
		this.selectedObject = selectedObject;
	}

	/**
	 * @deprecated DataSet usage is deprecated, use Collection
	 * @return
	 */
	public DataSet getDataSet()
	{
		if (this.dataSet==null){
			this.dataSet = new DataSet(this.collection.iterator());
		}
		return this.dataSet;
	}
	
	public Collection getCollection(){
		return this.collection;
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
