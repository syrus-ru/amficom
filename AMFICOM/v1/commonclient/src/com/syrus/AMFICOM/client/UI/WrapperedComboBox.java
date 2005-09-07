/*
 * $Id: WrapperedComboBox.java,v 1.4 2005/09/07 02:37:31 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/07 02:37:31 $
 * @module commonclient
 */
public class WrapperedComboBox<T> extends AComboBox {

	private static final long serialVersionUID = -6436644083913146379L;

	private WrapperedListModel<T> model;

	public WrapperedComboBox(final WrapperedListModel<T> model) {
		this.setRenderer(new LabelCheckBoxRenderer(model.wrapper, model.key));
		this.model = model;
		this.setModel(model);
	}

	public WrapperedComboBox(final Wrapper<T> wrapper, final List<T> objects, final String key, final String compareKey) {
		this(new WrapperedListModel<T>(wrapper, objects, key, compareKey));
	}

	public WrapperedComboBox(final Wrapper<T> wrapper, final String key, final String compareKey) {
		this(new WrapperedListModel<T>(wrapper, new LinkedList<T>(), key, compareKey));
	}	

	@Override
	public void removeAll() {
		super.removeAll();
		this.model.removeAllElements();
	}

	public void addElements(final Collection<T> objects) {
		this.model.addElements(objects);
	}

	@Override
	public void setSelectedItem(final Object anObject) {
		final Object anObjectValue = this.model.wrapper.getValue((T) anObject, this.model.compareKey);
		Object oldSelection = this.model.wrapper.getValue((T) this.selectedItemReminder, this.model.compareKey);
//		Log.debugMessage("WrapperedComboBox.setSelectedItem | anObjectValue " + anObjectValue, Log.FINEST);
//		Log.debugMessage("WrapperedComboBox.setSelectedItem | oldSelection " + oldSelection, Log.FINEST);
		if (oldSelection == null || !oldSelection.equals(anObjectValue)) {

			if (anObjectValue != null && !isEditable()) {
				for (int i = 0; i < this.model.getSize(); i++) {
					final Object elementAt = this.model.getElementAt(i);
					oldSelection = this.model.wrapper.getValue((T) this.model.getElementAt(i), this.model.compareKey);
					if (anObjectValue.equals(oldSelection)) {
						super.setSelectedItem(elementAt);
						break;
					}
				}

			} else {
				super.setSelectedItem(null);
			}
		}
	}
	
}
