/*
 * $Id: ObjComboBox.java,v 1.8 2005/04/13 21:40:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/04/13 21:40:46 $
 * @module generalclient_v1
 */
public class ObjComboBox extends AComboBox {

	private static final long	serialVersionUID	= -6436644083913146379L;

	private ObjListModel	model;

	public ObjComboBox(ObjListModel model) {
		LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.controller, model.key);
		this.setRenderer(renderer);
		this.model = model;
		this.setModel(model);
	}

	public ObjComboBox(ObjectResourceController controller, List objects, String key) {
		this(new ObjListModel(controller, objects, key));
	}

	public ObjComboBox(ObjectResourceController controller, String key) {
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
