/*
 * $Id: ElementItem.java,v 1.1 2005/03/15 11:42:12 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.logic.AbstractItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 11:42:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class ElementItem extends AbstractItem {

	protected Object	object;

	protected Class		clazz;

	protected ElementItem(Object object) {
		this.object = object;
	}
	
	protected abstract Class getChildenClass();

	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	public final int getMaxParentCount() {
		return 1;
	}	
	
	public Object getObject() {
		return this.object;
	}

	public void addChild(Item childItem) {
		Log.debugMessage("ElementItem.addChild | this.name: " + this.getName() + " \n\t name: " + childItem.getName(),
			Log.FINEST);

		if (this.clazz == null)
			this.clazz = this.getChildenClass();
		
		if (childItem.getObject().getClass().equals(this.clazz)) {

			if (this.children == null)
				this.children = new LinkedList();

			if (checkForRecursion(childItem, this)) { throw new UnsupportedOperationException(
																								"Recursion isn't supported."); }

			if (this.children.contains(childItem))
				return;

			this.children.add(childItem);
			Collection parents1 = childItem.getParents();
			if (parents1 == null || !parents1.contains(this))
				childItem.addParent(this);

			for (int i = 0; i < this.listener.length; i++) {
				this.listener[i].addChildPerformed(this, childItem);
			}
		} else {
			throw new UnsupportedOperationException("Class object " + childItem.getObject().getClass()
					+ " isn't supported.");
		}

	}

	public List getChildren() {
		return this.children;
	}

	public void removeChild(Item childItem) {
		Log.debugMessage("ElementItem.removeChild | this.name: " + this.getName() + "\n\t name: " + childItem.getName(),
			Log.FINEST);
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
		Log.debugMessage("ElementItem.addParent | this.name: " + this.getName() + " \n\t name: " + parent.getName(),
			Log.FINEST);
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
		Log.debugMessage("ElementItem.removeParent | this.name: " + this.getName() + "\n\t name: " + parent.getName(),
			Log.FINEST);
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
	
	public void print(String space) {
		if (space == null || space.length() == 0)
			space = "\t";
		System.out.println(space + this.getName());
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ElementItem element = (ElementItem) it.next();
				element.print(space + space);
			}
		}
	}

}
