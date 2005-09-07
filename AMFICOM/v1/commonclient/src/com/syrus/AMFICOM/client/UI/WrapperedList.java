/*
 * $Id: WrapperedList.java,v 1.6 2005/09/07 02:37:31 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/09/07 02:37:31 $
 * @module commonclient
 */
public class WrapperedList<T> extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;

	private WrapperedListModel<T>	model;

	public WrapperedList(final WrapperedListModel<T> model) {
		final LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer(model.wrapper, model.key);
		this.setCellRenderer(renderer);
		this.model = model;
		this.setModel(model);		
	}

	public WrapperedList(final Wrapper<T> controller, final List<T> objects, final String key, final String compareKey) {
		this(new WrapperedListModel<T>(controller, objects, key, compareKey));
	}

	public WrapperedList(final Wrapper<T> controller, final String key, final String compareKey) {
		this(new WrapperedListModel<T>(controller, new LinkedList<T>(), key, compareKey));
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
	public WrapperedListModel<T> getModel() {
		return (WrapperedListModel<T>) super.getModel();
	}

	@Override
	public void setSelectedValue(final Object anObject, final boolean shouldScroll) {
		final Object anObjectValue = this.model.wrapper.getValue((T) anObject, this.model.compareKey);
		// Log.debugMessage("WrapperedList.setSelectedValue | anObject " + anObject,
		// Log.FINEST);
		// Log.debugMessage("WrapperedList.setSelectedValue | this.model.compareKey:
		// " + this.model.compareKey, Log.FINEST);
		// Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue " +
		// anObjectValue, Log.FINEST);
		if (anObjectValue == null) {
			// System.err.println("WrapperedList.setSelectedValue() | -1");
			final int selectedIndex = super.getSelectedIndex();
			super.removeSelectionInterval(selectedIndex, selectedIndex);
			super.repaint();
		} else {
			Object elementValue = this.model.wrapper.getValue((T) super.getSelectedValue(), this.model.compareKey);
			// Log.debugMessage("WrapperedList.setSelectedValue | elementValue " +
			// elementValue, Level.FINEST);
			if (!anObjectValue.equals(elementValue)) {
				final WrapperedListModel<T> dm = this.getModel();
				int count = dm.getSize();
				// Log.debugMessage("WrapperedList.setSelectedValue | count " + count,
				// Log.FINEST);
				for (int i = 0; i < count; i++) {
					elementValue = this.model.wrapper.getValue((T) dm.getElementAt(i), this.model.compareKey);
					// Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue "
					// + anObjectValue, Level.FINEST);
					// Log.debugMessage("WrapperedList.setSelectedValue | elementValue " +
					// elementValue, Level.FINEST);
					if (anObjectValue.equals(elementValue)) {
						// System.out.println("WrapperedList.setSelectedValue() | " + i);
						super.setSelectedIndex(i);
						if (shouldScroll) {
							super.ensureIndexIsVisible(i);
						}
						super.repaint();
						return;
					}
				}

			}
			// System.out.println("WrapperedList.setSelectedValue() | -1");
			final int selectedIndex = super.getSelectedIndex();
			super.removeSelectionInterval(selectedIndex, selectedIndex);
			super.repaint();
		}

	}
	
}
