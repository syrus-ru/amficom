/*-
 * $Id: EventSourceFactory.java,v 1.5 2005/07/04 13:00:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.event.corba.IdlEventSource;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/04 13:00:47 $
 * @module event_v1
 */
final class EventSourceFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlEventSource[] allocateArrayOfTransferables(final int length) {
		return new IdlEventSource[length];
	}
}
