/*
 * $Id: ObjComboBox.java,v 1.1 2004/10/06 13:17:35 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2004/10/06 13:17:35 $
 * @module generalclient_v1
 */
public class ObjComboBox extends AComboBox {

	private ObjComboBoxModel	model;

	public ObjComboBox(ObjComboBoxModel model) {
		this.setRenderer(LabelCheckBoxRenderer.getInstance());
		this.model = model;
		this.setModel(model);
	}

	public void setSelectedItem(Object anObject) {		
		Object fieldByObject = this.model.getFieldByObject(anObject);
		super.setSelectedItem(fieldByObject);
	}

}
