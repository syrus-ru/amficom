/*-
 * $Id: ReflectogramMismatchEvent.java,v 1.10 2006/06/08 18:22:18 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/06/08 18:22:18 $
 * @module event
 */
public interface ReflectogramMismatchEvent
		extends Event<IdlReflectogramMismatchEvent>,
		ReflectogramMismatch, Identifiable,
		Comparable<ReflectogramMismatchEvent> {
	Date getCreated();

	/**
	 * Non-nullable.
	 */
	Identifier getMeasurementId();
}
