package com.syrus.AMFICOM.client.general.ui;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client.resource.ObjectResourceController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import java.util.Iterator;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ObjectResourceTableModel extends AbstractTableModel {

	protected ObjectResourceController	controller;
	private String						domainId	= "";

	private boolean						doRestrict	= false;

	private List						orList;

	private boolean[]					sortOrders;

	public ObjectResourceTableModel(ObjectResourceController controller) {
		this(controller, new ArrayList());
	}

	public ObjectResourceTableModel(ObjectResourceController controller, List objectResourceList) {
		this.controller = controller;
		this.sortOrders = new boolean[this.controller.getKeys().size()];
		setContents(objectResourceList);
	}

	public void clear() {
		this.orList.clear();
		super.fireTableDataChanged();
	}

	public Class getColumnClass(int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		return this.controller.getPropertyClass(key);
	}

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

	public ObjectResource getObjectResource(int index) {
		return (ObjectResource) this.orList.get(index);
	}

	public int getRowCount() {
		return this.orList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		ObjectResource or = (ObjectResource) this.orList.get(rowIndex);
		Object obj = this.controller.getValue(or, key);

		if (this.controller.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(key);
			Object keyObject = null;
			for(Iterator it=map.keySet().iterator();it.hasNext();){
				Object keyObj = it.next();
				if (map.get(keyObj).equals(obj)){
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
			ObjectResource or = (ObjectResource) it.next();
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

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		String key = this.controller.getKey(columnIndex);
		ObjectResource or = (ObjectResource) this.orList.get(rowIndex);
		if (this.controller.getPropertyValue(key) instanceof Map) {
			Map map = (Map) this.controller.getPropertyValue(key);
			this.controller.setValue(or, key, map.get(obj));
		} else
			this.controller.setValue(or, key, obj);
		this.fireTableDataChanged();
	}

	public boolean getSortOrder(int columnIndex) {
		return this.sortOrders[columnIndex];
	}

	public void sortRows(int columnIndex) {
		sortRows(columnIndex, this.sortOrders[columnIndex]);
		this.sortOrders[columnIndex] = !this.sortOrders[columnIndex];
	}

	public void sortRows(int columnIndex, boolean ascending) {
		if (this.orList != null) {
			String key = this.controller.getKey(columnIndex);
			Collections.sort(this.orList, new ColumnSorter(this.controller, key, ascending));
		}
	}
}