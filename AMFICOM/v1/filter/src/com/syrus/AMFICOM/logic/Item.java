/*
* $Id: Item.java,v 1.2 2005/03/10 15:17:48 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.logic;

import java.util.List;


/**
 * @version $Revision: 1.2 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @module filter_v1
 */
public interface Item {
	
	int getMaxChildrenCount();
	
	int getMaxParentCount();
	
	String getName();
	
	Object getObject();
	
	List getChildren();
	
	void addChild(Item childItem);
	
	void removeChild(Item childItem);
	
	List getParents();
	
	void addParent(Item parent);
	
	void removeParent(Item parent);
	
	void addChangeListener(ItemListener itemListener);
	
	void removeChangeListener(ItemListener itemListener);
}
