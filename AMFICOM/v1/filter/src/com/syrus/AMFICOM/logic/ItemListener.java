/*
 * $Id: ItemListener.java,v 1.2 2005/03/21 08:41:34 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/21 08:41:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface ItemListener {

	void setParentPerformed(Item item, Item oldParent, Item newParent);
}
