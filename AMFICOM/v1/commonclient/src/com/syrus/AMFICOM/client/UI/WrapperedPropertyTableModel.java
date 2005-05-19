
package com.syrus.AMFICOM.client.UI;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class WrapperedPropertyTableModel extends AbstractTableModel {

	private static final long		serialVersionUID	= 4007513055820570639L;

	public static final String KEY_PROPERTY = "Property";
	public static final String KEY_VALUE = "Value";

	/**
	 * Wrapper of Model (ObjectResource) will be used for
	 * sorting. see {@link Wrapper}
	 */
	protected Wrapper	wrapper;

	/**
	 * list of Model elements. 
	 */
	private Object				object;

	/**
	 * saved direction of column sorting. Used when change direction to
	 * negative to current. see {@link com.syrus.util.WrapperComparator#ascend}
	 */
	private boolean[]			ascendings;

	/**
	 * @param controller
	 *                see {@link #wrapper}
	 * @param object
	 */
	public WrapperedPropertyTableModel(Wrapper controller, Object object) {
		this.wrapper = controller;
		this.ascendings = new boolean[this.wrapper.getKeys().size()];
		setObject(object);
	}

	/**
	 * override {@link AbstractTableModel#getColumnClass(int)}method
	 */
	public Class getColumnClass(int columnIndex) 
	{
		Class clazz;
		if(columnIndex == 0)
			clazz = String.class;
		else
			clazz = Object.class;
		return clazz;
	}

//	public Class getColumnClass(int columnIndex) {
//		String key = this.controller.getKey(columnIndex);
//		return this.controller.getPropertyClass(key);
//	}

	/**
	 * override {@link javax.swing.table.TableModel#getColumnCount()}method
	 */
	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) 
	{
		String name = "";

		if(columnIndex == 0)
			name = LangModel.getString(KEY_PROPERTY);
		else
		if(columnIndex == 1)
			name = LangModel.getString(KEY_VALUE);

		return name;
	}

//	public String getColumnName(int columnIndex) {
//		String key = this.controller.getKey(columnIndex);
//		return this.controller.getName(key);
//	}

	public Object getObject() {
		return this.object;
	}

	public int getRowCount() {
		return this.wrapper.getKeys().size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String key = this.wrapper.getKey(rowIndex);
		if (columnIndex == 0)
			return this.wrapper.getName(key);
		Object obj = this.wrapper.getValue(this.object, key);

		if (this.wrapper.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(key);
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

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return false;
		String key = this.wrapper.getKey(rowIndex);
		return this.wrapper.isEditable(key);
	}

	public void setObject(Object object) {
		this.object = object;
		super.fireTableDataChanged();
	}

	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return;
		String key = this.wrapper.getKey(rowIndex);

		//		ObjectResource or = (ObjectResource)
		// this.orList.get(rowIndex);
		if (this.wrapper.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(key);
			this.wrapper.setValue(this.object, key, map.get(obj));
		} else
			this.wrapper.setValue(this.object, key, obj);
		this.fireTableDataChanged();
	}


	public void setController(Wrapper wrapper)
	{
		this.wrapper = wrapper;
		this.fireTableDataChanged();
	}

}
