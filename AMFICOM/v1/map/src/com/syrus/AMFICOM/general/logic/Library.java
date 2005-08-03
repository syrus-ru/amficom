/*-
 * $Id: Library.java,v 1.5 2005/08/03 14:33:02 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

import java.util.Set;

/**
 * @author Andrei Kroupennikov
 * @author $Author: max $
 * @version $Revision: 1.5 $, $Date: 2005/08/03 14:33:02 $
 * @module map
 */
public interface Library extends LibraryEntry {

	Set<LibraryEntry> getChildren();
	void addChild(final LibraryEntry libraryEntry);
	void removeChild(final LibraryEntry libraryEntry);
}
