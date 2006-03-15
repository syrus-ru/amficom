/*-
 * $Id: IdlTransferableObjectExt.java,v 1.1.2.1 2006/03/15 13:10:27 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/15 13:10:27 $
 * @module util
 */
public interface IdlTransferableObjectExt<T extends IDLEntity> extends IdlTransferableObject<T> {
	void fromIdlTransferable(final T transferable) throws IdlConversionException;
}
