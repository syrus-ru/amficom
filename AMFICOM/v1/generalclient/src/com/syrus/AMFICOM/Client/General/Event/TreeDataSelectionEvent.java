/*
 * $Id: TreeDataSelectionEvent.java,v 1.7 2004/09/27 10:44:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 10:44:52 $
 * @module generalclient_v1
 */
public class TreeDataSelectionEvent extends OperationEvent
{
	private Class dataClass;

	private int selected;
	
	private List list;

	private Object param = null;

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

	public void setParam(Object param)
	{
		this.param = param;
	}

	public Object getParam()
	{
		return this.param;
	}

	public List getList()
	{
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
	public Object getSelectedObject()
	{
		return this.selectedObject;
	}
}
