/*-
 * $Id: MeasurementCompletedEvent.java,v 1.2 2006/02/21 10:50:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementCompletedEvent;

/**
 * @version $Revision: 1.2 $, $Date: 2006/02/21 10:50:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public interface MeasurementCompletedEvent extends MeasurementStatusChangedEvent<IdlMeasurementCompletedEvent> {

	double getQuality();
}
