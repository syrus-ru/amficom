/*
 * $Id: WrapperedComboBox.java,v 1.2 2005/05/23 12:51:04 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/23 12:51:04 $
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

	public WrapperedComboBox(Wrapper wrapper, List objects, String key, String compareKey) {
		this(new WrapperedListModel(wrapper, objects, key, compareKey));
	}

	public WrapperedComboBox(Wrapper wrapper, String key, String compareKey) {
		this(new WrapperedListModel(wrapper, new LinkedList(), key, compareKey));
	}	

	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(Collection objects) {
		this.model.addElements(objects);
	}
	
	public void setSelectedItem(Object anObject) {
		Object anObjectValue = this.model.wrapper.getValue(anObject, this.model.compareKey);
		Object oldSelection = this.model.wrapper.getValue(this.selectedItemReminder, this.model.compareKey);
//		Log.debugMessage("WrapperedComboBox.setSelectedItem | anObjectValue " + anObjectValue, Log.FINEST);
//		Log.debugMessage("WrapperedComboBox.setSelectedItem | oldSelection " + oldSelection, Log.FINEST);
		if (oldSelection == null || !oldSelection.equals(anObjectValue)) {

			if (anObjectValue != null && !isEditable()) {
				for (int i = 0; i < this.model.getSize(); i++) {
					Object elementAt = this.model.getElementAt(i);
					oldSelection = this.model.wrapper.getValue(this.model.getElementAt(i), this.model.compareKey);
					if (anObjectValue.equals(oldSelection)) {
						super.setSelectedItem(elementAt);
						break;
					}
				}

			}else {
				super.setSelectedItem(null);
			}
		} 
	}
	
}
