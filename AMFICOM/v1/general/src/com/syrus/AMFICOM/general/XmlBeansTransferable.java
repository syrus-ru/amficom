/*-
 * $Id: XmlBeansTransferable.java,v 1.2 2005/08/30 16:05:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.xml.XmlStorableObject;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/30 16:05:28 $
 * @author $Author: bass $
 * @module general
 */
public interface XmlBeansTransferable<T extends XmlStorableObject> {
	void fromXmlTransferable(final T xmlStorableObject, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException;

	T getXmlTransferable();
}
