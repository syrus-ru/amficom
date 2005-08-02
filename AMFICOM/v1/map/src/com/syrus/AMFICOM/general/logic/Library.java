/*-
 * $Id: Library.java,v 1.4 2005/08/02 18:06:19 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

import java.util.Set;

/**
 * @author Andrei Kroupennikov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/02 18:06:19 $
 * @module map
 */
public interface Library extends LibraryEntry {

	Set<LibraryEntry> getChildren();
	void addChild(final LibraryEntry libraryEntry);
	void removeChild(final LibraryEntry libraryEntry);
}
