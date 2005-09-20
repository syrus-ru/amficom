/*-
 * $Id: AbstractChildrenFactory.java,v 1.2 2005/09/20 11:46:01 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/09/20 11:46:01 $
 * @author Stanislav Kholshin
 * @module filter 
 */
public abstract class AbstractChildrenFactory implements ChildrenFactory {

	protected List<StorableObject> getObjectsToAdd(
			final Collection<? extends StorableObject> newObjs, 
			final Collection<? extends Object> existingObjs) {
		final List<StorableObject> toAdd = new LinkedList<StorableObject>();
		for (final StorableObject itObj : newObjs) {
			if (!existingObjs.contains(itObj)) {
				toAdd.add(itObj);
			}	
 		}
 		return toAdd;
 	}
	
 	protected List<Item> getItemsToRemove(
 			final Collection<? extends StorableObject> newObjs, 
 			final Collection<? extends Item> existingObjs) {
 		final List<Item> toRemove = new LinkedList<Item>(existingObjs);
		for (final Item childItem : existingObjs) {
			if (newObjs.contains(childItem.getObject())) {
				toRemove.remove(childItem);
 			}
 		}
 		return toRemove;
 	}
 	
	protected Collection<Object> getChildObjects(final Item node) {
		final Collection<Object> childObjects = new LinkedList<Object>();
		for (final Item item : node.getChildren()) {
			childObjects.add(item.getObject());
		}
		return childObjects;
	}
}
