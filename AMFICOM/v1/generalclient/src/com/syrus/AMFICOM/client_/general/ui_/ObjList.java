/*
 * $Id: ObjList.java,v 1.2 2004/10/07 06:00:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JList;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2004/10/07 06:00:41 $
 * @module generalclient_v1
 */
public class ObjList extends JList {

	private ObjListModel	model;

	public ObjList(ObjListModel model) {
		this.setCellRenderer(LabelCheckBoxRenderer.getInstance());
		this.model = model;
		this.setModel(model);
	}

	
	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		Object fieldByObject = this.model.getFieldByObject(anObject);
		super.setSelectedValue(fieldByObject, shouldScroll);
	}
	
}
