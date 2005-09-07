
package com.syrus.AMFICOM.client.UI;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Wrapper;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class WrapperedListModel<T> extends AbstractListModel implements MutableComboBoxModel, Serializable {

	private static final long serialVersionUID = -1607982236171940302L;

	protected T selectedObject;

	protected List<T> objects;

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
		this.wrapper = wrapper;
		this.key = key;
		this.objects = objects;
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
		if ((this.selectedObject != null && !this.selectedObject.equals(anObject)) || this.selectedObject == null && anObject != null) {
			this.selectedObject = (T) anObject;
			super.fireContentsChanged(this, -1, -1);
		}
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem() {
		return this.selectedObject;
	}

	// implements javax.swing.ListModel
	public int getSize() {
		return (this.objects != null) ? this.objects.size() : 0;
	}

	// implements javax.swing.ListModel
	public Object getElementAt(final int index) {
		Object obj = null;
		if (index >= 0 && index < this.objects.size()) {
			obj = this.objects.get(index);
		}
		return obj;
	}

	public Object getFieldByObject(final T object) {

		Object obj = this.wrapper.getValue(object, this.key);

		if (this.wrapper.getPropertyValue(this.key) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(this.key);
			Object keyObject = null;
			for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
				final Object keyObj = it.next();
				if (map.get(keyObj).equals(obj)) {
					keyObject = keyObj;
					break;
				}
			}
			obj = keyObject;

		}

		return obj;
	}

	public Object getObjectByField(final Object field) {
		
		assert field != null : ErrorMessages.NON_NULL_EXPECTED;
		
		Object object = null;
		if (field != null) {
			for (final T element : this.objects) {
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
		if (this.objects != null) {
			for (final T element : this.objects) {
				final Object anObjectValue = this.wrapper.getValue(anObject, this.compareKey);
				final Object elementValue = this.wrapper.getValue(element, this.compareKey);
				if (anObjectValue.equals(elementValue)) {
					index = this.objects.indexOf(element);
					break;
				}
			}
		}
		return index;
	}

	// implements javax.swing.MutableComboBoxModel
	public void addElement(final Object anObject) {
		this.objects.add((T) anObject);
		this.sort();
		super.fireIntervalAdded(this, this.objects.size() - 1, this.objects.size() - 1);
		if (this.objects.size() == 1 && this.selectedObject == null && anObject != null) {
			this.setSelectedItem(anObject);
		}
	}

	public void addElements(final Collection<T> addingObjects) {

		assert addingObjects != null : ErrorMessages.NON_NULL_EXPECTED;

		if (addingObjects.size() == 0) {
			return;
		}
		this.objects.addAll(addingObjects);
		super.fireIntervalAdded(this, this.objects.size() - addingObjects.size(), this.objects.size() - 1);
	}

	public final void setElements(final Collection<T> objects) {
		this.objects.clear();
		if (objects != null) {
			this.objects.addAll(objects);
			this.sort();
			int size = this.objects.size();
			super.fireContentsChanged(this, 0, size > 0 ? size - 1 : 0);
		}
	}

	public void sort() {
		if (this.objects != null) {
			Collections.sort(this.objects, new WrapperComparator<T>(this.wrapper, this.key, true));
			super.fireContentsChanged(this, 0, this.objects.size());
		}

	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(final Object anObject, final int index) {
		this.objects.add(index, (T) anObject);
		super.fireIntervalAdded(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(final int index) {
		if (this.getElementAt(index) == this.selectedObject) {
			if (index == 0) {
				this.setSelectedItem(getSize() == 1 ? null : this.getElementAt(index + 1));
			} else {
				this.setSelectedItem(this.getElementAt(index - 1));
			}
		}

		this.objects.remove(index);

		super.fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(final Object anObject) {
		int index = this.objects.indexOf(anObject);
		if (index != -1) {
			this.removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (this.objects != null && this.objects.size() > 0) {
			final int firstIndex = 0;
			final int lastIndex = this.objects.size() - 1;
			this.objects.clear();
			this.selectedObject = null;
			super.fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			this.selectedObject = null;
		}
	}

}
