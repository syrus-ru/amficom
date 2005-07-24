/*-
 * $Id: LibraryEntry.java,v 1.2 2005/07/24 17:07:45 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.logic;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/24 17:07:45 $
 * @module scheme
 */
public interface LibraryEntry extends Item {
	void setParent(final Library library);
}
