/*
 * $Id: EvaluationManager.java,v 1.5 2004/07/21 08:26:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.5 $, $Date: 2004/07/21 08:26:06 $
 * @author $Author: arseniy $
 * @module 
 */

public interface EvaluationManager {

	SetParameter[] evaluate() throws EvaluationException;

	AlarmLevel getAlarmLevel();
}
