/*
* $Id: Item.java,v 1.1 2005/02/22 09:02:39 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.logic;

import java.util.Collection;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/22 09:02:39 $
 * @author $Author: bob $
 * @module filter_v1
 */
public interface Item {
	
	int getMaxChildrenCount();
	
	int getMaxParentCount();
	
	public Collection getChildren();
	
	public void addChild(Item childItem);
	
	public void removeChild(Item childItem);
	
	public String getName();
	
	public Object getObject();
}
