/*
 * $Id: LogicalConditionScheme.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JFrame;

import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalItem;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class LogicalConditionScheme {
	
	private LogicalItem rootItem = new LogicalItem(LogicalItem.ROOT);
	private LogicalItem savedRootItem;
	private JFrame parentFrame;
	private LogicalConditionUITemp logicalConditionUITemp;
		
	public LogicalConditionScheme(Map keyNameCondition, JFrame parentFrame) {
		
		this.parentFrame = parentFrame;
		
		LogicalItem orItem = new LogicalItem(LogicalItem.OR);
		this.rootItem.addChild(orItem);
		for (Iterator iter = keyNameCondition.keySet().iterator(); iter.hasNext();) {
			String conditionName = (String) iter.next();
			LogicalItem conditionItem = new LogicalItem(LogicalItem.CONDITION, conditionName);
			orItem.addChild(conditionItem);			
		}
		show();		
	}

	private Collection getResultConditions(LogicalItem logicalRootItem) throws CreateObjectException, IllegalDataException {
		Collection children = logicalRootItem.getChildren();
		Collection conditions = new LinkedList();
		StorableObjectCondition condition;
		for (Iterator it = children.iterator(); it.hasNext();) {
			LogicalItem child = (LogicalItem) it.next();
			if(child.getObject() instanceof CompoundConditionSort) {
				CompoundConditionSort sort = (CompoundConditionSort) child.getObject(); 
				condition = new CompoundCondition(getResultConditions(child), sort);
			} else if(child.getObject() instanceof StorableObjectCondition) {
				condition = (StorableObjectCondition) child.getObject();
			} else {
				throw new IllegalDataException("LogicalConditionScheme.getResultConditions | Wrong type" + child.getObject());
			}
			conditions.add(condition);
		}
		return conditions;
	}
	
	public StorableObjectCondition getResultCondition() throws IllegalDataException, CreateObjectException {
		Collection collection = getResultConditions(this.rootItem);
		if(collection.size()>1)
			throw new IllegalDataException("LogicalConditionScheme.getResultCondition | Size of collection must be 1. " +
					"Root element has more than one logical child");
		return (StorableObjectCondition) collection.iterator().next();
	}

	public Item getRootItem() {
		return this.rootItem;
	}
	
	public void show() {
		hold();
		this.logicalConditionUITemp = new LogicalConditionUITemp(this, this.parentFrame);
	}
	
	public void restore() {
		this.rootItem = this.savedRootItem;		
	}
	
	private void hold() {
		this.savedRootItem = (LogicalItem) this.rootItem.clone();
	
	}
	
	private boolean isValid(LogicalItem item, LogicalConditionUITemp temp) {
		if(item.getChildrenCount() > item.getMaxChildrenCount() 
				|| item.getChildrenCount() < item.getMinChildrenCount()) {
			
			return false;				
		}
		return true;
	}
	
	public boolean getValidation(LogicalConditionUITemp temp) {
		if (getConditionCount(this.rootItem) < getConditionCount(this.savedRootItem)) {
			temp.showErrorMessage("Condition out of link");
			return false;
		}
		if (!isValid(this.rootItem,temp))
			return false;
		return iterate(this.rootItem,temp);
	}
	
	private boolean iterate(LogicalItem parentItem, LogicalConditionUITemp temp) {
		if(parentItem.getChildren() == null)
			return true;
		for (Iterator it = parentItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem item = (LogicalItem) it.next();
			if(!isValid(item, temp))
				return false;
			if(item.getMaxChildrenCount() != 0)
				return iterate(item,temp);
		}
		return true;
	}
	
	private int getConditionCount(LogicalItem parentItem) {
		int number = 0;
		return getConditionCount0(parentItem, number);
	}
	
	private int getConditionCount0(LogicalItem parentItem, int number) {
		for (Iterator it = parentItem.getChildren().iterator(); it.hasNext();) {
			LogicalItem item = (LogicalItem) it.next();
			if (item.getMaxChildrenCount() == 0)
				number++;
			else 
				number = getConditionCount0(item, number);		
		}
		
		return number;
	}
	
}
