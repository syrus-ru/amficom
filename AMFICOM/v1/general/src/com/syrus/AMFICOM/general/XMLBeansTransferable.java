/*-
 * $Id: XMLBeansTransferable.java,v 1.2 2005/08/12 12:26:28 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/12 12:26:28 $
 * @author $Author: arseniy $
 * @module general
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
