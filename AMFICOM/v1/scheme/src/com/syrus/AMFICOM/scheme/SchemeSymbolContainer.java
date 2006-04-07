/*
 * $Id: SchemeSymbolContainer.java,v 1.4 2005/09/18 12:43:13 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.resource.BitmapImageResource;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/09/18 12:43:13 $
 * @module scheme
 */
public interface SchemeSymbolContainer extends Identifiable {
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
