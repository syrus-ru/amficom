/*
 * $Id: LogicalItem.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public class LogicalItem implements Item {
	
	private String name;
	private Object type;
	
	private Collection children;
	/*private interface Item {
		Collection getInput();
		int getInputCount();
		int getOutputCount();
		void addInput(ItemImpl item);
		void removeItem(ItemImpl item);
		Object getObject();
		String getName();
	}*/
	
	public LogicalItem(StorableObjectCondition condition) {
		this.type = condition;
		this.name = condition.toString();		
	}
	
	public LogicalItem(String sort) {
		if(sort.equals(ItemWtapper.OR)) {
			this.type = CompoundConditionSort.OR;
			this.name = ItemWtapper.OR;
		} else if(sort.equals(ItemWtapper.AND)) {
			this.type = CompoundConditionSort.AND;
			this.name = ItemWtapper.AND;
		} else if(sort.equals(ItemWtapper.ROOT)) {
			this.type = ItemWtapper.ROOT;
			this.name = ItemWtapper.ROOT;
		} else
			throw new UnsupportedOperationException("Item.Item | Operation " + sort + " is not supported.");
	}
		
	public void addChild(LogicalItem childItem) {
		if(this.children == null)
			this.children = new LinkedList();
		this.children.add(childItem);		
	}
	
	public void removeChild(LogicalItem childItem) {
		this.children.remove(childItem);
	}
	
	public String getName() {
		return this.name;
	}
	public Collection getChildren() {
		return this.children;
	}
	public Object getObject() {
		return this.type;
	}
}
