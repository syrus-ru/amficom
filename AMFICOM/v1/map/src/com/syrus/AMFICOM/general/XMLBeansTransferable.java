/*-
 * $Id: XMLBeansTransferable.java,v 1.3 2005/07/26 11:45:17 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * $Id: XMLBeansTransferable.java,v 1.3 2005/07/26 11:45:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(XmlObject xmlObject, ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
