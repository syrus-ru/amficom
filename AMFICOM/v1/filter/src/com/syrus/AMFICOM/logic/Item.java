/*-
 * $Id: Item.java,v 1.4 2005/03/23 15:04:49 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.List;


/**
 * @version $Revision: 1.4 $, $Date: 2005/03/23 15:04:49 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface Item {
	
	int getMaxChildrenCount();
	
	boolean allowsParents();
	
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
