/*-
 * $Id: Library.java,v 1.1 2005/03/24 09:40:15 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/03/24 09:40:15 $
 * @module scheme_v1
 */
public interface Library extends Item {
	void addChild(final Library library);

	void addChild(final LibraryEntry libraryEntry);

	void setParent(final Library library);
}
