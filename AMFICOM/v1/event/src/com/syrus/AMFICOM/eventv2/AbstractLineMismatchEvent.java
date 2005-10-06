/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.1 2005/10/06 14:34:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/06 14:34:29 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent implements LineMismatchEvent {
	public final EventType getType() {
		return LINE_MISMATCH;
	}
}
