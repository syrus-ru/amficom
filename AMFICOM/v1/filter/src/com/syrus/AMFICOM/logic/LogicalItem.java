/*
 * $Id: LogicalItem.java,v 1.2 2005/03/10 15:17:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalItem extends AbstractItem implements Item {

	public final static String	AND		= "AND";
	public final static String	OR		= "OR";
	public final static String	ROOT	= "Result";

	private int					maxChildrenCount;
	private int					maxParentCount;

	private String				name;
	private Object				type;
	
	private static int 			andCount = 0;
	private static int 			orCount = 0;
	private static int 			conditionCount = 0;
	
	public LogicalItem(StorableObjectCondition condition) {
		this.type = condition;
		this.maxChildrenCount = 0;
		this.maxParentCount = 1;
		this.name = "Condition" + conditionCount++;
	}

	public LogicalItem(String sort) {
		if (sort.equals(OR)) {
			this.type = CompoundConditionSort.OR;
			this.name = OR + orCount++;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.maxParentCount = 1;
		} else if (sort.equals(AND)) {
			this.type = CompoundConditionSort.AND;
			this.name = AND + andCount++;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.maxParentCount = 1;
		} else if (sort.equals(ROOT)) {
			this.type = ROOT;
			this.name = ROOT;
			this.maxChildrenCount = 1;
			this.maxParentCount = 0;
		} else
			throw new UnsupportedOperationException("LogicalItem.<init> | Operation " + sort + " is not supported.");		
	}

	public void addChild(Item childItem) {		
		System.out.println("LogicalItem.addChild | this.name: " + this.name + " \n\t name: " + childItem.getName() );
		if (this.children == null)
			this.children = new LinkedList();
		
		if (checkForRecursion(childItem, this )) {
			 throw new UnsupportedOperationException("Recursion isn't supported.");
		}

		if (this.children.contains(childItem))
			return;
		
		this.children.add(childItem);	
		Collection parents1 = childItem.getParents();
		if (parents1 == null || !parents1.contains(this))
			childItem.addParent(this);
		
		for (int i = 0; i < this.listener.length; i++) {
			this.listener[i].addChildPerformed(this, childItem);
		}
	}

	public List getChildren() {
		return this.children;
	}

	public void removeChild(Item childItem) {
		System.out.println("LogicalItem.removeChild | this.name: " + this.name + "\n\t name: " + childItem.getName() );
		if (this.children != null) {
			this.children.remove(childItem);
		}

		Collection parents1 = childItem.getParents();
		if (parents1 != null && parents1.contains(this))
			childItem.removeParent(this);		
		
		for (int i = 0; i < this.listener.length; i++) {
			this.listener[i].removeChildPerformed(this, childItem);
		}

	}

	public List getParents() {
		return this.parents;
	}	

	public void addParent(Item parent) {
		System.out.println("LogicalItem.addParent | this.name: " + this.name + " \n\t name: " + parent.getName() );
		if ((this.parents == null && this.maxParentCount == 0)
				|| (this.parents != null && this.parents.size() > this.maxParentCount))
			throw new UnsupportedOperationException("There cannot be more than " + this.maxParentCount
				+ " parent items at item '" + this.name + "', parent item '" + parent.getName() + '\'');
		if (this.parents == null)
			this.parents = new LinkedList();
		
		if (this.parents.contains(parent))
			return;
		
		this.parents.add(parent);
		
		Collection children1 = parent.getChildren();
		if (children1 == null || !children1.contains(this))
			parent.addChild(this);
		
		for (int i = 0; i < this.listener.length; i++) {
			this.listener[i].addParentPerformed(this, parent);
		}

	}
	
	public void removeParent(Item parent) {
		System.out.println("LogicalItem.removeParent | this.name: " + this.name + "\n\t name: " + parent.getName() );
		if (this.parents != null) {
			this.parents.remove(parent);
		}

		Collection children1 = parent.getChildren();
		if (children1 != null && children1.contains(this))			
			parent.removeChild(this);
		
		for (int i = 0; i < this.listener.length; i++) {
			this.listener[i].removeParentPerformed(this, parent);
		}
	}
	
	public int getMaxChildrenCount() {
		return this.maxChildrenCount;
	}

	public int getMaxParentCount() {
		return this.maxParentCount;
	}

	public String getName() {
		return this.name;
	}

	public Object getObject() {
		return this.type;
	}	
}
