/*
 * $Id: SchemeCellContainer.java,v 1.1 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.resource.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public interface SchemeCellContainer extends SchemeSymbolContainer {
	SchemeImageResource getSchemeCell();

	void setSchemeCell(final SchemeImageResource schemeCell);

	SchemeImageResource getUgoCell();

	void setUgoCell(final SchemeImageResource ugoCell);
}
