/*-
 * $Id: ItemListener.java,v 1.3 2005/03/23 15:04:49 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/23 15:04:49 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface ItemListener {

	void setParentPerformed(final Item item, final Item oldParent, final Item newParent);
}
