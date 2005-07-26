/*-
 * $Id: XMLBeansTransferable.java,v 1.4 2005/07/26 11:46:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.4 $, $Date: 2005/07/26 11:46:32 $
 * @author $Author: arseniy $
 * @module map
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(XmlObject xmlObject, ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
