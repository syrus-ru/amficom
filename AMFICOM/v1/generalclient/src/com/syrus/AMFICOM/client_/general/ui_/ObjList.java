/*
 * $Id: ObjList.java,v 1.1 2004/10/06 14:48:18 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JList;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2004/10/06 14:48:18 $
 * @module generalclient_v1
 */
public class ObjList extends JList {

	private ObjListModel	model;

	public ObjList(ObjListModel model) {
		this.setCellRenderer(LabelCheckBoxRenderer.getInstance());
		this.model = model;
		this.setModel(model);
	}

}
