/*
 * $Id: LogicalScheme.java,v 1.1 2005/03/25 10:29:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/25 10:29:31 $
 * @author $Author: max $
 * @module filter_v1
 */
public class LogicalScheme {

	private LogicalItem rootItem;
	private LogicalItem savedRootItem;
	
	public LogicalScheme(String conditionName, StorableObjectCondition condition) {
		this.rootItem = new LogicalItem(LogicalItem.ROOT);
		LogicalItem conditionItem = new LogicalItem(conditionName, condition);
		this.rootItem.addChild(conditionItem);		
	}

	private Collection getResultConditions(LogicalItem parentItem) throws CreateObjectException, IllegalDataException {
		Collection children = parentItem.getChildren();
		Collection conditions = new LinkedList();
		StorableObjectCondition condition;
		for (Iterator it = children.iterator(); it.hasNext();) {
			LogicalItem child = (LogicalItem) it.next();
			String type = child.getType();
			if (type.equals(LogicalItem.OR)) 
				condition = new CompoundCondition(getResultConditions(child), CompoundConditionSort.OR);
			else if (type.equals(LogicalItem.AND))
				condition = new CompoundCondition(getResultConditions(child), CompoundConditionSort.AND);
			else if (type.equals(LogicalItem.CONDITION))
				condition = child.getCondition();
			else 
				throw new IllegalDataException("LogicalScheme getResultConditions | wrong type");
			conditions.add(condition);
		}
		return conditions;
	}
	
	public StorableObjectCondition getResultCondition() {
		Collection collection = null;
		try {
			collection = getResultConditions(this.rootItem);
		} catch (CreateObjectException e) {
			Log.errorMessage(e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage(e.getMessage());
		}
		if(collection.size()>1)
			Log.errorMessage("LogicalScheme.getResultCondition | somthing wrong");
		return (StorableObjectCondition) collection.iterator().next();
	}
	
	public LogicalItem getRootItem() {
		return this.rootItem;
	}
	
	public void restore() {
		this.rootItem = this.savedRootItem;		
	}
	
	public void save() {
		this.savedRootItem = (LogicalItem) this.rootItem.clone();
	
	}
	
	private boolean isDefault() {
		for (Iterator it = this.rootItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem rootChilde = (LogicalItem) it.next();
			String type = rootChilde.getType();
			if(type.equals(LogicalItem.CONDITION))
				return true;
			if(type.equals(LogicalItem.OR)) {
				for (Iterator iter = rootChilde.getChildren().iterator(); iter.hasNext();) {
					LogicalItem item = (LogicalItem) iter.next();
					String itemType = item.getType();
					if (!itemType.equals(LogicalItem.CONDITION))
						return false;
				}
				return true;
			}			
		}
		return false;
	}
	
	public LogicalItem getSavedRootItem() {
		return this.savedRootItem;
	}

	public void addCondition(String keyName, StorableObjectCondition condition) {
		LogicalItem newItem = new LogicalItem(keyName, condition);
		LogicalItem oldItem = findConditionItem(keyName, this.rootItem);
		if (oldItem != null) {
			oldItem.setCondition(condition);
			return;
		} 
		if(isDefault()) {
			for (Iterator it = this.rootItem.getChildren().iterator(); it.hasNext();) {
				LogicalItem rootChild = (LogicalItem) it.next();
				if (rootChild.getType().equals(LogicalItem.CONDITION)) {
					this.rootItem.removeChild(rootChild);
					LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
					newOrItem.addChild(rootChild);
					newOrItem.addChild(newItem);
					this.rootItem.addChild(newOrItem);
				} else {
					rootChild.addChild(newItem);
				}
			}
		} else {
			for (Iterator it = this.rootItem.getChildren().iterator(); it.hasNext();) {
				LogicalItem rootChild = (LogicalItem) it.next();
				String type = rootChild.getType();
				if (type.equals(LogicalItem.OR))
					rootChild.addChild(newItem);
				else {
					this.rootItem.removeChild(rootChild);
					LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
					this.rootItem.addChild(newOrItem);
					newOrItem.addChild(rootChild);
					newOrItem.addChild(newItem);
				}	
			}
		}
	}
	
	private LogicalItem findConditionItem(String conditionName, LogicalItem parentItem) {
		for (Iterator iter = parentItem.getChildren().iterator(); iter.hasNext();) {
			LogicalItem item = (LogicalItem) iter.next();
			if (item.getName().equals(conditionName))
				return item;
			LogicalItem subItem = findConditionItem(conditionName, item);
			if (subItem != null)
				return subItem;
		}
		return null;
	}

	public void removeCondition(String keyName) {
		LogicalItem conditionItem = findConditionItem(keyName, this.rootItem);
		LogicalItem parent = (LogicalItem) conditionItem.getParent();
		parent.removeChild(conditionItem);
		if (parent.getChildren().size() < parent.getMinChildrenCount()){
			LogicalItem superParent = (LogicalItem) parent.getParent();
			
			for (Iterator it = parent.getChildren().iterator(); it.hasNext();) {
				LogicalItem child = (LogicalItem) it.next();
				LogicalItem newChildren = (LogicalItem) child.clone();
				newChildren.setParent(superParent);				
			}
			superParent.removeChild(parent);
		}
		LogicalItem rootItem = (LogicalItem)this.rootItem.clone();
		this.rootItem = rootItem;
	}	
}
