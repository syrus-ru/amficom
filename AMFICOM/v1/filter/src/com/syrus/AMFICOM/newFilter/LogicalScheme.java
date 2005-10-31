/*
 * $Id: LogicalScheme.java,v 1.17 2005/10/31 12:30:03 bass Exp $
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
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalItem;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/10/31 12:30:03 $
 * @author $Author: bass $
 * @module filter
 */
public class LogicalScheme {

	private static final String CONDITION = "Condition ";
	
	private LogicalItem rootItem;	
	private LogicalItem savedRootItem;
	private List<String> keyNames = new LinkedList<String>();
		
	public LogicalScheme() {
		this.rootItem = new LogicalItem(LogicalItem.ROOT);				
	}

	private Set<StorableObjectCondition> getResultConditions(final LogicalItem parentItem) throws CreateObjectException, IllegalDataException {
		final Collection<Item> children = parentItem.getChildren();
		final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>();
		StorableObjectCondition condition;
		for (final Iterator<Item> it = children.iterator(); it.hasNext();) {
			final LogicalItem child = (LogicalItem) it.next();
			final String type = child.getType();
			if (type.equals(LogicalItem.OR)) {
				condition = new CompoundCondition(this.getResultConditions(child), CompoundConditionSort.OR);
			}
			else if (type.equals(LogicalItem.AND)) {
				condition = new CompoundCondition(this.getResultConditions(child), CompoundConditionSort.AND);
			}
			else if (type.equals(LogicalItem.CONDITION)) {
				condition = child.getCondition();
			}
			else {
				throw new IllegalDataException("LogicalScheme getResultConditions | wrong type");
			}
			conditions.add(condition);
		}
		return conditions;
	}

