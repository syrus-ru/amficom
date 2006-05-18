/*-
 * $Id: XmlComplementor.java,v 1.5 2005/09/20 18:13:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.xml.XmlStorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/09/20 18:13:35 $
 * @module general
 */
public interface XmlComplementor {
	enum ComplementationMode {
		PRE_IMPORT,
		POST_IMPORT,
		EXPORT
	}

	/**
	 * @param storableObject
	 * @param importType
	 * @param mode
	 * @throws CreateObjectException
	 * @throws UpdateObjectException
	 */
	void complementStorableObject(final XmlStorableObject storableObject,
			final String importType,
			final ComplementationMode mode)
	throws CreateObjectException, UpdateObjectException;
}
