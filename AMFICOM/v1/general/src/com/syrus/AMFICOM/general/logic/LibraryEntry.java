/*-
 * $Id: LibraryEntry.java,v 1.1 2005/08/31 05:20:38 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.logic;

import com.syrus.util.Shitlet;

/**
 * @author Andrei Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/08/31 05:20:38 $
 * @module general
 */
@Shitlet
public interface LibraryEntry {
	void setParent(final Library library);
	Library getParent();
}
