/*
 * $Id: EvaluationManager.java,v 1.6 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public interface EvaluationManager {

	SetParameter[] evaluate() throws EvaluationException;

	AlarmLevel getAlarmLevel();
}
