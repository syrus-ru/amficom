/*-
 * $Id: IdlTransferableObjectExt.java,v 1.1 2005/12/07 19:21:05 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/12/07 19:21:05 $
 * @module util
 */
public interface IdlTransferableObjectExt<T extends IDLEntity>
		extends IdlTransferableObject<T> {
	void fromIdlTransferable(final T transferable);
}
