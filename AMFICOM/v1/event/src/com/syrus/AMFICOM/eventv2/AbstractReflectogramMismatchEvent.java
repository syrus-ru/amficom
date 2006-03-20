/*-
 * $Id: AbstractReflectogramMismatchEvent.java,v 1.5.2.1 2006/03/20 13:26:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5.2.1 $, $Date: 2006/03/20 13:26:14 $
 * @module event
 */
public abstract class AbstractReflectogramMismatchEvent extends StorableObject
		implements ReflectogramMismatchEvent {
	public final EventType getType() {
		return REFLECTORGAM_MISMATCH;
	}

	protected String paramString() {
		final double deltaX = this.getDeltaX();
		final int coord = this.getCoord();
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; resultId = " + this.getResultId()
				+ "; deltaX = " + deltaX
				+ "; coord = " + coord
				+ "; distance = " + this.getDistance() + " = " + deltaX + " * " + coord;
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}

	@Override
	public abstract IdlReflectogramMismatchEvent getIdlTransferable(final ORB orb);

	@Override
	protected final Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	@Override
	protected final AbstractReflectogramMismatchEventWrapper getWrapper() {
		return AbstractReflectogramMismatchEventWrapper.getInstance();
	}
}
