
package com.syrus.AMFICOM.client_.general.ui_;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/07 11:31:08 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ObjPropertyTableModel extends AbstractTableModel {

	private static final long		serialVersionUID	= 4007513055820570639L;

	/**
	 * ObjectResourceController of Model (ObjectResource) will be used for
	 * sorting. see {@link ObjectResourceController}
	 */
	protected ObjectResourceController	controller;

	/**
	 * list of Model (ObjectResouce) elements. see {@link ObjectResource}
	 */
	private Object				object;

	/**
	 * saved direction of column sorting. Used when change direction to
	 * negative to current. see {@link ColumnSorter#ascending}
	 */
	private boolean[]			ascendings;

	/**
	 * @param controller
	 *                see {@link #controller}
	 * @param objectResourceList
	 *                see {@link #orList}
	 */
	public ObjPropertyTableModel(ObjectResourceController controller, Object object) {
		this.controller = controller;
		this.ascendings = new boolean[this.controller.getKeys().size()];
		setObject(object);
	}

	/**
	 * override {@link AbstractTableModel#getColumnClass(int)}method
	 */
	public Class getColumnClass(int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		return this.controller.getPropertyClass(key);
	}

	/**
	 * override {@link javax.swing.table.TableModel#getColumnCount()}method
	 */
	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		return this.controller.getName(key);
	}

	public Object getObject() {
		return this.object;
	}

	public int getRowCount() {
		return this.controller.getKeys().size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String key = this.controller.getKey(rowIndex);
		if (columnIndex == 0)
			return key;
		Object obj = this.controller.getValue(this.object, key);

		if (this.controller.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(key);
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
		String key = this.controller.getKey(rowIndex);
		return this.controller.isEditable(key);
	}

	public void setObject(Object object) {
		this.object = object;
		super.fireTableDataChanged();
	}

	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return;
		String key = this.controller.getKey(rowIndex);

		//		ObjectResource or = (ObjectResource)
		// this.orList.get(rowIndex);
		if (this.controller.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(key);
			this.controller.setValue(this.object, key, map.get(obj));
		} else
			this.controller.setValue(this.object, key, obj);
		this.fireTableDataChanged();
	}

}