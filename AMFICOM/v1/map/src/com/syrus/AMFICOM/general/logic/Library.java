/*-
 * $Id: Library.java,v 1.3 2005/08/02 16:47:44 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

import java.util.List;

/**
 * @author Andrei Kroupennikov
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/08/02 16:47:44 $
 * @module map
 */
public interface Library extends LibraryEntry {

	List<LibraryEntry> getChildren();
	void addChild(final LibraryEntry libraryEntry);
	void removeChild(final LibraryEntry libraryEntry);
}
