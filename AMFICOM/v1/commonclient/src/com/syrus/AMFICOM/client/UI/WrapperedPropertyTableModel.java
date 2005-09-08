
package com.syrus.AMFICOM.client.UI;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/08 14:06:38 $
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

	private T object;

	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 * @param object
	 */
	public WrapperedPropertyTableModel(final Wrapper<T> wrapper, final T object, final String[] keys) {
		this.wrapper = wrapper;
		this.object = object;
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
		String name;

		if (columnIndex == 0) {
			name = LangModel.getString(KEY_PROPERTY);
		} else if (columnIndex == 1) {
			name = LangModel.getString(KEY_VALUE);
		} else {
			name = "";
		}

		return name;
	}

	public T getObject() {
		return this.object;
	}

	public int getRowCount() {
		return this.keys.length;
	}

	public Object getValueAt(final int rowIndex, final int columnIndex) {
		// String key = this.wrapper.getKey(rowIndex);
		if (columnIndex == 0) {
			return this.names[rowIndex];
		}
		Object obj = this.wrapper.getValue(this.object, this.keys[rowIndex]);

		if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
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

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		if (columnIndex == 0) {
			return false;
		}
		// String key = this.wrapper.getKey(rowIndex);
		return this.wrapper.isEditable(this.keys[rowIndex]);
	}

	public void setObject(final T object) {
		this.object = object;
		super.fireTableDataChanged();
	}

	public void setKeys(final String[] keys) {
		final int oldKeysSize = this.keys != null ? this.keys.length : 0;
		this.keys = keys;
		if (keys.length > oldKeysSize) {
			this.names = new String[keys.length];
		}
		for(int i = 0; i < keys.length; i++) {			
			this.names[i] = CommonUIUtilities.convertToHTMLString(this.wrapper.getName(keys[i]));
//			this.names[i] = this.wrapper.getName(keys[i]);
		}
		super.fireTableDataChanged();
	}

	@Override
	public void setValueAt(final Object obj, final int rowIndex, final int columnIndex) {
		if (columnIndex == 0) {
			return;
		}
		// String key = this.wrapper.getKey(rowIndex);

		// ObjectResource or = (ObjectResource)
		// this.orList.get(rowIndex);
		if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
			final Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
			this.wrapper.setValue(this.object, this.keys[rowIndex], map.get(obj));
		} else {
			this.wrapper.setValue(this.object, this.keys[rowIndex], obj);
		}
		this.fireTableDataChanged();
	}

}
