/*-
* $Id: WrapperedList.java,v 1.13 2005/10/06 08:07:17 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;

import com.syrus.util.Log;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/06 08:07:17 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class WrapperedList<T> extends JList {

	private static final long	serialVersionUID	= -1575309361246285747L;

	private WrapperedListModel<T>	model;

	public WrapperedList(final WrapperedListModel<T> model) {
		super(model);
		final LabelCheckBoxRenderer renderer = new LabelCheckBoxRenderer<T>(model.wrapper, model.key);
		this.setCellRenderer(renderer);
		this.model = model;
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
		final Object anObjectValue = this.model.compareKey != null ? 
				this.model.wrapper.getValue((T) anObject, this.model.compareKey) : 
				anObject;
//		 Log.debugMessage("WrapperedList.setSelectedValue | anObject " + anObject, Log.DEBUGLEVEL10);
//		 Log.debugMessage("WrapperedList.setSelectedValue | this.model.compareKey:" + this.model.compareKey, Log.DEBUGLEVEL10);
//		 Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue " + anObjectValue, Log.DEBUGLEVEL10);
		if (anObjectValue == null) {
			// System.err.println("WrapperedList.setSelectedValue() | -1");
			final int selectedIndex = super.getSelectedIndex();
			super.removeSelectionInterval(selectedIndex, selectedIndex);
			super.repaint();
		} else {
			final Object selectedValue = super.getSelectedValue();
			Object elementValue = this.model.compareKey != null ? 
					this.model.wrapper.getValue((T) selectedValue, this.model.compareKey) :
					selectedValue;
			// Log.debugMessage("WrapperedList.setSelectedValue | elementValue " +
			// elementValue, Level.FINEST);
			if (!anObjectValue.equals(elementValue)) {
				int count = this.model.getSize();
				assert Log.debugMessage("WrapperedList.setSelectedValue | count " + count, Log.DEBUGLEVEL10);
				for (int i = 0; i < count; i++) {
					final Object elementAt = this.model.getElementAt(i);
					elementValue = this.model.compareKey != null ? 
							this.model.wrapper.getValue((T) elementAt, this.model.compareKey) :
							elementAt;
					assert Log.debugMessage("WrapperedList.setSelectedValue | anObjectValue " 
							+ anObjectValue 
							+ " > elementValue " 
							+ elementValue, 
						Log.DEBUGLEVEL10);
					if (anObjectValue.equals(elementValue)) {
						assert Log.debugMessage("WrapperedList.setSelectedValue | index " + i, Log.DEBUGLEVEL10);
						super.setSelectedIndex(i);
						if (shouldScroll) {
							super.ensureIndexIsVisible(i);
						}
						super.repaint();
						return;
					}
				}
			}

			assert Log.debugMessage("WrapperedList.setSelectedValue | index -1" , Log.DEBUGLEVEL10);
			final int selectedIndex = super.getSelectedIndex();
			super.removeSelectionInterval(selectedIndex, selectedIndex);
			super.repaint();
		}

	}
	
}
