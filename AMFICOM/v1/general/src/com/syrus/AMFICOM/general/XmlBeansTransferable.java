/*-
 * $Id: XmlBeansTransferable.java,v 1.4 2005/09/09 18:12:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.xml.XmlStorableObject;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:12:53 $
 * @author $Author: bass $
 * @module general
 */
public interface XmlBeansTransferable<T extends XmlStorableObject> {
	void fromXmlTransferable(final T storableObject, final String importType) throws ApplicationException;

	T getXmlTransferable(final String importType) throws ApplicationException;
}
