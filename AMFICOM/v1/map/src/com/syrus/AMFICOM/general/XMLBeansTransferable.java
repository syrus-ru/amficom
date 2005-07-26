/*-
 * $Id: XMLBeansTransferable.java,v 1.5 2005/07/26 11:47:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/26 11:47:02 $
 * @author $Author: arseniy $
 * @module map
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
