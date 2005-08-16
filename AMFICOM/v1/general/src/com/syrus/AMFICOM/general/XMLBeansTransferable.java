/*-
 * $Id: XMLBeansTransferable.java,v 1.3 2005/08/16 10:52:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/16 10:52:03 $
 * @author $Author: arseniy $
 * @module general
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException;

	XmlObject getXMLTransferable();
}
