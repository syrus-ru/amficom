/*
 * $Id: AbstractItem.java,v 1.9 2005/03/28 07:47:12 bob Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/28 07:47:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public abstract class AbstractItem implements Item {

	protected List				children;

	protected Item				parent;

	protected ItemListener[]	listener	= new ItemListener[0];

	protected boolean checkForRecursion(Item child,
										Item parentItem) {
		if (child.equals(parentItem)) {
			return true;
		}
		boolean recursionExists = false;
		List children2 = child.getChildren();
		if (children2 != null && !children2.isEmpty()) {
			for (Iterator it = children2.iterator(); it.hasNext();) {
				Item item1 = (Item) it.next();
				if (item1.equals(parentItem)) {
					recursionExists = true;
					break;
				}
				recursionExists = this.checkForRecursion(item1, parentItem);
				if (recursionExists)
					break;
			}
		}
		return recursionExists;
	}

	public void addChangeListener(ItemListener itemListener) {
		ItemListener[] itemListeners = new ItemListener[this.listener.length + 1];
		System.arraycopy(this.listener, 0, itemListeners, 1, this.listener.length);
		itemListeners[0] = itemListener;
		this.listener = itemListeners;
	}

	public void removeChangeListener(ItemListener itemListener) {
		int index = -1;
		for (int i = 0; i < this.listener.length; i++) {
			if (this.listener[i].equals(itemListener)) {
				index = i;
				break;
			}
		}
		if (index >= -1) {
			ItemListener[] itemListeners = new ItemListener[this.listener.length - 1];
			System.arraycopy(this.listener, 0, itemListeners, 0, index);
			System.arraycopy(this.listener, index + 1, itemListeners, index, itemListeners.length - index);
			this.listener = itemListeners;
		}
	}

	public void addChild(Item childItem) {
		Log.debugMessage("AbstractItem.addChild | this.name: " + this.toString() + " \n\t name: "
				+ childItem.toString(), Log.FINEST);
		
		if (!this.canHaveChildren())
			throw new UnsupportedOperationException("Item " + this.getName() + " can not have children.");
		
		if (this.children == null) {
			this.children = new LinkedList();
		}

		if (this.checkForRecursion(childItem, this)) { throw new UnsupportedOperationException("Recursion isn't supported."); }

		if (this.children.contains(childItem))
			return;

		if (this.isService() || childItem.canHaveParent()) {
			if (childItem.getParent() != this) {
				this.children.add(childItem);
				childItem.setParent(this);
			}
		} else {
			throw new UnsupportedOperationException("Parent for " + childItem.toString() + " isn't allow.");
		}
	}

	public List getChildren() {
		return this.children == null ? Collections.EMPTY_LIST : this.children;
	}

	public void setParent(Item parent) {
		Log.debugMessage("AbstractItem.setParent | name:" + this.toString() +"\n\tparent:"
				+ (parent == null ? "'null'" : parent.toString()), Log.FINEST);

		Item oldParent = this.parent;
		/* yeah, really compare reference */
		if (parent == oldParent)
			return;

		if (oldParent != null) {
			List oldParentChildren = oldParent.getChildren();
			if (!oldParentChildren.isEmpty()) {
				oldParentChildren.remove(this);
			}
		}

		this.parent = parent;
		if (this.parent != null) {
			List children2 = this.parent.getChildren();
			if (!children2.isEmpty() && children2.contains(this))
				this.parent.addChild(this);
		}
		
		Item notNullParent = this.parent == null ? oldParent : this.parent;
		this.fireParentChanged(this, oldParent, this.parent, notNullParent);
	}
	
	protected void fireParentChanged(Item item, Item oldParent, Item newParent, Item oldSourceParent) {
		for (int i = 0; i < this.listener.length; i++) {
			this.listener[i].setParentPerformed( item, oldParent, newParent);
			Log.debugMessage(this.toString() + " listener[" + i + "(" + this.listener[i].getClass().getName() + ")" + "].setParentPerformed | item:" + item.toString()
				+ ", oldParent:" + (oldParent == null ? "'null'" : oldParent.toString())
				+ ", newParent:" + (newParent == null ? "'null'" : newParent.toString()), Log.FINEST);
		}
		/* yeah, really compare reference */
//		if (thisParent == this)
//			return;
		if (this.parent == null && oldSourceParent == this)
			return;
		if (oldSourceParent instanceof AbstractItem) {
			AbstractItem abstractItem = (AbstractItem) oldSourceParent;
			abstractItem.fireParentChanged(item, oldParent, newParent, oldSourceParent.getParent());
		}
	}

	public Item getParent() {
		return this.parent;
	}
	
	public void print(final int deep) { 
		char[] cs = new char[deep];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = '\t';
		}
		System.out.println(new String(cs) + this.getName());
		if (this.children != null) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				Item item = (Item) it.next();
				if (item instanceof AbstractItem) {
					AbstractItem abstractItem = (AbstractItem) item;
					abstractItem.print(deep + 1);
				}
			}
		}
	}
	
	public String toString() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf('.');
		className = lastDotIndex >=0 ? className.substring(lastDotIndex + 1) : className;
		return this.getName() + '{' + className + '}';
	}
}
