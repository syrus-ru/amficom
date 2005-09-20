/*-
 * $Id: XmlBeansTransferable.java,v 1.6 2005/09/20 10:42:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.xml.XmlStorableObject;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/20 10:42:00 $
 * @author $Author: bass $
 * @module general
 */
public interface XmlBeansTransferable<T extends XmlStorableObject> {
	void fromXmlTransferable(final T storableObject, final String importType) throws ApplicationException;

	void getXmlTransferable(final T storableObject, final String importType) throws ApplicationException;
}
