/*
 * $Id: ElementItem.java,v 1.3 2005/03/21 13:13:36 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import java.util.Iterator;

import com.syrus.AMFICOM.logic.AbstractItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/21 13:13:36 $
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

	public Object getObject() {
		return this.object;
	}

	public void addChild(Item childItem) {
		Log.debugMessage("ElementItem.addChild | this.name: " + this.getName() + " \n\t name: " + childItem.getName(),
			Log.FINEST);

		if (this.clazz == null)
			this.clazz = this.getChildenClass();

		Object object2 = childItem.getObject();
		if (childItem.isService() || object2.getClass().equals(this.clazz)) {

			super.addChild(childItem);
		} else {
			throw new UnsupportedOperationException("Class object " + object2.getClass()
					+ " isn't supported.");
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

	public boolean isService() {		
		return false;
	}
}
