/**
 * $Id: XMLBeansTransferable.java,v 1.2 2005/07/26 08:57:21 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

public interface XMLBeansTransferable {

	void fromXMLTransferable(XmlObject xmlObject, ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
