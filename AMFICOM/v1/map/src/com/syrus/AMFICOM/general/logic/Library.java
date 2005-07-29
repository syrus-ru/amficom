/*-
 * $Id: Library.java,v 1.1 2005/07/29 12:49:28 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

import java.util.List;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/07/29 12:49:28 $
 * @module scheme_v1
 */
public interface Library extends LibraryEntry {

	List getChildren();
	void addChild(final LibraryEntry libraryEntry);
	void removeChild(final LibraryEntry libraryEntry);
}
