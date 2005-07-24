/*
 * $Id: SchemeSymbolContainer.java,v 1.3 2005/07/24 17:10:19 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.resource.BitmapImageResource;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/24 17:10:19 $
 * @module scheme
 */
public interface SchemeSymbolContainer {
	/**
	 * @return <code>symbol</code> associated with this object, or
	 *         <code>null</code> if none.
	 */
	BitmapImageResource getSymbol();

	/**
	 * @param symbol can be <code>null</code>.
	 */
	void setSymbol(final BitmapImageResource symbol);
}
