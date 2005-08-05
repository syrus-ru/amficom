/*-
 * $Id: XMLBeansTransferable.java,v 1.1 2005/08/05 15:42:17 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/05 15:42:17 $
 * @author $Author: max $
 * @module map
 */
public interface XMLBeansTransferable {

	void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException;

	XmlObject getXMLTransferable();
}
