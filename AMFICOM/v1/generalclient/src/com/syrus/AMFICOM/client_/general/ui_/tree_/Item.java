/*
* $Id: Item.java,v 1.1 2005/03/23 10:19:14 bob Exp $
*
* Copyright ? 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/23 10:19:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface Item {
	
	int getMaxChildrenCount();
	
	boolean isParentAllow();
	
	String getName();
	
	Object getObject();
	
	List getChildren();
	
	void addChild(Item childItem);
	
	void setParent(Item parent);
	
	Item getParent();
	
	void addChangeListener(ItemListener itemListener);
	
	void removeChangeListener(ItemListener itemListener);
	
	boolean isService();
}
