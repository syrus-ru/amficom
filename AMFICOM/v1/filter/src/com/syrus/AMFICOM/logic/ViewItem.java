/*
 * $Id: ViewItem.java,v 1.1 2005/02/22 09:02:39 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/22 09:02:39 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class ViewItem implements Item {

	Collection	children;
	
	int			maxChildrenCount;
	int			maxParentCount;
	
	String		name;
	Object		object;
	
	int			width	= -1;
	int			height	= -1;
	int			x		= 0;
	int			y		= 0;

	public ViewItem(int inputCount, int outputCount, String name, Object object, int x, int y) {
		this.maxChildrenCount = inputCount;
		this.maxParentCount = outputCount;
		this.name = name;
		this.object = object;
		this.x = x;
		this.y = y;
	}

	ViewItem(Item item) {
		this.maxChildrenCount = item.getMaxChildrenCount();
		this.maxParentCount = item.getMaxParentCount();
		this.name = item.getName();
		Collection itemChildren = item.getChildren();
		if (itemChildren != null) {
			this.children = new LinkedList();
			for (Iterator it = itemChildren.iterator(); it.hasNext();) {
				Item item2 = (Item) it.next();
				ViewItem viewItem;
				if (item2 instanceof ViewItem) {
					viewItem = (ViewItem) item2;				
				} else
					viewItem = new ViewItem(item2);
//				System.out.println("add " + viewItem.name + " to " + this.name );
				this.children.add(viewItem);
			}
		}
		this.object = item.getObject();
	}

	public void addChild(Item item) {
		if (this.children == null && this.maxChildrenCount == 0)
			throw new UnsupportedOperationException("There cannot be linked input items");

		if (this.children == null)
			this.children = new LinkedList();

		if (this.children.size() < this.maxChildrenCount) {
			ViewItem viewItem;
			if (item instanceof ViewItem) {
				viewItem = (ViewItem) item;				
			} else
				viewItem = new ViewItem(item);
			this.children.add(viewItem);
		}
		else
			throw new UnsupportedOperationException("There cannot be more than " + this.maxChildrenCount
					+ " linked input items");
	}

	public Collection getChildren() {
		return this.children;
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
		return this.object;
	}

	public void removeChild(Item item) {
		if (this.children != null) {
			this.children.remove(item);
		}
	}
}
