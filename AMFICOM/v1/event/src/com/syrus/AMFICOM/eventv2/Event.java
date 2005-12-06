/*-
 * $Id: Event.java,v 1.3 2005/12/06 09:42:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.util.IdlTransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/12/06 09:42:28 $
 * @module event
 */
public interface Event<T extends IdlEvent> extends IdlTransferableObject<T> {
	EventType getType(); 
}
