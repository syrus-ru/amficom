/*
* $Id: AbstractItem.java,v 1.1 2005/03/10 15:17:48 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.logic;

import java.util.Iterator;
import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public abstract class AbstractItem implements Item {

	protected List				children;

	protected List				parents;	

	protected ItemListener[]	listener		= new ItemListener[0];	
	
	protected boolean checkForRecursion(Item child, Item parent) {
		List children2 = child.getChildren();
		boolean recursionExists = false;
		if (children2 != null && !children2.isEmpty()) {
			for (Iterator it = children2.iterator(); it.hasNext();) {
				Item item1 = (Item) it.next();
				if (item1.equals(parent)) {
					recursionExists = true;
					break;
				} 
				recursionExists = this.checkForRecursion(item1, parent);
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

}
