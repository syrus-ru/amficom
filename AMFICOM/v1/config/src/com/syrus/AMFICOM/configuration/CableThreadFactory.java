/*
 * $Id: CableThreadFactory.java,v 1.3 2005/07/03 19:16:23 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.configuration.corba.IdlCableThread;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/03 19:16:23 $
 * @author $Author: bass $
 * @module config_v1
 */
final class CableThreadFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlCableThread[] allocateArrayOfTransferables(final int length) {
		return new IdlCableThread[length];
	}
}
