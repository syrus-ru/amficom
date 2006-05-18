/*-
 * $Id: XmlTransferableObject.java,v 1.1.6.1 2006/05/18 17:50:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.xml;

import org.apache.xmlbeans.XmlObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1.6.1 $, $Date: 2006/05/18 17:50:01 $
 * @module util
 */
public interface XmlTransferableObject<T extends XmlObject> {
	void fromXmlTransferable(final T transferable, final String importType)
	throws XmlConversionException;

	void getXmlTransferable(final T transferable, final String importType)
	throws XmlConversionException;
}
