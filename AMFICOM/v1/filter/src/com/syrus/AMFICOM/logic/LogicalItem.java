/*
 * $Id: LogicalItem.java,v 1.15 2005/08/09 21:10:10 arseniy Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Iterator;

import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.15 $, $Date: 2005/08/09 21:10:10 $
 * @author $Author: arseniy $
 * @module filter
 */
public class LogicalItem extends AbstractItem {

	public static final String AND = "AND";

	public static final String CONDITION = "Condition";

	public static final String OR = "OR";

	public static final String ROOT = "Result";

	private static int andCount = 0;

	private static int orCount = 0;

	private int maxChildrenCount;

	private int maxParentCount;

	private int minChildrenCount;

	private String name;

	private String type;

	private StorableObjectCondition condition;

	public LogicalItem(final String sort) {
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
			throw new UnsupportedOperationException("LogicalItem.<init> | Operation " + sort + " is not supported.");
		}
	}

	public LogicalItem(final String name, final StorableObjectCondition condition) {
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

	public void setCondition(final StorableObjectCondition condition) {
		this.condition = condition;
	}

	public void childClone(final LogicalItem currentParentItem, final LogicalItem newParentItem) {
		for (final Iterator<Item> iter = currentParentItem.getChildren().iterator(); iter.hasNext();) {
			final LogicalItem item = (LogicalItem) iter.next();
			final String itemType = item.getType();
			if (itemType.equals(OR) || itemType.equals(AND)) {
				final LogicalItem newItem = new LogicalItem(item.getType());
				newParentItem.addChild(newItem);
				this.childClone(item, newItem);
			} else if (itemType.equals(CONDITION)) {
				final LogicalItem newItem = new LogicalItem(item.getName(), item.getCondition());
				newParentItem.addChild(newItem);
			} else {
				System.err.println("LogicalItem.childClone() | wrong type");
			}
		}
	}

	public String getType() {
		return this.type;
	}

	@Override
	public Object clone() {
		final String type1 = this.getType();
		if (type1.equals(CONDITION)) {
			return new LogicalItem(this.getName(), this.getCondition());
		}
		final LogicalItem newItem = new LogicalItem(this.getType());
		this.childClone(this, newItem);
		return newItem;
	}

	public int getChildrenCount() {
		if (this.children == null) {
			return 0;
		}
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
	
	public void setName(final String name) {
		this.name = name;
	}

//	public List getParents() {
//		return this.parents;
//	}

//		Collection parents1 = childItem.getParents();
//		if (parents1 != null && parents1.contains(this))
//			childItem.removeParent(this);
//
//		for (int i = 0; i < this.listener.length; i++) {
//			this.listener[i].removeChildPerformed(this, childItem);
//		}



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
