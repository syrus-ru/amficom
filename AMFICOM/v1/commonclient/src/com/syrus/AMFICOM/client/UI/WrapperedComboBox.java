/*
 * $Id: WrapperedComboBox.java,v 1.1 2005/05/19 14:06:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Wrapper;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @module generalclient_v1
 */
public class WrapperedComboBox extends AComboBox {

	private static final long	serialVersionUID	= -6436644083913146379L;

	private WrapperedListModel	model;

	public WrapperedComboBox(WrapperedListModel model) {
		this.setRenderer(new LabelCheckBoxRenderer(model.wrapper, model.key));
		this.model = model;
		this.setModel(model);
	}

	public WrapperedComboBox(Wrapper wrapper, List objects, String key) {
		this(new WrapperedListModel(wrapper, objects, key));
	}

	public WrapperedComboBox(Wrapper wrapper, String key) {
		this(new WrapperedListModel(wrapper, new LinkedList(), key));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}
}
