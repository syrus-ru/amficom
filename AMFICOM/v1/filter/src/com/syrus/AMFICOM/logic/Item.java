/*-
 * $Id: Item.java,v 1.6 2005/03/24 08:19:29 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.List;


/**
 * @version $Revision: 1.6 $, $Date: 2005/03/24 08:19:29 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter_v1
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
	
	List getChildren();
	
	void addChild(final Item childItem);
	
	void setParent(final Item parent);
	
	Item getParent();
	
	void addChangeListener(final ItemListener itemListener);
	
	void removeChangeListener(final ItemListener itemListener);
	
	boolean isService();
}
