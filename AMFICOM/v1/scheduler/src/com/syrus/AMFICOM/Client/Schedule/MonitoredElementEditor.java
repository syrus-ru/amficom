/*
 * $Id: MonitoredElementEditor.java,v 1.4 2005/03/30 14:26:20 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.configuration.MonitoredElement;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/30 14:26:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface MonitoredElementEditor {

	void setMonitoredElement(MonitoredElement monitoredElement);

	MonitoredElement getMonitoredElement();
}
