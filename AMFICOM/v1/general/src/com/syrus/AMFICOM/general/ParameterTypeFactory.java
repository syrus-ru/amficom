/*-
 * $Id: ParameterTypeFactory.java,v 1.3 2005/06/21 12:43:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.IdlParameterType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/21 12:43:47 $
 * @module general_v1
 */
final class ParameterTypeFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	@Override
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new ParameterType((IdlParameterType) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	@Override
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((IdlParameterType) transferable).header.id);
	}

	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IDLEntity[] allocateArrayOfTransferables(final int length) {
		return new IdlParameterType[length];
	}
}
