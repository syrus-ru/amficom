/*
 * $Id: MeasurementSetupEditor.java,v 1.3 2005/04/14 06:38:05 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.util.Set;

import com.syrus.AMFICOM.measurement.MeasurementSetup;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/14 06:38:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface MeasurementSetupEditor {

	void setMeasurementSetup(MeasurementSetup measurementSetup);
	
	void setMeasurementSetups(Set measurementSetups);

	MeasurementSetup getMeasurementSetup();
}
