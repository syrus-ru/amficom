/**
 * $Id: XMLBeansTransferable.java,v 1.1 2005/05/30 14:50:23 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

public interface XMLBeansTransferable {
	void fromXMLTransferable(XmlObject xmlObject, ClonedIdsPool clonedIdsPool)
		throws ApplicationException;
	XmlObject getXMLTransferable();
}
