/*-
* $Id: WrapperedComboBox.java,v 1.9 2005/10/30 15:20:24 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/30 15:20:24 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class WrapperedComboBox<T> extends AComboBox {

	private static final long serialVersionUID = -6436644083913146379L;

	private WrapperedListModel<T> model;

	public WrapperedComboBox(final WrapperedListModel<T> model) {
		super(model);
		this.setRenderer(new LabelCheckBoxRenderer<T>(model.wrapper, model.key));
		this.model = model;
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
		final Object anObjectValue = this.model.compareKey != null ? 
				this.model.wrapper.getValue((T) anObject, this.model.compareKey) :
				anObject;
		Object oldSelection = this.model.compareKey != null ? 
				this.model.wrapper.getValue((T) this.selectedItemReminder, this.model.compareKey) : 
				this.selectedItemReminder;
//		assert Log.debugMessage("anObjectValue " + anObjectValue, Log.FINEST);
//		assert Log.debugMessage("oldSelection " + oldSelection, Log.FINEST);
		if (oldSelection == null || !oldSelection.equals(anObjectValue)) {

			if (anObjectValue != null && !isEditable()) {
				for (int i = 0; i < this.model.getSize(); i++) {
					final Object elementAt = this.model.getElementAt(i);
					oldSelection = this.model.compareKey != null ?
							this.model.wrapper.getValue((T) elementAt, this.model.compareKey) :
							elementAt;
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
