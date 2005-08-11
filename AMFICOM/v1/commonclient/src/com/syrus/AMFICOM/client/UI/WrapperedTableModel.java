
package com.syrus.AMFICOM.client.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Wrapper;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/11 18:51:08 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class WrapperedTableModel extends AbstractTableModel {

	private static final long	serialVersionUID	= 4007513055820570639L;

	/**
	 * ObjectResourceController of Model (StorableObject) will be used for
	 * sorting. see {@link Wrapper}
	 */
	protected Wrapper			wrapper;

	private List				list;

	protected String[]			keys;

	/**
	 * saved direction of column sorting. Used when change direction to negative
	 * to current. see {@link WrapperComparator#ascend}
	 */
	private boolean[]			ascendings;

	
	private boolean[]			editables;
	
	private int lastSortedModelIndex = -1;
	
	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 */
	public WrapperedTableModel(final Wrapper wrapper, final String[] keys) {
		this(wrapper, new ArrayList(), keys);
	}

	/**
	 * @param controller
	 *            see {@link #wrapper}
	 * @param objectResourceList
	 *            see {@link #list}
	 */
	public WrapperedTableModel(final Wrapper controller, final List objectResourceList, final String[] keys) {
		this.wrapper = controller;
		this.ascendings = new boolean[keys.length];
		this.editables = new boolean[keys.length];
		this.keys = keys;
		this.setValues(objectResourceList);
	}

	/**
	 * clear model
	 */
	public void clear() {
		this.list.clear();
		super.fireTableDataChanged();
	}

	/**
	 * override {@link AbstractTableModel#getColumnClass(int)}method
	 */
	public Class getColumnClass(final int columnIndex) {
		final String key = this.keys[columnIndex];
		return this.wrapper.getPropertyClass(key);
	}

	/**
	 * override {@link javax.swing.table.TableModel#getColumnCount()}method
	 */
	public int getColumnCount() {
		return this.keys.length;
	}

	public String getColumnName(final int columnIndex) {
		final String key = this.keys[columnIndex];
		return this.wrapper.getName(key);
	}

	public List getValues() {
		return Collections.unmodifiableList(this.list);
	}

	public int getIndexOfObject(final Object object) {
		return this.list.indexOf(object);
	}

	public Object getObject(final int index) {
		return this.list.get(index);
	}

	public int getRowCount() {
		return this.list.size();
	}

	public Object getValueAt(	final int rowIndex,
	                         	final int columnIndex) {
		final String key = this.keys[columnIndex];
		final Object object = this.list.get(rowIndex);
		Object obj = this.wrapper.getValue(object, key);

		final Object propertyValue = this.wrapper.getPropertyValue(key);
		if (propertyValue instanceof Map) {
			final Map map = (Map) propertyValue;
			Object keyObject = null;
			for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
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
	
	public void setColumnEditable(final int columnIndex,
	                              final boolean editable) {
		
		assert columnIndex < this.editables.length : ErrorMessages.NATURE_INVALID;
			
		this.editables[columnIndex] = editable;
	}

	public boolean isCellEditable(	final int rowIndex,
	                              	final int columnIndex) {
		return this.editables[columnIndex];
	}
	
	public void setKeys(final String[] keys) {
		this.keys = keys;
		this.ascendings = new boolean[this.keys.length];
		this.editables = new boolean[this.keys.length];
		super.fireTableStructureChanged();
	}

	public void setValues(final List list) {
		List list1 = list;
		if (list == null) {
			list1 = new ArrayList();
		}
		this.list = list1;
		
		if (this.lastSortedModelIndex >= 0) {
			// sortRows just do fireTableDataChanged self
			this.sortRows(this.lastSortedModelIndex, this.ascendings[this.lastSortedModelIndex]);
		} else {
			super.fireTableDataChanged();
		}
	}

	public void setValues(final Collection collection) {
		if (this.list == null) {
			this.list = new ArrayList();
		}
		this.list.clear();
		if (collection != null) {
			this.list.addAll(collection);
		}
		
		if (this.lastSortedModelIndex >= 0) {
			// sortRows just do fireTableDataChanged self
			this.sortRows(this.lastSortedModelIndex, this.ascendings[this.lastSortedModelIndex]);
		} else {
			super.fireTableDataChanged();
		}
	}

	public void setValueAt(	final Object obj,
	                       	final int rowIndex,
	                       	final int columnIndex) {
		final String key = this.keys[columnIndex];
		final Object object = this.list.get(rowIndex);
		if (this.wrapper.getPropertyValue(key) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(key);
			this.wrapper.setValue(object, key, map.get(obj));
		} else {
			this.wrapper.setValue(object, key, obj);
		}
		
		if (this.lastSortedModelIndex >= 0) {
			// sortRows just do fireTableDataChanged self
			this.sortRows(this.lastSortedModelIndex, this.ascendings[this.lastSortedModelIndex]);
		} else {
			super.fireTableDataChanged();
		}
	}

	public boolean getSortOrder(int columnIndex) {
		return this.ascendings[columnIndex];
	}

	public void sortRows(int columnIndex) {
		this.ascendings[columnIndex] = !this.ascendings[columnIndex];
		this.sortRows(columnIndex, this.ascendings[columnIndex]);		
	}

	public void sortRows(	final int columnIndex,
	                     	final boolean ascending) {
		if (this.list != null) {
			this.lastSortedModelIndex = columnIndex;
			String sortedKey = this.keys[columnIndex];
			Collections.sort(this.list, new WrapperComparator(this.wrapper, sortedKey, ascending));
			super.fireTableDataChanged();
		}
	}
	
	public int addObject(Object object) {
		return this.addObject(this.list.size(), object);
	}
	
	public int addObject(int index, Object object) {
		this.list.add(index, object);
		if (this.lastSortedModelIndex >= 0) {
			// sortRows just do fireTableDataChanged self
			this.sortRows(this.lastSortedModelIndex, this.ascendings[this.lastSortedModelIndex]);
		} else {
			super.fireTableDataChanged();
		}
		return this.list.indexOf(object);
	}

	public void removeObject(Object object) {
		this.list.remove(object);
		this.fireTableDataChanged();
	}
	
	public void removeObject(int index) {
		this.list.remove(index);
		this.fireTableDataChanged();
	}
}
