/*-
 * $Id: XmlComplementor.java,v 1.1 2005/09/04 10:52:00 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/09/04 10:52:00 $
 * @module general
 */
public interface XmlComplementor {
	/**
	 * @param storableObject
	 * @throws UpdateObjectException
	 */
	void complementStorableObject(final XmlStorableObject storableObject) throws UpdateObjectException;
}
