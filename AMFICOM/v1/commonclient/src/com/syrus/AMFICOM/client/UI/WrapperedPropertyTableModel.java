
package com.syrus.AMFICOM.client.UI;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.21 $, $Date: 2005/12/14 12:28:13 $
 * @author $Author: bob $
 * @module commonclient
 */
public class WrapperedPropertyTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 4007513055820570639L;

	public static final String KEY_PROPERTY = "Property";
	public static final String KEY_VALUE = "Value";

	/**
	 * Wrapper of Model (ObjectResource) will be used for sorting. see
	 * {@link Wrapper}
	 */
	protected Wrapper<T> wrapper;

	protected String[] keys;

	protected String[] names;
	protected Object[] values;

	private T t;

	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 * @param t
	 */
	public WrapperedPropertyTableModel(final Wrapper<T> wrapper, final T t, final String[] keys) {
		this.wrapper = wrapper;
		this.t = t;
		this.setKeys(keys);
	}

	/**
	 * override {@link AbstractTableModel#getColumnClass(int)}method
	 */
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		Class clazz;
		if (columnIndex == 0) {
			clazz = String.class;
		} else {
			// TODO really ?
			clazz = Object.class;
		}
		return clazz;
	}

	/**
	 * override {@link javax.swing.table.TableModel#getColumnCount()}method
	 */
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		final String name = "";

//		switch(columnIndex) {
//		case 0:
//			name = I18N.getString("Common.WrapperedPropertyTable.Key");
//			break;
//		case 1:
//			name = I18N.getString("Common.WrapperedPropertyTable.Value");
//			break;
//		default:
//			name = "";
//			break;
//		}

		return name;
	}

	public T getObject() {
		return this.t;
	}

	public int getRowCount() {
		return this.keys.length;
	}
	
	public int getRowIndex(final String key) {
		int index = -1;
		for(int i = 0; i < this.keys.length; i++) {
			if (key.equals(this.keys[i])) {
				index = i;
				break;
			}
		}
		return index;
	}

	public Object getValueAt(final int rowIndex, final int columnIndex) {
		if (columnIndex == 0) {
			return this.names[rowIndex];
		}
		
		Object object = this.values[rowIndex];
		if (object == null) {
			object = this.wrapper.getValue(this.t, this.keys[rowIndex]);
			if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
				final Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
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
			
			this.values[rowIndex] = object;
		}
		return object;
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		if (columnIndex == 0) {
			return false;
		}
		// String key = this.wrapper.getKey(rowIndex);
		return this.wrapper.isEditable(this.keys[rowIndex]);
	}

	public void setObject(final T t) {
		this.t = t;
		this.fireTableDataChanged();
	}

	public void setKeys(final String[] keys) {
		final int oldKeysSize = this.keys != null ? this.keys.length : -1;
		this.keys = keys;
		if (keys.length > oldKeysSize) {
			this.names = new String[keys.length];
			this.values = new Object[keys.length];
		} else {
			for(int i = oldKeysSize; i < keys.length; i++) {
				this.names[i] = null;
			}
		}
		for(int i = 0; i < keys.length; i++) {
			final String name = this.wrapper.getName(keys[i]);
			if (name.indexOf('\n') > -1) {
				this.names[i] = CommonUIUtilities.convertToHTMLString(name);
			} else {
				this.names[i] = name;
			}
		}
		this.fireTableDataChanged();
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		if (this.values != null) {
			for(int i = 0; i < this.values.length; i++) {
				this.values[i] = null;
			}
		}
	}
	
	@Override
	public void fireTableRowsUpdated(	int firstRow,
										int lastRow) {
		for(int index = firstRow; index <= lastRow; index++) {
			this.values[index] = null;
		}
		super.fireTableRowsUpdated(firstRow, lastRow);
	}
	
	@Override
	public void setValueAt(final Object object, final int rowIndex, final int columnIndex) {
		if (columnIndex == 0) {
			return;
		}
		if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
			this.values[rowIndex] = map.get(object);
			this.wrapper.setValue(this.t, this.keys[rowIndex], this.values[rowIndex]);			
		} else {
			this.values[rowIndex] = object;
			this.wrapper.setValue(this.t, this.keys[rowIndex], object);
		}
		super.fireTableRowsUpdated(rowIndex, rowIndex);
	}

}
