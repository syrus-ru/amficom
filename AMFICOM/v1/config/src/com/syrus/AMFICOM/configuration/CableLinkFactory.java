/*-
 * $Id: CableLinkFactory.java,v 1.1 2005/06/24 14:21:39 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.IdlCableLink;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/24 14:21:39 $
 * @module config_v1
 */
final class CableLinkFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#newInstance(org.omg.CORBA.portable.IDLEntity)
	 */
	@Override
	protected CableLink newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new CableLink((IdlCableLink) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	@Override
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((IdlCableLink) transferable).header.id);
	}

	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlCableLink[] allocateArrayOfTransferables(final int length) {
		return new IdlCableLink[length];
	}

}
