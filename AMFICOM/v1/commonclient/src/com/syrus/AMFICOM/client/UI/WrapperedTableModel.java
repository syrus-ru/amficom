
package com.syrus.AMFICOM.client.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Wrapper;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class WrapperedTableModel extends AbstractTableModel {

	private static final long			serialVersionUID	= 4007513055820570639L;

	/**
	 * ObjectResourceController of Model (StorableObject) will be used for
	 * sorting. see {@link Wrapper}
	 */
	protected Wrapper	wrapper;

//	/**
//	 * ask Kruppen
//	 */
//	private String						domainId			= "";
//
//	/**
//	 * ask Kruppen
//	 */
//	private boolean						doRestrict			= false;

	/**
	 * list of Model (ObjectResouce) elements. see {@link StorableObject}
	 */
	private List						orList;

	/**
	 * saved direction of column sorting. Used when change direction to negative
	 * to current. see {@link WrapperComparator#ascend}
	 */
	private boolean[]					ascendings;

	/**
	 * @param wrapper
	 *            see {@link #wrapper}
	 */
	public WrapperedTableModel(Wrapper wrapper) {
		this(wrapper, new ArrayList());
	}

	/**
	 * @param controller
	 *            see {@link #wrapper}
	 * @param objectResourceList
	 *            see {@link #orList}
	 */
	public WrapperedTableModel(Wrapper controller, List objectResourceList) {
		this.wrapper = controller;
		this.ascendings = new boolean[this.wrapper.getKeys().size()];
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
		String key = this.wrapper.getKey(columnIndex);
		return this.wrapper.getPropertyClass(key);
	}

	/**
	 * override {@link javax.swing.table.TableModel#getColumnCount()}method
	 */
	public int getColumnCount() {
		return this.wrapper.getKeys().size();
	}

	public String getColumnName(int columnIndex) {
		String key = this.wrapper.getKey(columnIndex);
		return this.wrapper.getName(key);
	}

	public List getContents() {
		return this.orList;
	}

//	public String getDomainId() {
//		return this.domainId;
//	}

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
		String key = this.wrapper.getKey(columnIndex);
		Object object =  this.orList.get(rowIndex);
		Object obj = this.wrapper.getValue(object, key);

		Object propertyValue = this.wrapper.getPropertyValue(key);
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
		String key = this.wrapper.getKey(columnIndex);
		return this.wrapper.isEditable(key);
	}

//	public void restrictContents() {
//		List removeItemList = new ArrayList();
//		for (Iterator it = this.orList.iterator(); it.hasNext();) {
//			StorableObject or = (StorableObject) it.next();
//			if (!or.getDomainId().equals(this.domainId))
//				removeItemList.add(or);
//		}
//		this.orList.removeAll(removeItemList);
//		removeItemList.clear();
//		removeItemList = null;
//	}
//
//	public void restrictToDomain(boolean value) {
//		this.doRestrict = value;
//		if (this.doRestrict)
//			restrictContents();
//		super.fireTableDataChanged();
//	}

	public void setContents(List list) {
		if (list == null)
			list = new ArrayList();
		this.orList = list;
//		if (this.doRestrict)
//			restrictContents();
		super.fireTableDataChanged();
	}

	public void setContents(Collection collection) {
		if (this.orList == null)
			this.orList = new ArrayList();
		this.orList.clear();
		if(collection != null)
			this.orList.addAll(collection);
//		if (this.doRestrict)
//			restrictContents();
		super.fireTableDataChanged();
	}

//	public void setDomainId(String domainId) {
//		this.domainId = domainId;
//	}
//
	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		String key = this.wrapper.getKey(columnIndex);
		Object object = this.orList.get(rowIndex);
		if (this.wrapper.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.wrapper.getPropertyValue(key);
			this.wrapper.setValue(object, key, map.get(obj));
		} else
			this.wrapper.setValue(object, key, obj);
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
			String key = this.wrapper.getKey(columnIndex);
			Collections.sort(this.orList, new WrapperComparator(this.wrapper, key, ascending));
		}
	}
}
