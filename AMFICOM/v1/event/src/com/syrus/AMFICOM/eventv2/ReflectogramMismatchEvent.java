/*-
 * $Id: ReflectogramMismatchEvent.java,v 1.8 2006/03/30 12:10:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
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
 * @version $Revision: 1.8 $, $Date: 2006/03/30 12:10:05 $
 * @module event
 */
public interface ReflectogramMismatchEvent
		extends Event<IdlReflectogramMismatchEvent>,
		ReflectogramMismatch, Identifiable {
	Date getCreated();

	/**
	 * Nullable if {@code Measurement} referenced is deleted; a void
	 * identifier is returned in this case.
	 */
	Identifier getMeasurementId();
}
