/*-
 * $Id: TransferableObject.java,v 1.1 2005/10/07 10:04:21 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.Serializable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/07 10:04:21 $
 * @module util
 */
public interface TransferableObject<T extends IDLEntity> extends Serializable {
	T getTransferable(final ORB orb);
}
