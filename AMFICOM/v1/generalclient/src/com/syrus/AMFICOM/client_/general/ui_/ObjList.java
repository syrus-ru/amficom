/*
 * $Id: ObjList.java,v 1.4 2004/11/03 08:48:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2004/11/03 08:48:21 $
 * @module generalclient_v1
 */
public class ObjList extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;
	
	private ObjListModel	model;

	public ObjList(ObjListModel model) {
		this.setCellRenderer(LabelCheckBoxRenderer.getInstance());
		this.model = model;
		this.setModel(model);
	}
	
	public ObjList(ObjectResourceController controller, List objects, String key) {
		this(new ObjListModel(controller, objects, key));
	}
	
	public ObjList(ObjectResourceController controller, String key) {
		this(new ObjListModel(controller, new LinkedList(), key));
	}
	
	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		Object fieldByObject = this.model.getFieldByObject(anObject);
		super.setSelectedValue(fieldByObject, shouldScroll);
	}	
	
	public Object getSelectedValue() {
		Object selectedValue = this.model.getObjectByField(super.getSelectedValue());
		return selectedValue;
	}
	
}
