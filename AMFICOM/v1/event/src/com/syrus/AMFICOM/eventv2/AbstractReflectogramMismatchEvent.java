/*-
 * $Id: AbstractReflectogramMismatchEvent.java,v 1.1 2005/10/06 14:34:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/06 14:34:29 $
 * @module event
 */
public abstract class AbstractReflectogramMismatchEvent
		implements ReflectogramMismatchEvent {
	public final EventType getType() {
		return REFLECTORGAM_MISMATCH;
	}
}
