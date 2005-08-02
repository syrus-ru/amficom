/*-
 * $Id: LibraryEntry.java,v 1.2 2005/08/02 07:15:11 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

/**
 * @author Andrei Kroupennikov
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 07:15:11 $
 * @module map
 */
public interface LibraryEntry {

	void setParent(final Library library);
	Library getParent();
}
