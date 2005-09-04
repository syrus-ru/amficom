/*-
 * $Id: XmlComplementor.java,v 1.3 2005/09/04 12:18:31 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/09/04 12:18:31 $
 * @module general
 */
public interface XmlComplementor {
	/**
	 * @param storableObject
	 * @param importType
	 * @throws CreateObjectException
	 * @throws UpdateObjectException
	 */
	void complementStorableObject(final XmlStorableObject storableObject,
			final String importType)
	throws CreateObjectException, UpdateObjectException;
}
