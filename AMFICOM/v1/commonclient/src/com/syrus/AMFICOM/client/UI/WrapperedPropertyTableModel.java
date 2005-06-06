
package com.syrus.AMFICOM.client.UI;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class WrapperedPropertyTableModel extends AbstractTableModel {

	private static final long	serialVersionUID	= 4007513055820570639L;

	public static final String	KEY_PROPERTY		= "Property";
	public static final String	KEY_VALUE			= "Value";

	/**
	 * Wrapper of Model (ObjectResource) will be used for sorting. see
	 * {@link Wrapper}
	 */
	protected Wrapper			wrapper;

	protected String[]			keys;

	private Object				object;

	/**
	 * saved direction of column sorting. Used when change direction to negative
	 * to current. see {@link com.syrus.util.WrapperComparator#ascend}
	 */
	private boolean[]			ascendings;

	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 * @param object
	 */
	public WrapperedPropertyTableModel(final Wrapper wrapper, final Object object, final String[] keys) {
		this.wrapper = wrapper;
		this.object = object;
		this.keys = keys;
		this.ascendings = new boolean[this.keys.length];
	}

	/**
	 * override {@link AbstractTableModel#getColumnClass(int)}method
	 */
	public Class getColumnClass(int columnIndex) {
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

	public String getColumnName(int columnIndex) {
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

	public Object getObject() {
		return this.object;
	}

	public int getRowCount() {
		return this.keys.length;
	}

	public Object getValueAt(	int rowIndex,
								int columnIndex) {
		// String key = this.wrapper.getKey(rowIndex);
		if (columnIndex == 0)
			return this.wrapper.getName(this.keys[rowIndex]);
		Object obj = this.wrapper.getValue(this.object, this.keys[rowIndex]);

		if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
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

	public boolean isCellEditable(	int rowIndex,
									int columnIndex) {
		if (columnIndex == 0)
			return false;
		// String key = this.wrapper.getKey(rowIndex);
		return this.wrapper.isEditable(this.keys[rowIndex]);
	}

	public void setObject(Object object) {
		this.object = object;
		super.fireTableDataChanged();
	}
	
	public void setKeys(final String[] keys) {
		this.keys = keys;
		this.ascendings = new boolean[this.keys.length];
		super.fireTableDataChanged();
	}

	public void setValueAt(	Object obj,
							int rowIndex,
							int columnIndex) {
		if (columnIndex == 0)
			return;
		// String key = this.wrapper.getKey(rowIndex);

		// ObjectResource or = (ObjectResource)
		// this.orList.get(rowIndex);
		if (this.wrapper.getPropertyValue(this.keys[rowIndex]) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(this.keys[rowIndex]);
			this.wrapper.setValue(this.object, this.keys[rowIndex], map.get(obj));
		} else {
			this.wrapper.setValue(this.object, this.keys[rowIndex], obj);
		}
		this.fireTableDataChanged();
	}

}
