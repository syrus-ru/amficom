/*-
 * $Id: ItemListener.java,v 1.4 2005/04/07 10:53:59 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/07 10:53:59 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface ItemListener {

	void setParentPerformed(final Item item, final Item oldParent, final Item newParent);
	
	void setObjectNameChanged(Item item, String oldName, String newName);
}
