/*
 * $Id: TreeDataSelectionEvent.java,v 1.9 2005/03/05 15:23:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/03/05 15:23:50 $
 * @module generalclient_v1
 */
public class TreeDataSelectionEvent extends OperationEvent {
	private Class dataClass;
	private int selected;
	private List list;
	private Object selectedObject;
	private ObjectResourceController controller;
	
	public static final String type = "treedataselectionevent";

	public TreeDataSelectionEvent(Object source, List list, Class dataClass, int selected, Object selectedObject, ObjectResourceController controller) {
		this(source, list, dataClass, selected, selectedObject);
		this.controller = controller;
	}

	public TreeDataSelectionEvent(Object source, List list, Class dataClass, int selected, Object selectedObject) {
		super(source, 0, type);
		this.list = list;
		this.dataClass = dataClass;
		this.selected = selected;
		this.selectedObject = selectedObject;
	}

	public List getList() {
		return this.list;
	}

	public Class getDataClass() {
		return this.dataClass;
	}

	public int getSelectionNumber() {
		return this.selected;
	}

	/**
	 * @return Returns the selectedObject.
	 */
	public Object getSelectedObject() {
		return this.selectedObject;
	}
	
	public ObjectResourceController getController() {
		return this.controller;
	}
}
