/*
* $Id: AddDeleteItems.java,v 1.2 2005/03/23 15:06:50 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import com.syrus.AMFICOM.logic.Item;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/23 15:06:50 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface AddDeleteItems {

	void addItem(Item item);
	
	void deleteItem(Item item);
}
