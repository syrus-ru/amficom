package com.syrus.AMFICOM.client_.general.ui_;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/03 08:48:21 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ObjListModel extends AbstractListModel implements MutableComboBoxModel, Serializable {

	private static final long	serialVersionUID	= -1607982236171940302L;

	private Object				selectedObject;

	private List				objects;

	/**
	 * ObjectResourceController of Model (ObjectResource) will be used for
	 * sorting. see {@link ObjectResourceController}
	 */
	protected ObjectResourceController	controller;

	private String				key;

	/**
	 * @param controller
	 *                see {@link #controller}
	 */
	public ObjListModel(ObjectResourceController controller, String key) {
		this(controller, new ArrayList(), key);

	}

	public ObjListModel(ObjectResourceController controller, List objects, String key) {
		this.controller = controller;
		this.key = key;
		this.objects = objects;
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * 
	 * @param anObject
	 *                The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject) {
		if ((this.selectedObject != null && !this.selectedObject.equals(anObject))
				|| this.selectedObject == null && anObject != null) {
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
			Object object = this.objects.get(index);
			obj = getFieldByObject(object);
		}
		return obj;
	}

	public Object getFieldByObject(Object object) {
		
		Object obj = this.controller.getValue(object, this.key);

		if (this.controller.getPropertyValue(this.key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(this.key);
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
	
	public Object getObjectByField(Object field){
		Object object = null;
		for (Iterator it = this.objects.iterator(); it.hasNext();) {
			Object element = it.next();
			if (field.equals(getFieldByObject(element))){
				object = element;
				break;
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
		return this.objects.indexOf(anObject);
	}

	// implements javax.swing.MutableComboBoxModel
	public void addElement(Object anObject) {
		this.objects.add(anObject);
		fireIntervalAdded(this, this.objects.size() - 1, this.objects.size() - 1);
		if (this.objects.size() == 1 && this.selectedObject == null && anObject != null) {
			setSelectedItem(anObject);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(Object anObject, int index) {
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
	public void removeElement(Object anObject) {
		int index = this.objects.indexOf(anObject);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (this.objects.size() > 0) {
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