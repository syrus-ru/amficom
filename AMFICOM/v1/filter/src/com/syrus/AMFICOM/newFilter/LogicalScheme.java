/*
 * $Id: LogicalScheme.java,v 1.8 2005/05/18 12:42:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/18 12:42:50 $
 * @author $Author: bass $
 * @module filter_v1
 */
public class LogicalScheme {

	private static final String CONDITION = "Condition ";
	
	private LogicalItem rootItem;	
	private LogicalItem savedRootItem;
	private List keyNames = new LinkedList();
		
	public LogicalScheme() {
		this.rootItem = new LogicalItem(LogicalItem.ROOT);				
	}

	private Set getResultConditions(LogicalItem parentItem) throws CreateObjectException, IllegalDataException {
		Collection children = parentItem.getChildren();
		Set conditions = new HashSet();
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
		if (this.rootItem.getChildren() == Collections.EMPTY_LIST)
			return true;
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
		String name = CONDITION + (this.keyNames.size()+1);
		this.keyNames.add(keyName);
		LogicalItem newItem = new LogicalItem(name, condition);
		LogicalItem oldItem = findConditionItem(name, this.rootItem);
		if (oldItem != null) {
			oldItem.setCondition(condition);
			return;
		}
		if(isDefault()) {
			if(this.rootItem.getChildren() == Collections.EMPTY_LIST) {
				LogicalItem conditionItem = new LogicalItem(name, condition);
				this.rootItem.addChild(conditionItem);
				return;
			}
			LogicalItem rootChild = (LogicalItem) this.rootItem.getChildren().iterator().next();
			if (rootChild.getType().equals(LogicalItem.CONDITION)) {
				//this.rootItem.removeChild(rootChild);
				LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
				//newOrItem.addChild(rootChild);
				newOrItem.addChild(newItem);
				newOrItem.addChild(rootChild);
				this.rootItem.addChild(newOrItem);
			} else {
				rootChild.addChild(newItem);
			}
		} else {
			LogicalItem rootChild = (LogicalItem) this.rootItem.getChildren().iterator().next();
			String type = rootChild.getType();
			if (type.equals(LogicalItem.OR))
				rootChild.addChild(newItem);
			else {
				//this.rootItem.removeChild(rootChild);
				LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
				newOrItem.addChild(rootChild);
				newOrItem.addChild(newItem);
				this.rootItem.addChild(newOrItem);
				
			}	
			
		}
		LogicalItem newRootItem = (LogicalItem)this.rootItem.clone();
		this.rootItem = newRootItem;
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
		int index = this.keyNames.indexOf(keyName);
		this.keyNames.remove(index);
		String name = CONDITION + (index+1);
		LogicalItem conditionItem = findConditionItem(name, this.rootItem);
		LogicalItem parent = (LogicalItem) conditionItem.getParent();
		conditionItem.setParent(null);
		//parent.removeChild(conditionItem);
		if (parent.getChildren().size() < parent.getMinChildrenCount()){
			LogicalItem superParent = (LogicalItem) parent.getParent();
			parent.setParent(null);
			for (Iterator it = parent.getChildren().iterator(); it.hasNext();) {
				LogicalItem child = (LogicalItem) it.next();
				LogicalItem newChilde = (LogicalItem) child.clone();
				superParent.addChild(newChilde);
				//newChildren.setParent(superParent);				
			}
			//superParent.removeChild(parent);
		}
		LogicalItem newRootItem = (LogicalItem)this.rootItem.clone();
		this.rootItem = newRootItem;
		renameConditions(index);
	}

	private void renameConditions(int index) {
		for (int i = index; i < this.keyNames.size(); i++) {
			String name = CONDITION + (i+2);
			LogicalItem conditionItem = findConditionItem(name, this.rootItem);
			conditionItem.setName(CONDITION + (i+1));
		}		
	}
	
	public String getStringCondition() {
		StringBuffer condition = createStringCondition(this.rootItem, new StringBuffer());
		return condition.toString();
	}
	
	private StringBuffer createStringCondition(LogicalItem parentItem, StringBuffer conditionBuff) {
		
		if(parentItem.getChildren().size() > 1)
			conditionBuff.append("(");
		for (Iterator it = parentItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem item = (LogicalItem) it.next();
			if(item.getChildren() == Collections.EMPTY_LIST) {
				conditionBuff.append(" \"");
				conditionBuff.append(item.getName());
				conditionBuff.append("\" ");
			} else
				createStringCondition(item, conditionBuff);
			if(it.hasNext())
				conditionBuff.append(parentItem.getName());
		}
		if(parentItem.getChildren().size() > 1)
			conditionBuff.append(")");
		return conditionBuff;
	}
	
	
}
