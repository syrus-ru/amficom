/*
 * $Id: LogicalItem.java,v 1.9 2005/03/25 10:29:31 max Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Iterator;

import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/25 10:29:31 $
 * @author $Author: max $
 * @module filter_v1
 */
public class LogicalItem extends AbstractItem {

	
	public final static String	AND				= "AND";

	public final static String	CONDITION		= "Condition";

	public final static String	OR				= "OR";

	public final static String	ROOT			= "Result";

	private static int			andCount		= 0;

	private static int			orCount			= 0;

	private int					maxChildrenCount;

	private int					maxParentCount;

	private int					minChildrenCount;

	private String				name;

	private String				type;
	
	private StorableObjectCondition	condition;

	public LogicalItem(String sort) {
		if (sort.equals(OR)) {
			this.type = OR;
			this.name = OR + orCount++;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.minChildrenCount = 2;
			this.maxParentCount = 1;
		} else if (sort.equals(AND)) {
			this.type = AND;
			this.name = AND + andCount++;
			this.maxChildrenCount = Integer.MAX_VALUE;
			this.minChildrenCount = 2;
			this.maxParentCount = 1;
		} else if (sort.equals(ROOT)) {
			this.type = ROOT;
			this.name = ROOT;
			this.maxChildrenCount = 1;
			this.minChildrenCount = 1;
			this.maxParentCount = 0;
		} else {
			throw new UnsupportedOperationException(
					"LogicalItem.<init> | Operation " + sort
							+ " is not supported.");
		}
	}

	public LogicalItem(String name, StorableObjectCondition condition) {
			this.type = CONDITION;
			this.maxChildrenCount = 0;
			this.minChildrenCount = 0;
			this.maxParentCount = 1;
			this.name = name;
			this.condition = condition;
	}
	
	public boolean isService() {
		return false;
	}
	
	public boolean canHaveParent() {
		return !this.type.equals(ROOT);
	}	
	
	public boolean canHaveChildren() {		
		return (this.type.equals(ROOT) || this.type.equals(OR) || this.type.equals(AND));
	}

	public void setCondition(StorableObjectCondition condition) {
		this.condition = condition;
	}
	
	public void childClone(LogicalItem currentParentItem,
			LogicalItem newParentItem) {
		for (Iterator iter = currentParentItem.getChildren().iterator(); iter
				.hasNext();) {
			LogicalItem item = (LogicalItem) iter.next();
			String type = item.getType();
			if (type.equals(OR) || type.equals(AND)) {
				LogicalItem newItem = new LogicalItem(item.getType());
				newParentItem.addChild(newItem);
				childClone(item, newItem);
			} else if (type.equals(CONDITION)) {
				LogicalItem newItem = new LogicalItem(item.getName(), item.getCondition());
				newParentItem.addChild(newItem);
			} else {
				System.err.println("LogicalItem.childClone() | wrong type");
			}
		}
	}


	public String getType() {
		return this.type;
	}

	public Object clone() {
		String type = this.getType();
		if(type.equals(CONDITION))
			return new LogicalItem(this.getName(),this.getCondition());
		LogicalItem newItem = new LogicalItem(this.getType());
		childClone(this, newItem);
		return newItem;
	}

	public int getChildrenCount() {
		if (this.children == null)
			return 0;
		return this.children.size();
	}

	public int getMaxChildrenCount() {
		return this.maxChildrenCount;
	}

	public int getMaxParentCount() {
		return this.maxParentCount;
	}

	public int getMinChildrenCount() {
		return this.minChildrenCount;
	}

	public String getName() {
		return this.name;
	}

//	public List getParents() {
//		return this.parents;
//	}

	public void removeChild(Item childItem) {
		System.out.println("LogicalItem.removeChild | this.name: " + this.name
				+ "\n\t name: " + childItem.getName());
		if (this.children != null) {
			this.children.remove(childItem);
		}

//		Collection parents1 = childItem.getParents();
//		if (parents1 != null && parents1.contains(this))
//			childItem.removeParent(this);
//
//		for (int i = 0; i < this.listener.length; i++) {
//			this.listener[i].removeChildPerformed(this, childItem);
//		}

	}

//	public void removeParent(Item parent) {
//		System.out.println("LogicalItem.removeParent | this.name: " + this.name
//				+ "\n\t name: " + parent.getName());
//		if (this.parents != null) {
//			this.parents.remove(parent);
//		}
//
//		Collection children1 = parent.getChildren();
//		if (children1 != null && children1.contains(this))
//			parent.removeChild(this);
//
//		for (int i = 0; i < this.listener.length; i++) {
//			this.listener[i].removeParentPerformed(this, parent);
//		}
//	}
	public StorableObjectCondition getCondition() {
		return this.condition;
	}

	public Object getObject() {
		//nothig to do by now
		return null;
	}
}
