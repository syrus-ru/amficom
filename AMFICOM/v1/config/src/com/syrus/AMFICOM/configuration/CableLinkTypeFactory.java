/*-
 * $Id: CableLinkTypeFactory.java,v 1.1 2005/05/25 13:01:09 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.CableLinkType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 13:01:09 $
 * @module config_v1
 */
final class CableLinkTypeFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new CableLinkType((CableLinkType_Transferable) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((CableLinkType_Transferable) transferable).header.id);
	}
}
