/*-
 * $Id: MeasurementCompletedEvent.java,v 1.3 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEvent;
import com.syrus.AMFICOM.reflectometry.Qable;

/**
 * @version $Revision: 1.3 $, $Date: 2006/03/28 10:17:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public interface MeasurementCompletedEvent extends MeasurementStatusChangedEvent<IdlMeasurementCompletedEvent>, Qable {
	//Nothing
}
