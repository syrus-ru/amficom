/*
 * $Id: ObjComboBox.java,v 1.4 2004/11/16 07:19:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2004/11/16 07:19:37 $
 * @module generalclient_v1
 */
public class ObjComboBox extends AComboBox {

	private static final long	serialVersionUID	= -6436644083913146379L;
	
	private ObjListModel	model;

	public ObjComboBox(ObjListModel model) {
		this.setRenderer(LabelCheckBoxRenderer.getInstance());
		this.model = model;
		this.setModel(model);
	}
	
	public ObjComboBox(ObjectResourceController controller, List objects, String key) {
		this(new ObjListModel(controller, objects, key));
	}

	public ObjComboBox(ObjectResourceController controller, String key) {
		this(new ObjListModel(controller, new LinkedList(), key));
	}

	public void setSelectedItem(Object anObject) {		
		Object fieldByObject = this.model.getFieldByObject(anObject);
		super.setSelectedItem(fieldByObject);
	}

}
