/*-
 * $Id: Event.java,v 1.5 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public interface Event<T extends IdlEvent> extends IdlTransferableObjectExt<T> {
	EventType getType(); 
}
