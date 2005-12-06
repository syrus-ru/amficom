/*-
 * $Id: XmlTransferableObject.java,v 1.1 2005/12/06 13:08:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import org.apache.xmlbeans.XmlObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/12/06 13:08:54 $
 * @module util
 */
public interface XmlTransferableObject<T extends XmlObject> {
	void fromXmlTransferable(final T transferable, final String importType)
	throws XmlConversionException;

	void getXmlTransferable(final T transferable, final String importType,
			final boolean usePool)
	throws XmlConversionException;
}
