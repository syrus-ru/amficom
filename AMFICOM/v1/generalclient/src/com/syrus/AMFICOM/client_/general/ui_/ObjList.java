/*
 * $Id: ObjList.java,v 1.8 2005/05/05 11:04:48 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

import javax.swing.JList;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: bob $
 * @version $Revision: 1.8 $, $Date: 2005/05/05 11:04:48 $
 * @module generalclient_v1
 */
public class ObjList extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;

	private ObjListModel	model;

	public ObjList(ObjListModel model) {
		LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.wrapper, model.key);
		this.setCellRenderer(renderer);
		this.model = model;
		this.setModel(model);		
	}

	public ObjList(ObjectResourceController controller, List objects, String key) {
		this(new ObjListModel(controller, objects, key));
	}

	public ObjList(ObjectResourceController controller, String key) {
		this(new ObjListModel(controller, new LinkedList(), key));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}
}
