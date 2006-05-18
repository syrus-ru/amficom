/*-
 * $Id: IdlTransferableObjectExt.java,v 1.2 2006/03/13 16:28:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/13 16:28:56 $
 * @module util
 */
public interface IdlTransferableObjectExt<T extends IDLEntity>
		extends IdlTransferableObject<T> {
	void fromIdlTransferable(final T transferable)
	throws IdlConversionException;
}
