/*
 * $Id: MeasurementTypeEditor.java,v 1.4 2005/03/21 13:13:36 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/21 13:13:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface MeasurementTypeEditor {

	void setMeasurementType(MeasurementType measurementType);

	MeasurementType getMeasurementType();
}
