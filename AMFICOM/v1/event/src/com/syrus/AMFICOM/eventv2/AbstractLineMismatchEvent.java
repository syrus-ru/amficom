/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.4.2.1 2006/03/20 13:26:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4.2.1 $, $Date: 2006/03/20 13:26:14 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent extends StorableObject
		implements LineMismatchEvent {
	public final EventType getType() {
		return LINE_MISMATCH;
	}

	protected String paramString() {
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; resultId = " + this.getResultId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}

	@Override
	public abstract IdlLineMismatchEvent getIdlTransferable(final ORB orb);

	@Override
	protected final Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	@Override
	protected final AbstractLineMismatchEventWrapper getWrapper() {
		return AbstractLineMismatchEventWrapper.getInstance();
	}
}
