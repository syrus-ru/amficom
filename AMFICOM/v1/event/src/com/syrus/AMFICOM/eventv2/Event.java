/*-
 * $Id: Event.java,v 1.4.4.1 2006/03/21 13:46:43 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.4.4.1 $, $Date: 2006/03/21 13:46:43 $
 * @module event
 */
public interface Event<T extends IdlEvent> extends IdlTransferableObjectExt<T> {
	EventType getType(); 
}
