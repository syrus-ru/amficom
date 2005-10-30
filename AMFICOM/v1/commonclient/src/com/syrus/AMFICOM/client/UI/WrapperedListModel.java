
package com.syrus.AMFICOM.client.UI;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.10 $, $Date: 2005/10/30 14:48:51 $
 * @author $Author: bass $
 * @module commonclient
 */
public final class WrapperedListModel<T> extends AbstractListModel implements MutableComboBoxModel, Serializable {

	private static final long serialVersionUID = -1607982236171940302L;

	protected T selectedT;

	protected List<T> ts;

	/**
	 * Wrapper of Model (ObjectResource) will be used for sorting. see
	 * {@link Wrapper}
	 */
	protected Wrapper<T> wrapper;

	protected String key;

	protected String compareKey;
	
	/**
	 * @param wrapper
	 *        see {@link #wrapper}
	 */
	public WrapperedListModel(final Wrapper<T> wrapper, final String key, final String compareKey) {
		this(wrapper, new LinkedList<T>(), key, compareKey);

	}
	
	public WrapperedListModel(Wrapper<T> wrapper, List<T> objects, String key, String compareKey) {
		assert wrapper != null : ErrorMessages.NON_NULL_EXPECTED;
		this.wrapper = wrapper;
		
		assert key != null : ErrorMessages.NON_NULL_EXPECTED;		
		this.key = key;
		
		assert objects != null : ErrorMessages.NON_NULL_EXPECTED;
		this.ts = objects;
		this.compareKey = compareKey;
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * 
	 * @param anObject
	 *            The combo box value or null for no selection.
	 */
	public void setSelectedItem(final Object anObject) {
		if ((this.selectedT != null && !this.selectedT.equals(anObject)) || this.selectedT == null && anObject != null) {
			this.selectedT = (T) anObject;
			super.fireContentsChanged(this, -1, -1);
		}
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem() {
		return this.selectedT;
	}

	// implements javax.swing.ListModel
	public int getSize() {
		return (this.ts != null) ? this.ts.size() : 0;
	}

	// implements javax.swing.ListModel
	public Object getElementAt(final int index) {
		Object obj = null;
		if (index >= 0 && index < this.ts.size()) {
			obj = this.ts.get(index);
		}
		return obj;
	}

	public Object getFieldByObject(final T t) {

		Object object = this.wrapper.getValue(t, this.key);

		if (this.wrapper.getPropertyValue(this.key) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(this.key);
			Object keyObject = null;
			for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
				final Object keyObj = it.next();
				if (map.get(keyObj).equals(object)) {
					keyObject = keyObj;
					break;
				}
			}
			object = keyObject;

		}

		return object;
	}

	public Object getObjectByField(final Object field) {
		
		assert field != null : ErrorMessages.NON_NULL_EXPECTED;
		
		Object object = null;
		if (field != null) {
			for (final T element : this.ts) {
				if (field.equals(this.getFieldByObject(element))) {
					object = element;
					break;
				}
			}
		}
		return object;
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 * 
	 * @param anObject
	 * @return an int representing the index position, where 0 is the first
	 *         position
	 */
	public int getIndexOf(final T anObject) {
		int index = -1;
		if (this.ts != null) {
			for (final T element : this.ts) {
				final Object anObjectValue = 
					this.compareKey != null ? 
							this.wrapper.getValue(anObject, this.compareKey) :
							anObject;
				final Object elementValue = 
					this.compareKey != null ?
						this.wrapper.getValue(element, this.compareKey) :
						element;
				if (anObjectValue.equals(elementValue)) {
					index = this.ts.indexOf(element);
					break;
				}
			}
		}
		return index;
	}

	// implements javax.swing.MutableComboBoxModel
	public void addElement(final Object anObject) {
		Log.debugMessage("before " + this.ts, Log.DEBUGLEVEL10);
		this.ts.add((T) anObject);
		this.sort();
		super.fireIntervalAdded(this, this.ts.size() - 1, this.ts.size() - 1);
		if (this.ts.size() == 1 && this.selectedT == null && anObject != null) {
			this.setSelectedItem(anObject);
		}
		Log.debugMessage("after " + this.ts, Log.DEBUGLEVEL10);
	}

	public void addElements(final Collection<T> addingObjects) {

		assert addingObjects != null : ErrorMessages.NON_NULL_EXPECTED;

		if (addingObjects.size() == 0) {
			return;
		}
		this.ts.addAll(addingObjects);
		super.fireIntervalAdded(this, this.ts.size() - addingObjects.size(), this.ts.size() - 1);
	}

	public final void setElements(final Collection<T> objects) {
		this.ts.clear();
		if (objects != null) {
			this.ts.addAll(objects);
			this.sort();
			int size = this.ts.size();
			super.fireContentsChanged(this, 0, size > 0 ? size - 1 : 0);
		}
	}

	public void sort() {
		if (this.ts != null) {
			Collections.sort(this.ts, new WrapperComparator<T>(this.wrapper, this.key, true));
			super.fireContentsChanged(this, 0, this.ts.size());
		}

	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(final Object anObject, final int index) {
		this.ts.add(index, (T) anObject);
		super.fireIntervalAdded(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(final int index) {
		if (this.getElementAt(index) == this.selectedT) {
			if (index == 0) {
				this.setSelectedItem(getSize() == 1 ? null : this.getElementAt(index + 1));
			} else {
				this.setSelectedItem(this.getElementAt(index - 1));
			}
		}

		this.ts.remove(index);

		super.fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(final Object anObject) {
		int index = this.ts.indexOf(anObject);
		if (index != -1) {
			this.removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (this.ts != null && this.ts.size() > 0) {
			final int firstIndex = 0;
			final int lastIndex = this.ts.size() - 1;
			this.ts.clear();
			this.selectedT = null;
			super.fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			this.selectedT = null;
		}
	}

}
