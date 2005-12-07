/*-
 * $Id: Event.java,v 1.4 2005/12/07 17:16:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/12/07 17:16:25 $
 * @module event
 */
public interface Event<T extends IdlEvent> extends IdlTransferableObject<T> {
	EventType getType(); 
}