	public StorableObjectCondition getResultCondition() {
		Collection<StorableObjectCondition> collection = null;
		try {
			collection = this.getResultConditions(this.rootItem);
		} catch (CreateObjectException e) {
			Log.errorMessage(e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage(e.getMessage());
		}
		if (collection.size() != 1) {
			return null;
		}
		return collection.iterator().next();
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
		if (this.rootItem.getChildren() == Collections.EMPTY_LIST) {
			return true;
		}
	 	for (final Iterator<Item> it = this.rootItem.getChildren().iterator(); it.hasNext();) {
			final LogicalItem rootChilde = (LogicalItem) it.next();
			final String type = rootChilde.getType();
			if(type.equals(LogicalItem.CONDITION)) {
				return true;
			}
			if(type.equals(LogicalItem.OR)) {
				for (final Iterator<Item> iter = rootChilde.getChildren().iterator(); iter.hasNext();) {
					final LogicalItem item = (LogicalItem) iter.next();
					final String itemType = item.getType();
					if (!itemType.equals(LogicalItem.CONDITION)) {
						return false;
					}
				}
				return true;
			}			
		}
		return false;
	}

	public LogicalItem getSavedRootItem() {
		return this.savedRootItem;
	}

	public void addCondition(final String keyName, final StorableObjectCondition condition) {
		LogicalItem item = getItem(keyName);
		if(item != null) {
			item.setCondition(condition);
			return;
		}
		final String name = CONDITION + (this.keyNames.size() + 1);
		this.keyNames.add(keyName);
		final LogicalItem newItem = new LogicalItem(name, condition);
		final LogicalItem oldItem = this.findConditionItem(name, this.rootItem);
		if (oldItem != null) {
			oldItem.setCondition(condition);
			return;
		}
		if (this.isDefault()) {
			if (this.rootItem.getChildren() == Collections.EMPTY_LIST) {
				final LogicalItem conditionItem = new LogicalItem(name, condition);
				this.rootItem.addChild(conditionItem);
				return;
			}
			final LogicalItem rootChild = (LogicalItem) this.rootItem.getChildren().iterator().next();
			if (rootChild.getType().equals(LogicalItem.CONDITION)) {
				// this.rootItem.removeChild(rootChild);
				final LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
				// newOrItem.addChild(rootChild);
				newOrItem.addChild(newItem);
				newOrItem.addChild(rootChild);
				this.rootItem.addChild(newOrItem);
			} else {
				rootChild.addChild(newItem);
			}
		} else {
			final LogicalItem rootChild = (LogicalItem) this.rootItem.getChildren().iterator().next();
			final String type = rootChild.getType();
			if (type.equals(LogicalItem.OR)) {
				rootChild.addChild(newItem);
			}
			else {
				// this.rootItem.removeChild(rootChild);
				final LogicalItem newOrItem = new LogicalItem(LogicalItem.OR);
				newOrItem.addChild(rootChild);
				newOrItem.addChild(newItem);
				this.rootItem.addChild(newOrItem);
			}
		}

		LogicalItem newRootItem = (LogicalItem) this.rootItem.clone();
		this.rootItem = newRootItem;
	}

	private LogicalItem findConditionItem(final String conditionName, final LogicalItem parentItem) {
		for (final Iterator<Item> iter = parentItem.getChildren().iterator(); iter.hasNext();) {
			final LogicalItem item = (LogicalItem) iter.next();
			if (item.getName().equals(conditionName)) {
				return item;
			}
			final LogicalItem subItem = this.findConditionItem(conditionName, item);
			if (subItem != null) {
				return subItem;
			}
		}
		return null;
	}

	public void removeCondition(final String keyName) {
		final int index = this.keyNames.indexOf(keyName);
		this.keyNames.remove(index);
		final String name = CONDITION + (index + 1);
		final LogicalItem conditionItem = this.findConditionItem(name, this.rootItem);
		final LogicalItem parent = (LogicalItem) conditionItem.getParent();
		conditionItem.setParent(null);
		// parent.removeChild(conditionItem);
		if (parent.getChildren().size() < parent.getMinChildrenCount()) {
			final LogicalItem superParent = (LogicalItem) parent.getParent();
			parent.setParent(null);
			for (final Iterator<Item> it = parent.getChildren().iterator(); it.hasNext();) {
				final LogicalItem child = (LogicalItem) it.next();
				final LogicalItem newChilde = (LogicalItem) child.clone();
				superParent.addChild(newChilde);
				// newChildren.setParent(superParent);
			}
			// superParent.removeChild(parent);
		}
		final LogicalItem newRootItem = (LogicalItem) this.rootItem.clone();
		this.rootItem = newRootItem;
		this.renameConditions(index);
	}

	private void renameConditions(final int index) {
		for (int i = index; i < this.keyNames.size(); i++) {
			final String name = CONDITION + (i + 2);
			final LogicalItem conditionItem = this.findConditionItem(name, this.rootItem);
			conditionItem.setName(CONDITION + (i + 1));
		}
	}

	public String getStringCondition() {
		final StringBuffer condition = this.createStringCondition(this.rootItem, new StringBuffer());
		return condition.toString();
	}

	private StringBuffer createStringCondition(final LogicalItem parentItem, final StringBuffer conditionBuff) {
		if (parentItem.getChildren().size() > 1) {
			conditionBuff.append("(");
		}
		for (final Iterator<Item> it = parentItem.getChildren().iterator(); it.hasNext();) {
			final LogicalItem item = (LogicalItem) it.next();
			if (item.getChildren() == Collections.EMPTY_LIST) {
				conditionBuff.append(" \"");
				conditionBuff.append(item.getName());
				conditionBuff.append("\" ");
			} else {
				this.createStringCondition(item, conditionBuff);
			}
			if (it.hasNext()) {
				conditionBuff.append(parentItem.getName());
			}
		}
		if (parentItem.getChildren().size() > 1) {
			conditionBuff.append(")");
		}
		return conditionBuff;
	}
	
	StorableObjectCondition getCondition(String keyName) {
		LogicalItem conditionItem = getItem(keyName);
		return conditionItem.getCondition();
	}
	
	private LogicalItem getItem(String keyName) {
		int index = this.keyNames.indexOf(keyName);
		if(index == -1) {
			return null;
		}
		String name = CONDITION + (index + 1);
		return findConditionItem(name, this.rootItem);
	}
	

}
