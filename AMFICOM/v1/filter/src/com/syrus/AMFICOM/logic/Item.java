/*-
 * $Id: Item.java,v 1.8 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.List;


/**
 * @version $Revision: 1.8 $, $Date: 2005/08/08 11:37:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface Item {
	
	int getMaxChildrenCount();

	/**
	 * @return <code>true</code> if this <code>Item</code> can have a
	 *         parent <code>Item</code>, <code>false</code> otherwise.
	 */
	boolean canHaveParent();

	/**
	 * @return <code>true</code> if this <code>Item</code> can have
	 *         child <code>Item</code>s, <code>false</code> otherwise.
	 */
	boolean canHaveChildren();

	String getName();
	
	Object getObject();
	
	List<Item> getChildren();
	
	void addChild(final Item childItem);
	
	void setParent(final Item parent);
	
	Item getParent();
	
	void addChangeListener(final ItemListener itemListener);
	
	void removeChangeListener(final ItemListener itemListener);
	
	boolean isService();
}
