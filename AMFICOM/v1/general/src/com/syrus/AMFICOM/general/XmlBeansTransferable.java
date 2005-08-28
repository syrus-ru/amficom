/*-
 * $Id: XmlBeansTransferable.java,v 1.1 2005/08/28 11:32:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.apache.xmlbeans.XmlObject;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/28 11:32:59 $
 * @author $Author: bass $
 * @module general
 */
public interface XmlBeansTransferable<T extends XmlObject> {

	void fromXmlTransferable(final T xmlObject, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException;

	T getXmlTransferable();
}
