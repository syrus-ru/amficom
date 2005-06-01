/*-
 * $Id: MCMFactory.java,v 1.1 2005/06/01 17:09:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.1 $, $Date: 2005/06/01 17:09:18 $
 * @module admin_v1
 */
final class MCMFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new MCM((MCM_Transferable) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((MCM_Transferable) transferable).header.id);
	}
}
