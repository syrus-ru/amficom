/*
 * $Id: ObjComboBox.java,v 1.9 2005/05/05 11:04:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.util.Wrapper;

/**
 * @author $Author: bob $
 * @version $Revision: 1.9 $, $Date: 2005/05/05 11:04:48 $
 * @module generalclient_v1
 */
public class ObjComboBox extends AComboBox {

	private static final long	serialVersionUID	= -6436644083913146379L;

	private ObjListModel	model;

	public ObjComboBox(ObjListModel model) {
		LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.wrapper, model.key);
		this.setRenderer(renderer);
		this.model = model;
		this.setModel(model);
	}

	public ObjComboBox(Wrapper wrapper, List objects, String key) {
		this(new ObjListModel(wrapper, objects, key));
	}

	public ObjComboBox(Wrapper wrapper, String key) {
		this(new ObjListModel(wrapper, new LinkedList(), key));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}
}
