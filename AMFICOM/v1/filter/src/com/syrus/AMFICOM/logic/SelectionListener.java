/*
* $Id: SelectionListener.java,v 1.3 2005/08/09 19:41:15 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.logic;

import java.util.Collection;


/**
 * @version $Revision: 1.3 $, $Date: 2005/08/09 19:41:15 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface SelectionListener {

	void selectedItems(Collection<Item> items);
}
