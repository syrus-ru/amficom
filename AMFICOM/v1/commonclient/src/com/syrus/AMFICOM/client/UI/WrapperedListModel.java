
package com.syrus.AMFICOM.client.UI;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/10 07:04:43 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class WrapperedListModel extends AbstractListModel implements MutableComboBoxModel, Serializable {

	private static final long	serialVersionUID	= -1607982236171940302L;

	private Object				selectedObject;

	private List				objects;

	/**
	 * Wrapper of Model (ObjectResource) will be used for sorting. see
	 * {@link Wrapper}
	 */
	protected Wrapper			wrapper;

	protected String			key;
	
	protected String			compareKey;

	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 */
	public WrapperedListModel(Wrapper wrapper, String key, String compareKey) {
		this(wrapper, new LinkedList(), key, compareKey);

	}

	public WrapperedListModel(Wrapper wrapper, List objects, String key, String compareKey) {
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
	public void setSelectedItem(Object anObject) {
		if ((this.selectedObject != null && !this.selectedObject.equals(anObject)) || this.selectedObject == null
				&& anObject != null) {
			this.selectedObject = anObject;
			fireContentsChanged(this, -1, -1);
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
	public Object getElementAt(int index) {
		Object obj = null;
		if (index >= 0 && index < this.objects.size()) {
			obj = this.objects.get(index);
		}
		return obj;
	}

	public Object getFieldByObject(Object object) {

		Object obj = this.wrapper.getValue(object, this.key);

		if (this.wrapper.getPropertyValue(this.key) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(this.key);
			Object keyObject = null;
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				Object keyObj = it.next();
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
			for (Iterator it = this.objects.iterator(); it.hasNext();) {
				Object element = it.next();
				if (field.equals(getFieldByObject(element))) {
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
	public int getIndexOf(Object anObject) {
		int index = -1;
		if (this.objects != null) {
			for (int i = 0; i < this.objects.size(); i++) {
				Object element = this.objects.get(i);
				Object anObjectValue = this.wrapper.getValue(anObject, this.compareKey);
				Object elementValue = this.wrapper.getValue(element, this.compareKey);
				if (anObjectValue.equals(elementValue)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	// implements javax.swing.MutableComboBoxModel
	public void addElement(Object anObject) {
		this.objects.add(anObject);
		fireIntervalAdded(this, this.objects.size() - 1, this.objects.size() - 1);
		if (this.objects.size() == 1 && this.selectedObject == null && anObject != null) {
			setSelectedItem(anObject);
		}
	}

	public void addElements(final Collection _objects) {
		
		assert _objects != null : ErrorMessages.NON_NULL_EXPECTED;
		
		if (_objects.size() == 0)
			return;
		this.objects.addAll(_objects);
		fireIntervalAdded(this, this.objects.size() - _objects.size(), this.objects.size() - 1);
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(final Object anObject,
								int index) {
		this.objects.add(index, anObject);
		fireIntervalAdded(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(int index) {
		if (getElementAt(index) == this.selectedObject) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		this.objects.remove(index);

		fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(final Object anObject) {
		int index = this.objects.indexOf(anObject);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (this.objects != null && this.objects.size() > 0) {
			int firstIndex = 0;
			int lastIndex = this.objects.size() - 1;
			this.objects.clear();
			this.selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			this.selectedObject = null;
		}
	}
}
