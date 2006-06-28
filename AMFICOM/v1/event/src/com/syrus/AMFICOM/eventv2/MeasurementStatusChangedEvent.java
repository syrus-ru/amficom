/*-
 * $Id: MeasurementStatusChangedEvent.java,v 1.1 2006/02/20 17:14:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlMeasurementStatusChangedEvent;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/20 17:14:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public interface MeasurementStatusChangedEvent<T extends IdlMeasurementStatusChangedEvent> extends Event<T> {

	Identifier getMeasurementId();
}
