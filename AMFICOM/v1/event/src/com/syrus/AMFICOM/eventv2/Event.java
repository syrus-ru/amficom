/*-
 * $Id: Event.java,v 1.2 2005/10/07 14:58:57 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.util.TransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/07 14:58:57 $
 * @module event
 */
public interface Event<T extends IdlEvent> extends TransferableObject<T> {
	EventType getType(); 
}
