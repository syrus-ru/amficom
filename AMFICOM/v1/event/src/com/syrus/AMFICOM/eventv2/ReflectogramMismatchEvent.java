/*-
 * $Id: ReflectogramMismatchEvent.java,v 1.2 2005/10/07 14:58:57 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/07 14:58:57 $
 * @module event
 */
public interface ReflectogramMismatchEvent
		extends Event<IdlReflectogramMismatchEvent>,
		ReflectogramMismatch {
	// empty
}
