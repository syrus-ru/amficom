/*
 * $Id: MeasurementSetupEditor.java,v 1.4 2005/04/14 17:44:21 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.util.Collection;

import com.syrus.AMFICOM.measurement.MeasurementSetup;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/14 17:44:21 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface MeasurementSetupEditor {

	void setMeasurementSetup(MeasurementSetup measurementSetup);
	
	void setMeasurementSetups(Collection measurementSetups);

	MeasurementSetup getMeasurementSetup();
}
