/*
 * $Id: LogicalItem.java,v 1.4 2005/03/16 08:18:30 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/16 08:18:30 $
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

	private Object				type;

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

	public LogicalItem(String sort, String name) {
		if (sort.equals(CONDITION)) {
			this.type = CONDITION;
			this.maxChildrenCount = 0;
			this.minChildrenCount = 0;
			this.maxParentCount = 1;
			this.name = name;
		} else {
			throw new UnsupportedOperationException(
					"LogicalItem.<init> | Operation " + sort
							+ " is not supported.");
		}
	}

	public void addChild(Item childItem) {
		Log.debugMessage("LogicalItem.addChild | this.name: " + this.name
				+ " \n\t name: " + childItem.getName(), Log.FINEST);
		if (this.children == null)
			this.children = new LinkedList();

		if (checkForRecursion(childItem, this)) {
			throw new UnsupportedOperationException(
					"Recursion isn't supported.");
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

	public void addParent(Item parent) {
		Log.debugMessage("LogicalItem.addParent | this.name: " + this.name
				+ " \n\t name: " + parent.getName(), Log.FINEST);
		if ((this.parents == null && this.maxParentCount == 0)
				|| (this.parents != null && this.parents.size() > this.maxParentCount))
			throw new UnsupportedOperationException(
					"There cannot be more than " + this.maxParentCount
							+ " parent items at item '" + this.name
							+ "', parent item '" + parent.getName() + '\'');
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

	public void childClone(LogicalItem currentParentItem,
			LogicalItem newParentItem) {
		for (Iterator iter = currentParentItem.getChildren().iterator(); iter
				.hasNext();) {
			LogicalItem item = (LogicalItem) iter.next();
			String type = (String) item.getObject();
			if (type.equals(OR) || item.getObject().equals(AND)) {
				LogicalItem newItem = new LogicalItem((String) item.getObject());
				newParentItem.addChild(newItem);
				childClone(item, newItem);
			} else if (type.equals(CONDITION)) {
				LogicalItem newItem = new LogicalItem(LogicalItem.CONDITION,
						item.getName());
				newParentItem.addChild(newItem);
			} else {
				Log.errorMessage("LogicalItem.childClone() | wrong type");
			}
		}
	}

	public Object clone() {
		LogicalItem newRoot = new LogicalItem(ROOT);
		childClone(this, newRoot);
		return newRoot;
	}

	public List getChildren() {
		return this.children;
	}

	public int getChildrenCount() {
		if (this.getChildren() == null)
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

	public Object getObject() {
		return this.type;
	}

	public List getParents() {
		return this.parents;
	}

	public void removeChild(Item childItem) {
		Log.debugMessage("LogicalItem.removeChild | this.name: " + this.name
				+ "\n\t name: " + childItem.getName(), Log.FINEST);
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

	public void removeParent(Item parent) {
		Log.debugMessage("LogicalItem.removeParent | this.name: " + this.name
				+ "\n\t name: " + parent.getName(), Log.FINEST);
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
}
