/*-
 * $Id: IdlTransferableObject.java,v 1.1.4.1 2006/03/27 11:21:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @version $Revision: 1.1.4.1 $, $Date: 2006/03/27 11:21:22 $
 * @module util
 */
public interface IdlTransferableObject<T extends IDLEntity> {
	T getIdlTransferable(final ORB orb);
}
