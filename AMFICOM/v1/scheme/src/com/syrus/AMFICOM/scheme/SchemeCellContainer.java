/*
 * $Id: SchemeCellContainer.java,v 1.2 2005/03/21 16:46:50 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.resource.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/21 16:46:50 $
 * @module scheme_v1
 */
public interface SchemeCellContainer extends SchemeSymbolContainer {
	/**
	 * @return <code>schemeCell</code> associated with this object, or
	 *         <code>null</code> if none.
	 */
	SchemeImageResource getSchemeCell();

	/**
	 * @param schemeCell can be <code>null</code>.
	 */
	void setSchemeCell(final SchemeImageResource schemeCell);

	/**
	 * @return <code>ugoCell</code> associated with this object, or
	 *         <code>null</code> if none.
	 */
	SchemeImageResource getUgoCell();

	/**
	 * @param ugoCell can be <code>null</code>.
	 */
	void setUgoCell(final SchemeImageResource ugoCell);
}
