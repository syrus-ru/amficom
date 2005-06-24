/*-
 * $Id: LinkFactory.java,v 1.4 2005/06/24 14:21:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.IdlLink;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/24 14:21:00 $
 * @module config_v1
 */
final class LinkFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	@Override
	protected Link newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new Link((IdlLink) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	@Override
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((IdlLink) transferable).header.id);
	}

	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlLink[] allocateArrayOfTransferables(final int length) {
		return new IdlLink[length];
	}
}
