/*-
 * $Id: ItemListener.java,v 1.6 2005/09/20 12:31:36 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/20 12:31:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface ItemListener {

	void setParentPerformed(final Item item, final Item oldParent, final Item newParent);
	
	void setObjectNameChanged(final Item item, final String oldName, final String newName);
}
