/*
 * $Id: Item.java,v 1.1 2005/03/15 16:11:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.Collection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 16:11:44 $
 * @author $Author: max $
 * @module misc
 */
public interface Item {
	
	public Collection getChildren();//getInput
	public void addChild(LogicalItem childItem);//addInput
	public void removeChild(LogicalItem childItem);//removeItem
	public String getName();
	public Object getObject(); //getObject()
}
