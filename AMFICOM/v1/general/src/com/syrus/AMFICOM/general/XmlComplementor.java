/*-
 * $Id: XmlComplementor.java,v 1.2 2005/09/04 11:55:51 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/09/04 11:55:51 $
 * @module general
 */
public interface XmlComplementor {
	/**
	 * @param storableObject
	 * @param importType
	 * @throws UpdateObjectException
	 */
	void complementStorableObject(final XmlStorableObject storableObject,
			final String importType)
	throws UpdateObjectException;
}
