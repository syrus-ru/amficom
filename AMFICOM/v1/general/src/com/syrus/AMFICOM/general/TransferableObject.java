/*-
 * $Id: TransferableObject.java,v 1.13 2005/10/07 09:51:16 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/07 09:51:16 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface TransferableObject<T extends IDLEntity> extends Serializable {
	T getTransferable(final ORB orb);
}
