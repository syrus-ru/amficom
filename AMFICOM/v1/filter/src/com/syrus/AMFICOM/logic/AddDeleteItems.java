/*
* $Id: AddDeleteItems.java,v 1.1 2005/03/10 15:17:48 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.logic;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface AddDeleteItems {

	void addItem(Item item);
	
	void deleteItem(Item item);
}
