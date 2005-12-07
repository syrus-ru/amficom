/*-
 * $Id: IdlTransferableObject.java,v 1.1 2005/12/07 17:15:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

import java.io.Serializable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/12/07 17:15:29 $
 * @module util
 */
public interface IdlTransferableObject<T extends IDLEntity> extends Serializable {
	T getIdlTransferable(final ORB orb);
}
