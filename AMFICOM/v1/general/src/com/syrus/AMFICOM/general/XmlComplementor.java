/*-
 * $Id: XmlComplementor.java,v 1.4 2005/09/20 13:00:11 bass Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/09/20 13:00:11 $
 * @module general
 */
public interface XmlComplementor {
	enum ComplementationMode {
		IMPORT,
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
