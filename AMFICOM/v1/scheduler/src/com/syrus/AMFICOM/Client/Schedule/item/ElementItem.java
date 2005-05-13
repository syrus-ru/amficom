/*
 * $Id: ElementItem.java,v 1.6 2005/05/13 15:07:30 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import java.util.Iterator;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.AbstractItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/13 15:07:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class ElementItem extends AbstractItem {

	protected Identifier	identifier;

	protected short entityCode = ObjectEntities.UNKNOWN_ENTITY_CODE;

	protected ElementItem(Identifier identifier) {
		this.identifier = identifier;
	}

	protected abstract short getChildenEntityCode();

	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	public Object getObject() {
		return this.identifier;
	}	

	public void addChild(Item childItem) {
//		Log.debugMessage("ElementItem.addChild | this.name: " + this.getName() + " \n\t name: " + childItem.getName(),
//			Log.FINEST);

		if (this.entityCode == ObjectEntities.UNKNOWN_ENTITY_CODE)
			this.entityCode = this.getChildenEntityCode();

		Object object2 = childItem.getObject();
		if (childItem.isService() || ((Identifier) object2).getMajor() == this.entityCode) {

			super.addChild(childItem);
		} else {
			throw new UnsupportedOperationException("Class object " + object2.getClass() + " isn't supported.");
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
