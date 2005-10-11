/*-
 * $Id: ReflectogramMismatchEvent.java,v 1.3 2005/10/11 13:16:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/11 13:16:34 $
 * @module event
 */
public interface ReflectogramMismatchEvent
		extends Event<IdlReflectogramMismatchEvent>,
		ReflectogramMismatch {
	Identifier getMonitoredElementId();
}
