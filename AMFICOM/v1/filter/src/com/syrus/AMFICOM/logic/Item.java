/*-
 * $Id: Item.java,v 1.5 2005/03/24 08:04:57 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.List;


/**
 * @version $Revision: 1.5 $, $Date: 2005/03/24 08:04:57 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface Item {
	
	int getMaxChildrenCount();
	
	boolean allowsParents();
	
	boolean allowsChildren();
	
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
