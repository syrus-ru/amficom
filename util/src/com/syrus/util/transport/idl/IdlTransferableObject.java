/*-
 * $Id: IdlTransferableObject.java,v 1.2 2006/03/22 08:12:19 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2006/03/22 08:12:19 $
 * @module util
 */
public interface IdlTransferableObject<T extends IDLEntity> {
	T getIdlTransferable(final ORB orb);
}
