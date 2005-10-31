/*
 * $Id: AbstractItem.java,v 1.23 2005/10/31 12:30:03 bass Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2005/10/31 12:30:03 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public abstract class AbstractItem implements Item, PropertyChangeListener {

	protected List<Item>				children;

	protected Item				parent;

	/**
	 * Not <code>List</code>, but <em>exactly</em> <code>ArrayList</code>.
	 * @todo Implement smth similar to {@link javax.swing.event.EventListenerList}.
	 */
	protected ArrayList<ItemListener> itemListeners = new ArrayList<ItemListener>();

	public static final String	OBJECT_NAME_PROPERTY	= "ObjectNameProperty";

	protected boolean checkForRecursion(Item child,
										Item parentItem) {
		if (child.equals(parentItem)) { return true; }
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

	public void addChangeListener(final ItemListener itemListener) {
		assert !this.itemListeners.contains(itemListener) : "Listener " + itemListener + " already subsribed to " + this;
		this.itemListeners.add(0, itemListener);
	}

	public void removeChangeListener(final ItemListener itemListener) {
		assert this.itemListeners.contains(itemListener) : "Listener " + itemListener + " have not subsribed to " + this;
		this.itemListeners.remove(itemListener);
	}

	public void propertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		if (propertyName.equals(OBJECT_NAME_PROPERTY)) {
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			this.fireObjectNameChanged(this, oldValue.toString(), newValue.toString());
		}
	}

	public void addChild(Item childItem) {
		Log.debugMessage("this.name: " + this.toString() + " \n\t name: "
				+ childItem.toString(), Log.DEBUGLEVEL10);

		if (!this.canHaveChildren())
			throw new UnsupportedOperationException("Item " + this.getName() + " can not have children.");

		if (this.children == null) {
			this.children = new LinkedList<Item>();
		}

		if (this.checkForRecursion(childItem, this)) { throw new UnsupportedOperationException(
																								"Recursion isn't supported."); }

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

	public List<Item> getChildren() {
		if (this.children != null) {
			return this.children;
		}
		return Collections.emptyList();
	}

	public void setParent(Item parent) {
		Log.debugMessage("name:" + this.toString() + "\n\tparent:"
				+ (parent == null ? "'null'" : parent.toString()), Log.DEBUGLEVEL10);

		Item oldParent = this.parent;
		/* yeah, really compare reference */
		if (parent == oldParent) {
			return;
		}

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
	
	protected void fireParentChanged(final Item item, final Item oldParent, final Item newParent, final Item oldSourceParent) {
		for (int i = 0; i < this.itemListeners.size(); i++) {
			/*
			 * Exactly #get(), not #iterator(), as the latter is
			 * much slower for an ArrayList.
			 */
			final ItemListener itemListener = this.itemListeners.get(i);
			itemListener.setParentPerformed(item, oldParent, newParent);
			Log.debugMessage(this.toString() + " listener[" + i + "(" + itemListener.getClass().getName() + ")"
					+ "].setParentPerformed | item:" + item.toString() + ", oldParent:"
					+ (oldParent == null ? "'null'" : oldParent.toString()) + ", newParent:"
					+ (newParent == null ? "'null'" : newParent.toString()), Log.DEBUGLEVEL10);
		}
		/* yeah, really compare reference */
		// if (thisParent == this)
		// return;
		if (this.parent == null && oldSourceParent == this)
			return;
		if (oldSourceParent instanceof AbstractItem) {
			AbstractItem abstractItem = (AbstractItem) oldSourceParent;
			abstractItem.fireParentChanged(item, oldParent, newParent, oldSourceParent.getParent());
		}
	}

	protected void fireObjectNameChanged(	Item item,
											String oldName,
											String newName) {
		for (Iterator it = this.itemListeners.iterator(); it.hasNext();) {
			ItemListener itemListener = (ItemListener) it.next();
			itemListener.setObjectNameChanged(item, oldName, newName);
		}
		if (this.parent == null)
			return;
		if (this.parent instanceof AbstractItem) {
			AbstractItem abstractItem = (AbstractItem) this.parent;
			abstractItem.fireObjectNameChanged(item, oldName, newName);
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
		//System.out.println(new String(cs) + this.getName());
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

	@Override
	public String toString() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf('.');
		className = lastDotIndex >= 0 ? className.substring(lastDotIndex + 1) : className;
		return this.getName() + '{' + className + '}';
	}
	
	public void clearChildren() {
		if(this.children != null) {
			List<Item> removedChildren = new LinkedList<Item>(this.children);
			this.children.clear();
			for(Item child : removedChildren) {
				if(child instanceof AbstractItem) {
					((AbstractItem)child).fireParentChanged(child, this, null, this);
				}
			}
		}
	}
	
}
