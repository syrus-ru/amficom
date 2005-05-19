/*
 * $Id: WrapperedList.java,v 1.1 2005/05/19 14:06:41 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;

import com.syrus.util.Wrapper;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @module generalclient_v1
 */
public class WrapperedList extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;

	private WrapperedListModel	model;

	public WrapperedList(WrapperedListModel model) {
		LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.wrapper, model.key);
		this.setCellRenderer(renderer);
		this.model = model;
		this.setModel(model);		
	}

	public WrapperedList(Wrapper controller, List objects, String key) {
		this(new WrapperedListModel(controller, objects, key));
	}

	public WrapperedList(Wrapper controller, String key) {
		this(new WrapperedListModel(controller, new LinkedList(), key));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}
}
