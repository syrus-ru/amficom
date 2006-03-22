/*-
 * $Id: MeasurementCompletedEvent.java,v 1.2.4.1 2006/03/19 13:01:37 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEvent;
import com.syrus.AMFICOM.reflectometry.Qable;

/**
 * @version $Revision: 1.2.4.1 $, $Date: 2006/03/19 13:01:37 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public interface MeasurementCompletedEvent extends MeasurementStatusChangedEvent<IdlMeasurementCompletedEvent>, Qable {
	//Nothing
}
