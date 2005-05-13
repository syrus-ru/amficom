
package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.StorableObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import java.util.Iterator;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/13 19:06:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class ObjectResourceTableModel extends AbstractTableModel {

	private static final long			serialVersionUID	= 4007513055820570639L;

	/**
	 * ObjectResourceController of Model (StorableObject) will be used for
	 * sorting. see {@link ObjectResourceController}
	 */
	protected ObjectResourceController	controller;

	/**
	 * ask Kruppen
	 */
	private String						domainId			= "";

	/**
	 * ask Kruppen
	 */
	private boolean						doRestrict			= false;

	/**
	 * list of Model (ObjectResouce) elements. see {@link StorableObject}
	 */
	private List						orList;

	/**
	 * saved direction of column sorting. Used when change direction to negative
	 * to current. see {@link ColumnSorter#ascending}
	 */
	private boolean[]					ascendings;

	/**
	 * @param controller
	 *            see {@link #controller}
	 */
	public ObjectResourceTableModel(ObjectResourceController controller) {
		this(controller, new ArrayList());
	}

	/**
	 * @param controller
	 *            see {@link #controller}
	 * @param objectResourceList
	 *            see {@link #orList}
	 */
	public ObjectResourceTableModel(ObjectResourceController controller, List objectResourceList) {
		this.controller = controller;
		this.ascendings = new boolean[this.controller.getKeys().size()];
		setContents(objectResourceList);
	}

	/**
	 * clear model
	 */
	public void clear() {
		this.orList.clear();
		super.fireTableDataChanged();
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
		return this.controller.getKeys().size();
	}

	public String getColumnName(int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		return this.controller.getName(key);
	}

	public List getContents() {
		return this.orList;
	}

	public String getDomainId() {
		return this.domainId;
	}

	public int getIndexOfObject(Object object) {		
		return this.orList.indexOf(object);
	}

	public Object getObject(int index) {
		return this.orList.get(index);
	}

	public int getRowCount() {
		return this.orList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		Object object =  this.orList.get(rowIndex);
		Object obj = this.controller.getValue(object, key);

		Object propertyValue = this.controller.getPropertyValue(key);
		if (propertyValue instanceof Map) {
			Map map = (Map) propertyValue;
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
		String key = this.controller.getKey(columnIndex);
		return this.controller.isEditable(key);
	}

	public void restrictContents() {
		List removeItemList = new ArrayList();
		for (Iterator it = this.orList.iterator(); it.hasNext();) {
			StorableObject or = (StorableObject) it.next();
			if (!or.getDomainId().equals(this.domainId))
				removeItemList.add(or);
		}
		this.orList.removeAll(removeItemList);
		removeItemList.clear();
		removeItemList = null;
	}

	public void restrictToDomain(boolean value) {
		this.doRestrict = value;
		if (this.doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public void setContents(List list) {
		if (list == null)
			list = new ArrayList();
		this.orList = list;
		if (this.doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public void setContents(Collection collection) {
		if (this.orList == null)
			this.orList = new ArrayList();
		this.orList.clear();
		if(collection != null)
			this.orList.addAll(collection);
		if (this.doRestrict)
			restrictContents();
		super.fireTableDataChanged();
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		Object object = this.orList.get(rowIndex);
		if (this.controller.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(key);
			this.controller.setValue(object, key, map.get(obj));
		} else
			this.controller.setValue(object, key, obj);
		this.fireTableDataChanged();
	}

	public boolean getSortOrder(int columnIndex) {
		return this.ascendings[columnIndex];
	}

	public void sortRows(int columnIndex) {
		sortRows(columnIndex, this.ascendings[columnIndex]);
		this.ascendings[columnIndex] = !this.ascendings[columnIndex];
	}

	public void sortRows(int columnIndex, boolean ascending) {
		if (this.orList != null) {
			String key = this.controller.getKey(columnIndex);
			Collections.sort(this.orList, new ColumnSorter(this.controller, key, ascending));
		}
	}
}
